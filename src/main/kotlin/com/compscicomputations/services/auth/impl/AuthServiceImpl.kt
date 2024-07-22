package com.compscicomputations.services.auth.impl

import com.compscicomputations.plugins.connectToPostgres
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.requests.RegisterUser
import com.compscicomputations.services.auth.models.response.AuthFile
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.utils.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import org.apache.http.auth.InvalidCredentialsException
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Types


internal class AuthServiceImpl : AuthService {
    private var connection: Connection? = null
    private val conn : Connection
        get() {
            if (connection == null || connection!!.isClosed) {
                connection = connectToPostgres()
            }
            return connection!!
        }

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
            displayName = getString("display_name"),
            imageId = getInt("image_id"),
            phone = getString("phone"),
            isAdmin = getBoolean("is_admin"),
            isStudent = getBoolean("is_student"),
            isEmailVerified = getBoolean("is_email_verified"),
        )

        private fun Connection.saveAuthFile(authFile: AuthFile): Int =
            querySingle("""
                insert into auth.files (name, description, data, size)
                values (?, ?, ?, ?)
                returning id;
            """.trimMargin(), { getInt("id") }
        ) {
            setString(1, authFile.name)
            setString(2, authFile.description)
            setBytes(3, authFile.data)
            setString(4, authFile.size)
        }

//        private fun ResultSet.getAuthFile(): AuthFile = AuthFile(
//            name = getString("name"),
//            description = getString("description"),
//            data = getBytes("data"),
//            size = getString("size"),
//            createdAt = getString("created_at"),
//        )
    }

    override suspend fun uploadFile(multipartData: MultiPartData, fileSize: String): Int = dbQuery(conn)  {
        var fileName = ""
        var fileDescription = ""
        var fileBytes = byteArrayOf()
        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    fileDescription = part.value
                }
                is PartData.FileItem -> {
                    fileName = part.originalFileName as String
                    fileBytes = part.streamProvider().readBytes()
//                        val encoded = Base64.getEncoder().encodeToString(fileBytes)
                }
                is PartData.BinaryChannelItem -> {}
                is PartData.BinaryItem -> {}
            }
            part.dispose()
        }

        saveAuthFile(AuthFile(
            name = fileName,
            description = fileDescription,
            data = fileBytes,
            size = fileSize,
        ))
    }

    override suspend fun downloadFile(id: Int): ByteArray = dbQuery(conn) {
        querySingle("""
                select data from auth.files
                where id = ?
            """.trimMargin(), { getBytes("data") }
        ) {
            setInt(1, id)
        }
    }

    override suspend fun registerUser(registerUser: RegisterUser): User = dbQuery(conn) {
        querySingle("select * from auth.insert_user(?,?,?,?)", { getUser() }) {
            setString(1, registerUser.email)
            setString(2, registerUser.password)
            setString(3, registerUser.displayName)
            setObject(4, registerUser.imageId, Types.INTEGER)
        }
    }

    override suspend fun updateUserImage(id: String, imageId: Int): Unit = dbQuery(conn) {
        update("update auth.users set image_id = ? where id = ?::uuid") {
            setInt(1, imageId)
            setString(2, id)
        }
    }

    override suspend fun googleUser(idTokenString: String): User = dbQuery(conn) {
        val googleToken = googleVerifier.authenticate(idTokenString)
            ?: throw InvalidCredentialsException("Invalid google token.")

        querySingleOrNull("select * from auth.users where email like ?", { getUser() }) {
            setString(1, googleToken.email)
        } ?: let {
            logger.warn("Goggle user does not exists, creating user...")
            if (googleToken.photoUrl == null) {
                return@dbQuery registerUser(RegisterUser(
                    email = googleToken.email,
                    password = null,
                    displayName = googleToken.displayName,
                    imageId = null
                ))
            }
            val response = client.get(googleToken.photoUrl)
            val bytes = when {
                response.status == HttpStatusCode.OK -> response.body<ByteArray>()
                else -> {
                    logger.warn(response.bodyAsText())
                    return@dbQuery registerUser(RegisterUser(
                        email = googleToken.email,
                        password = null,
                        displayName = googleToken.displayName,
                        imageId = null
                    ))
                }
            }
            val fileSize = response.headers[HttpHeaders.ContentLength]
            val fileId = saveAuthFile(AuthFile(
                name = googleToken.email,
                description = "Google profile image.",
                data = bytes,
                size = fileSize.toString()
            ))

            querySingle("select * from auth.insert_user(?,?,?,?)", { getUser() }) {
                setString(1, googleToken.email)
                setString(2, null)
                setString(3, googleToken.displayName)
                setObject(4, fileId, Types.INTEGER)
            }
        }
    }

    override suspend fun readUser(email: String, password: String): User = dbQuery(conn) {
        querySingle("select * from auth.validate_password_n_get_user(?, ?)", { getUser() }){
            setString(1, email)
            setString(2, password)
        }
    }

    override suspend fun readUsers(limit: Int): List<User> = dbQuery(conn) {
        query("select * from auth.users order by users.display_name limit $limit", { getUser() })
    }











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
                querySingle("select * from auth.insert_user(?, ?, ?, ?, ?, ?, ?, ?, ?);", { getUser() }) {
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

    override suspend fun deleteUser(id: String): Unit = dbQuery(conn) {
        update("delete from auth.users where id like ?") {
            setString(1, id)
        }
    }

    override suspend fun updatePassword(id: String, password: String): Unit = dbQuery(conn) {
        update("""
            update auth.users 
            set password_hash = ext.crypt(?, ext.gen_salt('md5'))
            where id = ?::uuid
            """.trimMargin()
        ) {
            setString(1, password)
            setString(2, id)
        }
    }*/
}