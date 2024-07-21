package com.compscicomputations.services.auth.impl

import com.compscicomputations.plugins.connectToPostgres
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.RegisterUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.utils.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.ResultSet


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
                photoUrl = getString("photo_url"),
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
            executeQuerySingle("""
do
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
    end;
$$
            """.trimMargin(), { getUser() }
            ) {
                setString(1, user.email)
                setString(2, user.password)
                setString(3, user.displayName)
                setString(4, "file-storage/users/${user.email}/images")
            }
        } catch (e: Exception) {
            throw e;
        }
    }

    override suspend fun createUser(googleToken: GoogleToken): User {
        val response = googleToken.photoUrl?.let { url -> client.get(url) }
        val bytes = when(response?.status) {
            null -> null
            HttpStatusCode.ExpectationFailed -> {
                logger.warn(response.bodyAsText())
                null
            }
            HttpStatusCode.OK -> response.body<ByteArray>()
            else -> null
        }
        return createUser(RegisterUser(
            email = googleToken.email,
            password = null,
            displayName = googleToken.displayName,
            image = bytes
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