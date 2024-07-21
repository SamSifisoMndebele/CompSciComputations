package com.compscicomputations.services.auth.impl

import com.compscicomputations.plugins.connectToPostgres
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.RegisterUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.utils.*
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

    companion object {
        private val logger = LoggerFactory.getLogger("AuthService")

        internal class UserExistsException(val email: String? = null) : Exception("The user${
            if (email != null) " with the provided email: $email" else "" } already exists")

        private fun ResultSet.getUser(): User {
            return User(
                id = getObject("id").toString(),
                email = getString("email"),
                names = getString("names"),
                photoUrl = getString("photo_url"),
                phone = getString("phone"),
                isAdmin = getBoolean("is_admin"),
                isStudent = getBoolean("is_student"),
                createdAt = getTimestamp("created_at").asString,
                updatedAt = getTimestamp("updated_at")?.asString
            )
        }
    }

    override suspend fun userIdByEmail(email: String): String? = dbQuery(conn) {
        executeQuerySingleOrNull("select id from auth.users where email like '$email';", { getString("id") })
    }

    override suspend fun createUser(user: RegisterUser): User = dbQuery(conn) {
        //Insert the user values to the database
        try {
            if (user.isAdmin) {
                executeQuerySingle("select * from auth.insert_admin(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", { getUser() }) {
                    setString(1, user.email)
                    setString(2, user.password)
                    setString(3, user.adminPin)
                    setString(4, user.names)
                    setString(5, user.lastname)
                    setString(6, user.photoUrl)
                    setString(7, user.phone)
                    setBoolean(8, user.isStudent)
                    setString(9, user.course)
                    setString(10, user.school)
                }
            } else {
                executeQuerySingle("select * from auth.insert_user(?, ?, ?, ?, ?, ?, ?, ?, ?);", { getUser() }) {
                    setString(1, user.email)
                    setString(2, user.password)
                    setString(3, user.names)
                    setString(4, user.lastname)
                    setString(5, user.photoUrl)
                    setString(6, user.phone)
                    setBoolean(7, user.isStudent)
                    setString(8, user.course)
                    setString(9, user.school)
                }
            }
        } catch (e: Exception) {
            when {
                e.message?.contains("users_email_key") == true -> {
                    throw UserExistsException(user.email)
                }
                else -> throw e
            }
        }
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
        TODO("Not yet implemented.")
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