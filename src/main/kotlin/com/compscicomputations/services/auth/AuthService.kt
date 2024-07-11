package com.compscicomputations.services.auth

import com.compscicomputations.firebase.FirebaseUser
import com.compscicomputations.services.auth.exceptions.NoSuchUserException
import com.compscicomputations.services.auth.models.*

internal interface AuthService {
    /**
     * Get user unique identifier given the user email.
     * @param email the user email address.
     * @return `uid` the user unique identifier or `null` if does not exists.
     */
    suspend fun getUserUidByEmail(email: String): String?
    /**
     * Get user unique identifier given the user phone number.
     * @param phone the user phone number.
     * @return `uid` the user unique identifier or `null` if does not exists.
     */
    suspend fun getUserUidByPhone(phone: String): String?

    /**
     * Create a firebase user and additional information on the database.
     * @param request [CreateUserRequest] the information about the user.
     * @param admin if admin makes the request set true.
     */
    suspend fun createUser(request: CreateUserRequest, admin: Boolean = false)

    /**
     * Reads the user information from the database.
     * @param uid the user unique identifier.
     * @return [User] the database user information.
     * @throws NoSuchUserException if user with `uid` does not exist.
     */
    suspend fun readUser(uid: String): User
    /**
     * Reads the user information from firebase auth.
     * @param uid the user unique identifier.
     * @return [FirebaseUser] firebase user information.
     * @throws NoSuchUserException if user with `uid` does not exist.
     */
    suspend fun readFirebaseUser(uid: String): FirebaseUser

    /**
     * Updates the user information on the database and firebase auth.
     * @param request [UpdateUserRequest] the user information to be updated,
     * if a field value is null it remains unchanged.
     * @param admin if admin makes the request set true.
     * @return [User] the database user information.
     */
    suspend fun updateUser(firebaseUser: FirebaseUser, request: UpdateUserRequest, admin: Boolean = false): User

    /**
     * Create a admin user verification code.
     * @param request [CreateAdminCodeRequest] the admin code info.
     */
    suspend fun createAdminCode(request: CreateAdminCodeRequest)

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
}