package com.compscicomputations.services.auth.impl

import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.*
import com.compscicomputations.services.auth.models.requests.NewAdminCode
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.utils.*
import com.google.firebase.auth.*
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.ResultSet


internal class AuthServiceImpl(
    private val auth: FirebaseAuth
) : AuthService {
    private val logger = LoggerFactory.getLogger("Auth Service")
//    private val conn: Connection by inject(Connection::class.java)

    companion object {
        private val conn: Connection by inject(Connection::class.java)
        internal class ExpiredException : Exception("There admin verification code has expired, request a new one from admin.")
        internal suspend fun isAdminCodeValid(email: String, adminCode: String): Boolean = dbQuery(conn) {
            val hashCode = executeQuery("select hash_code from auth.admins_codes where email = '$email'") {
                it.getString("hash_code")
            }?.singleOrNull()
            return@dbQuery hashCode?.let {
                PasswordEncryptor.validatePassword(adminCode, it)
            } ?: false
        }
        internal fun String.toSAPhoneNumber(): String {
            return if (startsWith("0")) replaceFirst("0", "+27")
            else this
        }
        internal class UserExistsException(val email: String? = null, val phone: String? = null) : Exception("The user${
                when {
                    email == null && phone == null -> ""
                    email == null -> " with the provided phone number: $phone"
                    phone == null -> " with the provided email: $email"
                    else -> " with the provided phone number: $phone and/or email: $email"
                }
            } already exists")
    }

    override fun getUserUidByEmail(email: String): String? {
        return try {
            auth.getUserByEmail(email).uid
        } catch (_: FirebaseAuthException) {
            null
        }
    }

    override fun getUserUidByPhone(phone: String): String? {
        return try {
            auth.getUserByPhoneNumber(phone.toSAPhoneNumber()).uid
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun createUser(newUser: NewUser): Unit = dbQuery(conn) {
        val createRequest: UserRecord.CreateRequest = UserRecord.CreateRequest()
            .setEmail(newUser.email) //Must be a valid email address.
        if (!newUser.password.isNullOrBlank())
            createRequest.setDisplayName(newUser.displayName) //The users' display name.
        if (!newUser.password.isNullOrBlank())
            createRequest.setPassword(newUser.password) //The user's raw, not hashed password. Must be at least six characters long.
        if (!newUser.photoUrl.isNullOrBlank())
            createRequest.setPhotoUrl(newUser.photoUrl) //The user's photo URL.
        if (!newUser.phone.isNullOrBlank())
            createRequest.setPhoneNumber(newUser.phone.toSAPhoneNumber())

        //Create an account if not exists
        var emailExists = false
        var phoneExists = false
        val userRecord = try {
            auth.createUserAsync(createRequest).get()
        } catch (e: Exception) {
            logger.warn(e.localizedMessage)
            when {
                e.message?.contains("EMAIL_EXISTS") == true -> {
                    emailExists = true
                    auth.getUserByEmailAsync(newUser.email).get()
                }
                e.message?.contains("PHONE_NUMBER_EXISTS") == true -> {
                    phoneExists = true
                    auth.getUserByPhoneNumberAsync(newUser.phone!!.toSAPhoneNumber()).get()
                }
                else -> throw e
            }
        }
        auth.setCustomUserClaimsAsync(userRecord.uid, mapOf(
            "usertype" to newUser.usertype.toString()
        ))

        //Insert the user values to the database
        try {
            executeUpdate("""
                insert into auth.users(uid, email, display_name, photo_url, phone, usertype) 
                values ('${userRecord.uid}', 
                        '${newUser.email}', ?, ?, ?, 
                        '${newUser.usertype}'::auth.usertype)
                on conflict (email)
                do update 
                set uid = excluded.uid,
                    phone = excluded.phone,
                    usertype = excluded.usertype,
                    updated_at = (now() at time zone 'SAST')
            """.trimIndent()
            ) {
                setString(1, newUser.displayName ?: userRecord.displayName)
                setString(2, newUser.photoUrl ?: userRecord.photoUrl)
                setString(3, newUser.phone ?: userRecord.phoneNumber)
            }
        } catch (e: Exception) {
            when {
                emailExists -> throw UserExistsException(email = userRecord.email)
                phoneExists -> throw UserExistsException(phone = userRecord.phoneNumber)
                e.message?.contains("users_phone_unique") == true -> {
                    auth.deleteUser(userRecord.uid)
                    throw Exception("An unexpected error has occurred, due to user info conflict please ask for help from super admin.")
                }
                else -> throw e
            }
        }
    }

    private fun ResultSet.toUser(): User {
        return User(
            uid = getString("uid"),
            email = getString("email"),
            displayName = getString("display_name"),
            photoUrl = getString("photo_url"),
            phone = getString("phone"),
            usertype = Usertype.valueOf(getObject("usertype").toString()),
            createdAt = getTimestamp("created_at").asString,
            updatedAt = getTimestamp("updated_at")?.asString,
            lastSeenAt = getTimestamp("last_seen_at")?.asString,
        )
    }

    override suspend fun readUser(uid: String): User? = dbQuery(conn) {
        executeQuery("select * from auth.users where uid = '$uid'") { it.toUser() }
            ?.singleOrNull()
    }

    override suspend fun readUsers(limit: Int): List<User> = dbQuery(conn) {
        executeQuery("select * from auth.users limit $limit") { it.toUser() } ?: listOf()
    }

    override suspend fun readFirebaseUser(uid: String): UserRecord? = dbQuery(conn) {
        try {
            auth.getUserAsync(uid).get()
        } catch (e: FirebaseAuthException) {
            null
        }
    }

    override suspend fun updateUser(uid: String, request: UpdateUser, admin: Boolean): User = dbQuery(conn) {
        val updateRequest: UserRecord.UpdateRequest = UserRecord.UpdateRequest(uid)
        request.email?.let { updateRequest.setEmail(it) }
        request.password?.let { updateRequest.setPassword(it) }
        request.displayName?.let { updateRequest.setDisplayName(it) }
        request.photoUrl?.let { updateRequest.setPhotoUrl(it) }
        request.phone?.let { it: String -> updateRequest.setPhoneNumber(it) }

        val record = auth.updateUserAsync(updateRequest).get()
//        val firebaseUser = readFirebaseUser(uid)


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

    override suspend fun createAdminCode(request: NewAdminCode): Unit = dbQuery(conn) {
        executeUpdate("""
            insert into auth.admins_codes(email, hash_code) 
            values ('${request.email}', '${PasswordEncryptor.encryptPassword(request.code)}')
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

    override suspend fun updateLastSeen(uid: String): Unit = dbQuery(conn) {
        executeUpdate("""
            update auth.users
            set last_seen_at = (now() at time zone 'SAST')
            where uid = '$uid'
        """.trimIndent())
    }
}