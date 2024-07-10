package com.compscicomputations.services.auth.impl

import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.exceptions.NoSuchUserException
import com.compscicomputations.services.auth.exceptions.singleOrNoSuchUserException
import com.compscicomputations.services.auth.models.*
import com.compscicomputations.utils.PasswordEncryptor
import com.compscicomputations.utils.dbQuery
import com.compscicomputations.utils.executeQuery
import com.compscicomputations.utils.executeUpdate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory
import java.sql.Connection

internal class AuthServiceImpl(
    private val auth: FirebaseAuth
) : AuthService {
    private val logger = LoggerFactory.getLogger("Auth Service")
//    private val conn: Connection by inject(Connection::class.java)

    companion object {
        private val conn: Connection by inject(Connection::class.java)
        internal class ExpiredException : Exception("There admin verification code has expired, request a new one from admin.")
        internal suspend fun isAdminCodeValid(email: String, adminCode: String): Boolean = dbQuery(conn) {
            val hashCode = executeQuery("select ac.hashed_code from auth.admins_codes ac where ac.email = '$email'") {
                it.getString("hashed_code")
            }?.singleOrNull()
            return@dbQuery hashCode?.let {
                PasswordEncryptor.validatePassword(adminCode, it)
            } ?: false
        }
        internal fun String.toSAPhoneNumber(): String {
            return if (startsWith("0")) replaceFirst("0", "+27")
            else this
        }
        internal class UserExistsException(val email: String? = null, val phone: String? = null) :
            Exception("The user${
                when {
                    email == null && phone == null -> ""
                    email == null -> " with the provided phone number: $phone"
                    phone == null -> " with the provided email: $email"
                    else -> " with the provided phone number: $phone and/or email: $email"
                }
            } already exists")
    }

    override suspend fun getUserUidByEmail(email: String): String? = dbQuery(conn) {
        try {
            auth.getUserByEmailAsync(email).get().uid
        } catch (_: FirebaseAuthException) {
            null
        }
    }

    override suspend fun getUserUidByPhone(phone: String): String? = dbQuery(conn) {
        try {
            executeQuery("SELECT u.uid FROM users u WHERE u.phone = '$phone'") { rs ->
                rs.getString("uid")
            }?.singleOrNull()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun createUser(request: CreateUserRequest, admin: Boolean): Unit = dbQuery(conn) {
        val createRequest: UserRecord.CreateRequest = UserRecord.CreateRequest()
            .setEmail(request.email) //Must be a valid email address.
            .setPassword(request.password) //The user's raw, unhashed password. Must be at least six characters long.
            .setDisplayName(request.displayName) //The users' display name.
        if (!request.photoUrl.isNullOrBlank())
            createRequest.setPhotoUrl(request.photoUrl) //The user's photo URL.
        if (!request.phone.isNullOrBlank())
            createRequest.setPhoneNumber(request.phone.toSAPhoneNumber())

        //Create a user if not exists
        var emailExists = false
        var phoneExists = false
        val userRecord = try {
            auth.createUserAsync(createRequest).get()
        } catch (e: Exception) {
            logger.warn(e.localizedMessage)
            when {
                e.message?.contains("EMAIL_EXISTS") == true -> {
                    emailExists = true
                    auth.getUserByEmailAsync(request.email).get()
                }
                e.message?.contains("PHONE_NUMBER_EXISTS") == true -> {
                    phoneExists = true
                    auth.getUserByPhoneNumberAsync(request.phone!!.toSAPhoneNumber()).get()
                }
                else -> throw e
            }
        }
        auth.setCustomUserClaimsAsync(userRecord.uid, mapOf(
            "usertype" to request.usertype.toString()
        ))

        //Insert the user values to the database
        try {
            executeUpdate("""
                insert into auth.users(uid, email, phone, usertype) 
                values ('${userRecord.uid}', '${request.email}', '${request.phone}', '${request.usertype}'::auth.usertype)
                on conflict (email)
                do update 
                set uid = excluded.uid,
                    phone = excluded.phone,
                    usertype = excluded.usertype,
                    updated_at = (now() at time zone 'SAST')
            """.trimIndent())
        } catch (e: Exception) {
            when {
                emailExists -> {
                    throw UserExistsException(email = userRecord.email)
                }
                phoneExists -> {
                    throw UserExistsException(phone = userRecord.phoneNumber)
                }
                e.message?.contains("users_phone_unique") == true -> {
                    auth.deleteUser(userRecord.uid)
                    throw Exception("An unexpected error has occurred, due to user info conflict please ask for help from super admin.")
                }
                else -> throw e
            }
        }
    }

    override suspend fun readUser(uid: String): User = dbQuery(conn) {
        executeQuery("""
            select uid, email, phone, usertype, created_at, updated_at, last_seen_at, banned_until, deleted_at
            from auth.users
            where uid = '$uid'
        """.trimIndent()) {
            User(
                uid = it.getString("uid"),
                email = it.getString("email"),
                phone = it.getString("phone"),
//                usertype = Usertype.valueOf(it.getString("usertype")),
                usertype = Usertype.STUDENT,
                createdAt = it.getTimestamp("created_at"),
                updatedAt = it.getTimestamp("updated_at"),
                lastSeenAt = it.getTimestamp("last_seen_at"),
                bannedUntil = it.getTimestamp("banned_until"),
                deletedAt = it.getTimestamp("deleted_at"),
            )
        }.singleOrNoSuchUserException()
    }

    override suspend fun readFirebaseUser(uid: String): FirebaseUser = dbQuery(conn) {
        try {
            auth.getUserAsync(uid).get()
                .let { row ->
                    FirebaseUser(
                        uid = row.uid,
                        displayName = row.displayName,
                        photoUrl = row.photoUrl,
                        email = row.email,
                        isEmailVerified = row.isEmailVerified,
                        claims = row.customClaims
                    )
                }
        } catch (e: FirebaseAuthException) {
            throw NoSuchUserException()
        }
    }

    override suspend fun updateUser(firebaseUser: FirebaseUser, request: UpdateUserRequest, admin: Boolean): User = dbQuery(conn) {
        val updateRequest: UserRecord.UpdateRequest = UserRecord.UpdateRequest(firebaseUser.uid)
        request.email?.let { updateRequest.setEmail(it) }
        request.password?.let { updateRequest.setPassword(it) }
        request.displayName?.let { updateRequest.setDisplayName(it) }
        request.photoUrl?.let { updateRequest.setPhotoUrl(it) }
        request.phone?.let { it: String -> updateRequest.setPhoneNumber(it) }
        request.isEmailVerified?.let { updateRequest.setEmailVerified(it) }

        val record = auth.updateUserAsync(updateRequest).get()
//        if (request.isAdmin != null) {
//            val claims = mapOf("admin" to request.isAdmin)
//            auth.setCustomUserClaimsAsync(firebaseUser.uid, claims)
//
//            updateUserIsAdmin(firebaseUser.uid, record.email, request.isAdmin, request.adminCode)
//        }

//        if (request.email != null || request.usertype != null) {
//            Users.update({Users.id eq firebaseUser.uid}) {
//                request.email?.apply { it[email] = this }
//                request.usertype?.apply { it[usertype] = this }
//            }
//        }
//
//        User.findById(firebaseUser.uid).ifNullException()
        TODO("Not yet implemented.")
    }

    override suspend fun createAdminCode(request: CreateAdminCodeRequest): Unit = dbQuery(conn) {
        executeUpdate("""
            insert into auth.admins_codes(email, hashed_code) 
            values ('${request.email}', '${request.hashCode}')
            on conflict (email)
            do update
            set hashed_code = excluded.hashed_code,
                created_at = (now() at time zone 'SAST'),
                valid_until = (now() at time zone 'SAST') + '1 day'::interval
        """.trimIndent())
    }

    override suspend fun updateUserIsAdmin(uid: String, email: String, isAdmin: Boolean?, adminCode: String?, admin: Boolean): Unit = dbQuery(conn) {
        if (isAdmin == null) return@dbQuery
//        if (isAdmin) {
//            conn.executeNested("select ac.admin_code from admins_codes ac where ac.email = '$email'") { rs ->
//                val code = rs.getString("admin_code")
//                if (adminCode != code) return@executeNested
//                Users.update({ Users.id eq uid }) {
//                    it[Users.isAdmin] = true
//                }
//            }
//        } else {
//            Users.update({ Users.id eq uid }) {
//                it[Users.isAdmin] = false
//            }
//        }
        TODO("Not yet implemented.")
    }

    override suspend fun deleteUser(uid: String): Unit = dbQuery(conn) {
        auth.deleteUserAsync(uid).get()
//        Users.update({ Users.id eq uid }) {
//            it[deletedAt] = Instant.now()
//        }
        TODO("Not yet implemented.")
    }
}