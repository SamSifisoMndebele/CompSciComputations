package com.compscicomputations.services.auth

import com.compscicomputations.plugins.databaseConnection
import com.compscicomputations.services._contrast.AuthServiceContrast
import com.compscicomputations.services.auth.models.OTP
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.ResetPassword
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.utils.*
import com.compscicomputations.utils.Image.Companion.asImage
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import org.apache.http.auth.InvalidCredentialsException
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory
import java.sql.ResultSet
import java.sql.Types


internal class AuthService : AuthServiceContrast {
    private var conn = databaseConnection()

    private val client by lazy {
        HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }

    private val googleVerifier by lazy { GoogleVerifier() }

    companion object {
        private val logger = LoggerFactory.getLogger("AuthService")

        private fun ResultSet.getUser(): User = User(
            id = getObject("id").toString(),
            email = getString("email"),
            names = getString("names"),
            lastname = getString("lastname"),
            image = getBytes("image").asImage,
            phone = getString("phone"),

            isEmailVerified = getBoolean("is_email_verified"),
            isStudent = getBoolean("is_student"),
            university = getString("university"),
            school = getString("school"),
            course = getString("course"),
            isAdmin = getBoolean("is_admin"),
            isSuperAdmin = getBoolean("is_super_admin")

        )
    }

    override suspend fun registerUser(newUser: NewUser): User = dbQuery(conn) {
        querySingle("select * from auth.insert_user(?, ?, ?, ?, ?, ?, ?)", { getUser() }) {
            setString(1, newUser.email.trim())
            setString(2, newUser.names.trim())
            setString(3, newUser.lastname.trim())
            setBytes(4, newUser.image?.bytes)
            setBoolean(5, true)
            setString(6, newUser.password)
            setString(7, newUser.otp)
        }
    }

    override suspend fun googleUser(idTokenString: String): User = dbQuery(conn) {
        val googleToken = googleVerifier.authenticate(idTokenString)
            ?: throw InvalidCredentialsException("Invalid google token.")

        try {
            querySingle("select * from auth.select_user(_email := ?)", { getUser() }) {
                setString(1, googleToken.email.trim())
            }
        } catch (e: PSQLException) {
            if (e.serverErrorMessage?.hint != "Register a new account.") throw e
            logger.warn(e.serverErrorMessage.toString())
            logger.warn("Goggle user does not exists, creating user...")
            googleToken.photoUrl?.let { url -> client.get(url) }
                ?.let { response ->
                    if (response.status == HttpStatusCode.OK) response.body<ByteArray>()
                    else {
                        logger.warn(response.bodyAsText())
                        null
                    }
                }.let { bytes ->
                    querySingle("select * from auth.insert_user(?, ?, ?, ?, ?)", { getUser() }) {
                        setString(1, googleToken.email.trim())
                        setString(2, googleToken.names.trim())
                        setString(3, googleToken.lastname.trim())
                        setBytes(4, bytes)
                        setBoolean(5, googleToken.emailVerified)
                    }
                }
        }
    }

    override suspend fun readUser(email: String, password: String): User = dbQuery(conn) {
        querySingle("select * from auth.get_user(?, ?)", { getUser() }){
            setString(1, email.trim())
            setString(2, password)
        }
    }

    override suspend fun readUsers(limit: Int): List<User> = dbQuery(conn) {
        query("select * from auth.users order by users.display_name limit $limit", { getUser() })
    }

    override suspend fun getOTP(email: String, isUser: Boolean?): OTP = dbQuery(conn) {
        querySingle("select * from auth.create_otp(?, ?)",
            {
                OTP(
                    getInt("id"),
                    getString("email"),
                    getString("otp"),
                    getTimestamp("valid_until").toString().substring(11, 19),
                )
            }
        ) {
            setString(1, email.trim())
            setObject(2, isUser, Types.BOOLEAN)
        }
    }

