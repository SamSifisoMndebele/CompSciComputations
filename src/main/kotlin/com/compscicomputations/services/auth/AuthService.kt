package com.compscicomputations.services.auth

import com.compscicomputations.services.auth.impl.AuthServiceImpl
import com.compscicomputations.services.auth.impl.AuthServiceImpl.Companion.UserExistsException
import com.compscicomputations.services.auth.models.requests.NewAdminCode
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.User
import com.google.firebase.auth.UserRecord

internal interface AuthService {
    /**
     * Get user unique identifier given the user email.
     * @param email the user email address.
     * @return `uid` the user unique identifier or `null` if does not exists.
     */
    fun getUserUidByEmail(email: String): String?
    /**
     * Get user unique identifier given the user phone number.
     * @param phone the user phone number.
     * @return `uid` the user unique identifier or `null` if does not exists.
     */
    fun getUserUidByPhone(phone: String): String?

    /**
     * Create a firebase user and additional information on the database.
     * @param request [NewUser] the information about the user.
     * @throws UserExistsException if the user email or phone number exists.
     */
    suspend fun createUser(newUser: NewUser)

    /**
     * Reads the user information from the database.
     * @param uid the user unique identifier.
     * @return [User] the database user information otherwise `null` if it doesn't exist.
     */
    suspend fun readUser(uid: String): User?

    /**
     * Reads the user information from the database.
     * @return [User] list from a database.
     */
    suspend fun readUsers(limit: Int = 10): List<User>

    /**
     * Reads the user information from firebase auth.
     * @param uid the user unique identifier.
     * @return [UserRecord] firebase user information otherwise `null` if it doesn't exist.
     */
    suspend fun readFirebaseUser(uid: String): UserRecord?

    /**
     * Updates the user information on the database and firebase auth.
     * @param uid the user unique identifier.
     * @param request [UpdateUser] the user information to be updated,
     * if a field value is null it remains unchanged.
     * @param admin if admin makes the request set true.
     * @return [User] the database user information.
     */
    suspend fun updateUser(uid: String, request: UpdateUser, admin: Boolean = false): User

    /**
     * Create a admin user verification code.
     * @param request [NewAdminCode] the admin code info.
     */
    suspend fun createAdminCode(request: NewAdminCode)

    /**
     * Set the user as an admin or not.
     * @param uid the user unique identifier.
     * @param isAdmin `true` if admin `false` otherwise, `null` to keep original value.
     * @param adminCode the admin verification code.
     * @param admin if admin makes the request set true.
     */
    suspend fun updateUserIsAdmin(uid: String, email: String,
                                  isAdmin: Boolean? = null,
                                  adminCode: String? = null,
                                  admin: Boolean = false)

    /**
     * Deletes the firebase user on firebase only, and set user as deleted by an admin on database.
     * @param uid the user unique identifier.
     */
    suspend fun deleteUser(uid: String)

    suspend fun updateLastSeen(uid: String)
}