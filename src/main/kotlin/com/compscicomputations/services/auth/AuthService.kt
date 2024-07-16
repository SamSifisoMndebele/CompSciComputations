package com.compscicomputations.services.auth

import com.compscicomputations.services.auth.impl.AuthServiceImpl.Companion.UserExistsException
import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.User

internal interface AuthService {
    /**
     * Get user unique identifier given the user email.
     * @param email the user email address.
     * @return `id` the user identifier or `null` if it does not exist.
     */
    suspend fun userIdByEmail(email: String): String?

    /**
     * Create a user on the database.
     * @param user [NewUser] the information about the user.
     * @throws UserExistsException if the user email exists.
     */
    suspend fun createUser(user: NewUser)

    /**
     * Reads the user information from the database.
     * @param id the user unique identifier.
     * @return [User] the database user information otherwise `null` if it doesn't exist.
     */
    suspend fun readUser(id: String): User?

    /**
     * Reads the user information from the database.
     * @return [User] list from a database, ordered by email.
     */
    suspend fun readUsers(limit: Int = 100): List<User>

    /**
     * Updates the user information on the database.
     * @param id the user unique identifier.
     * @param updateUser [UpdateUser] the user information to be updated,
     * if a field value is null it remains unchanged.
     * @return [User] the database user information.
     */
    suspend fun updateUser(id: String, user: UpdateUser): User

    /**
     * Create a admin user verification code.
     * @param adminPin [NewAdminPin]
     */
    suspend fun createAdminPin(adminPin: NewAdminPin)

    /**
     * Checks if admin code is valid.
     * @param email admin email
     * @param pin admin verification pin
     * @return the row number of the admin pin.
     * @throws Exception if the pin not valid or does not exist.
     */
    suspend fun validateAdminPin(email: String, pin: String): Int

    /**
     * Set the user as an admin or not.
     * @param id the user unique identifier.
     * @param isAdmin `true` if admin `false` otherwise, `null` to keep original value.
     * @param adminCode the admin verification code.
     * @param admin if admin makes the request set true.
     */
    suspend fun updateUserIsAdmin(id: String, email: String,
                                  isAdmin: Boolean? = null,
                                  adminCode: String? = null,
                                  admin: Boolean = false)

    /**
     * Deletes the user on database.
     * @param id the user unique identifier.
     */
    suspend fun deleteUser(id: String)
}