    override suspend fun passwordReset(resetPassword: ResetPassword): Unit = dbQuery(conn) {
        when {
            !resetPassword.otp.isNullOrBlank() -> update("call auth.reset_password_otp(?, ?, ?)") {
                setString(1, resetPassword.email.trim())
                setString(2, resetPassword.newPassword)
                setString(3, resetPassword.otp)
            }
            !resetPassword.password.isNullOrBlank() -> update("call auth.reset_password(?, ?, ?)") {
                setString(1, resetPassword.email.trim())
                setString(2, resetPassword.newPassword)
                setString(3, resetPassword.password)
            }
            else -> throw IllegalArgumentException("OTP and old password are null or empty.")
        }
    }

    override suspend fun deleteUser(email: String): Unit = dbQuery(conn) {
        update("delete from auth.users where email like ?") {
            setString(1, email.trim())
        }
    }

    override suspend fun updateUser(user: UpdateUser): User = dbQuery(conn) {
        querySingle("select * from auth.update_user(?::uuid, ?, ?, ?, ?, ?, ?, ?, ?, ?)", { getUser() }) {
            setString(1, user.id)
            setString(2, user.names.trim())
            setString(3, user.lastname.trim())
            setBytes(4, user.image?.bytes)
            setString(5, user.phone?.trim())
            setBoolean(6, user.isEmailVerified)
            setBoolean(7, user.isStudent)
            setString(8, user.university?.trim())
            setString(9, user.school?.trim())
            setString(10, user.course?.trim())
        }
    }

//    override suspend fun uploadFile(multipartData: MultiPartData, fileSize: String): Unit = dbQuery(conn)  {
//        var fileName = ""
//        var fileDescription = ""
//        var fileBytes = byteArrayOf()
//        multipartData.forEachPart { part ->
//            when (part) {
//                is PartData.FormItem -> {
//                    fileDescription = part.value
//                }
//                is PartData.FileItem -> {
//                    fileName = part.originalFileName as String
//                    fileBytes = part.streamProvider().readBytes()
////                        val encoded = Base64.getEncoder().encodeToString(fileBytes)
//                }
//                is PartData.BinaryChannelItem -> {}
//                is PartData.BinaryItem -> {}
//            }
//            part.dispose()
//        }
//
//        saveAuthFile(StorageFile(
//            name = fileName,
//            description = fileDescription,
//            data = fileBytes,
//            size = fileSize,
//        ))
//    }

//    override suspend fun downloadFile(id: Int): ByteArray = dbQuery(conn) {
//        querySingle("""
//                select data from auth.files
//                where id = ?
//            """.trimMargin(), { getBytes("data") }
//        ) {
//            setInt(1, id)
//        }
//    }


    /*override suspend fun updateUser(id: String, user: UpdateUser): User = dbQuery(conn) {
        try {
            if (user.isAdmin == true) {
                querySingle("select * from auth.insert_admin(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", { getUser() }) {
                    setString(1, user.email)
                    setString(2, user.password)
                    setString(3, user.adminPin)
                    setString(4, user.displayName)
                    setString(5, user.lastname)
                    setString(6, user.photoUrl)
                    setString(7, user.phone)
                    setBoolean(8, user.isStudent ?: false)
                    setString(9, user.course)
                    setString(10, user.school)
                }
            } else {
                querySingle("select * from auth.(?, ?, ?, ?, ?, ?, ?, ?, ?);", { getUser() }) {
                    setString(1, user.email)
                    setString(2, user.password)
                    setString(3, user.displayName)
                    setString(4, user.lastname)
                    setString(5, user.photoUrl)
                    setString(6, user.phone)
                    setBoolean(7, user.isStudent ?: false)
                    setString(8, user.course)
                    setString(9, user.school)
                }
            }
        } catch (e: Exception) {
            throw e
//            when {
//                e.message?.contains("users_email_key") == true -> {
//                    throw UserExistsException(user.email)
//                }
//                else -> throw e
//            }
        }
    }

    override suspend fun createAdminPin(adminPin: NewAdminPin): Unit = dbQuery(conn) {
        update("call auth.upsert_admin_pin(?, ?)") {
            setString(1, adminPin.email)
            setString(2, adminPin.pin)
        }
    }

    override suspend fun validateAdminPin(email: String, pin: String): Int = dbQuery(conn) {
        querySingle("select auth.validate_admin_pin(?, ?)", { getInt(1) }) {
            setString(1, email)
            setString(2, pin)
        }
    }

    */
}