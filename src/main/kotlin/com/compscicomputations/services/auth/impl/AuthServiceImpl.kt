package com.compscicomputations.services.auth.impl

import com.compscicomputations.plugins.connectToPostgres
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.Users
import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.RegisterUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
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
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLType
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

    companion object {
        private val logger = LoggerFactory.getLogger("AuthService")

//        internal class UserExistsException(val email: String? = null) : Exception("The user${
//            if (email != null) " with the provided email: $email" else "" } already exists")

        private fun ResultSet.getUser(): User {
            return User(
                id = getObject("id").toString(),
                email = getString("email"),
                displayName = getString("display_name"),
                photoId = getInt("photo_id"),
                phone = getString("phone"),
                isAdmin = getBoolean("is_admin"),
                isStudent = getBoolean("is_student"),
                isEmailVerified = getBoolean("is_email_verified"),
            )
        }
    }

    override suspend fun userIdByEmail(email: String): String? = dbQuery(conn) {
        executeQuerySingleOrNull("select id from auth.users where email like '$email';", { getString("id") })
    }

    override suspend fun createUser(user: RegisterUser): User = dbQuery(conn) {
        try {
            executeQuerySingle(
                """
/*do
$$
    declare
        _email text := ?;
        rec record;
    begin
        insert into auth.users (email, password_hash, display_name)
        values (_email, ext.crypt(?, ext.gen_salt('md5')), ?)
        returning * into strict rec;

    exception
        when unique_violation then
            raise exception 'User with email: % already exists', _email
                using hint = 'Login to your account or reset your forgotten password.';
        when foreign_key_violation then
            raise exception 'User image does not exist on the database';
    end
$$*/
                insert into auth.users (email, password_hash, display_name, photo_id)
                values (?, ext.crypt(?, ext.gen_salt('md5')), ?, ?)
                returning *;
            """.trimMargin(), { getUser() }
            ) {
                setString(1, user.email)
                setString(2, user.password)
                setString(3, user.displayName)
                setObject(4, user.imageId, Types.INTEGER)
            }
        } catch (e: Exception) {
            throw e;
        }
    }

    override suspend fun createUser(googleToken: GoogleToken): User = dbQuery(conn)  {
        if (googleToken.photoUrl == null) {
            return@dbQuery createUser(RegisterUser(
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
                return@dbQuery createUser(RegisterUser(
                    email = googleToken.email,
                    password = null,
                    displayName = googleToken.displayName,
                    imageId = null
                ))
            }
        }
        val fileSize = response.headers[HttpHeaders.ContentLength]
        val fileId = saveAuthFile(AuthFile(
            name = googleToken.displayName ?: googleToken.email,
            description = "Google profile image.",
            data = bytes,
            size = fileSize.toString(),
        ))

        createUser(RegisterUser(
            email = googleToken.email,
            password = null,
            displayName = googleToken.displayName,
            imageId = fileId
        ))
    }

    private suspend fun saveAuthFile(authFile: AuthFile): Int = dbQuery(conn)  {
        try {
            executeQuerySingle("""
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
        } catch (e: Exception) {
            throw e;
        }
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

    override suspend fun readUser(id: String): User? = dbQuery(conn) {
        executeQuerySingleOrNull("select * from auth.users where id = ?::uuid", { getUser() }) {
            setString(1, id)
        }
    }

    override suspend fun readUserByEmail(email: String): User? = dbQuery(conn) {
        executeQuerySingleOrNull("select * from auth.users where email like ?", { getUser() }) {
            setString(1, email)
        }
    }

    override suspend fun readUsers(limit: Int): List<User> = dbQuery(conn) {
        executeQuery("select * from auth.users order by users.email limit $limit", { getUser() })
    }

    override suspend fun updateUser(id: String, user: UpdateUser): User = dbQuery(conn) {
        try {
            if (user.isAdmin == true) {
                executeQuerySingle("select * from auth.insert_admin(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", { getUser() }) {
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
                executeQuerySingle("select * from auth.insert_user(?, ?, ?, ?, ?, ?, ?, ?, ?);", { getUser() }) {
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
        executeUpdate("call auth.upsert_admin_pin(?, ?)") {
            setString(1, adminPin.email)
            setString(2, adminPin.pin)
        }
    }

    override suspend fun validateAdminPin(email: String, pin: String): Int = dbQuery(conn) {
        executeQuerySingle("select auth.validate_admin_pin(?, ?)", { getInt(1) }) {
            setString(1, email)
            setString(2, pin)
        }
    }

    override suspend fun deleteUser(id: String): Unit = dbQuery(conn) {
        executeUpdate("delete from auth.users where id like ?") {
            setString(1, id)
        }
    }

    override suspend fun validatePassword(email: String, password: String): User = dbQuery(conn) {
        executeQuerySingle("select * from auth.validate_password_n_get_user(?, ?)", { getUser() }){
            setString(1, email)
            setString(2, password)
        }
    }

    override suspend fun updatePassword(id: String, password: String): Unit = dbQuery(conn) {
        executeUpdate("""
            update auth.users 
            set password_hash = ext.crypt(?, ext.gen_salt('md5'))
            where id = ?::uuid
            """.trimMargin()
        ) {
            setString(1, password)
            setString(2, id)
        }
    }
}