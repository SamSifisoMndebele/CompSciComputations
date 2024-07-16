package com.compscicomputations.services.auth.impl

import com.compscicomputations.plugins.connectToPostgres
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.utils.*
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.ResultSet


internal class AuthServiceImpl : AuthService {
    private val logger = LoggerFactory.getLogger("AuthService")
    private val conn: Connection by lazy { connectToPostgres() }

    companion object {
        internal class UserExistsException(val email: String? = null) : Exception("The user${
            if (email != null) " with the provided email: $email" else "" } already exists")
    }

    override suspend fun userIdByEmail(email: String): String? = dbQuery(conn) {
        executeQuery("""
            select id from auth.users where email like '$email'
        """.trimIndent()) {
            it.getString("")
        }?.singleOrNull()
    }

    override suspend fun createUser(user: NewUser): Unit = dbQuery(conn) {
        //Insert the user values to the database
        try {
            if (user.isAdmin) {
                executeUpdate("call auth.insert_admin(?, ?, ?, ?, ?, ?, ?, ?);") {
                    setString(1, user.email)
                    setString(2, user.adminPin)
                    setString(3, user.names)
                    setString(4, user.lastName)
                    setString(5, user.password)
                    setString(6, user.photoUrl)
                    setString(7, user.phone)
                    setBoolean(8, user.isStudent)
                }
            } else {
                executeUpdate("call auth.insert_user(?, ?, ?, ?, ?, ?, ?, ?);") {
                    setString(1, user.email)
                    setString(2, user.names)
                    setString(3, user.lastName)
                    setString(4, user.password)
                    setString(5, user.photoUrl)
                    setString(6, user.phone)
                    setBoolean(7, false)
                    setBoolean(8, user.isStudent)
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

    private fun ResultSet.toUser(): User {
        return User(
            id = getString("id"),
            email = getString("email"),
            names = getString("names"),
            lastName = getString("last_name"),
            passwordHash = getString("password_hash"),
            photoUrl = getString("photo_url"),
            phone = getString("phone"),
            isAdmin = getBoolean("is_admin"),
            isStudent = getBoolean("is_student"),
            createdAt = getTimestamp("created_at").asString,
            updatedAt = getTimestamp("updated_at")?.asString
        )
    }

    override suspend fun readUser(id: String): User? = dbQuery(conn) {
        executeQuery("select * from auth.users where id = ?",
            args = {
                setString(1, id)
            }
        ) {
            it.toUser()
        }?.singleOrNull()
    }

    override suspend fun readUsers(limit: Int): List<User> = dbQuery(conn) {
        executeQuery("""
            select * from auth.users 
            order by users.email limit $limit
            """.trimIndent()
        ) {
            it.toUser()
        } ?: listOf()
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
        try {
            executeQuery("select auth.validate_admin_pin(?, ?)",
                args = {
                    setString(1, email)
                    setString(2, pin)
                }
            ) {
                it.getInt(1)
            }!!.single()
        } catch (e: Exception) {
            logger.warn(e.message)
            throw e
        }
    }

    override suspend fun updateUserIsAdmin(id: String, email: String, isAdmin: Boolean?, adminCode: String?, admin: Boolean): Unit = dbQuery(conn) {
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

    override suspend fun deleteUser(id: String): Unit = dbQuery(conn) {
        executeUpdate("delete from auth.users where id like ?") {
            setString(1, id)
        }
    }
}