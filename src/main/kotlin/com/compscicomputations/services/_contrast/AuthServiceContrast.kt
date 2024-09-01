package com.compscicomputations.services._contrast

import com.compscicomputations.services.auth.models.OTP
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.ResetPassword
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.User
import io.ktor.http.content.*

interface AuthServiceContrast {

//    /**
//     * Save a file from multipartDara to the database.
//     * @param multipartData MultiPartData containing the file info.
//     * @return the uploaded file id.
//     */
//    suspend fun uploadFile(multipartData: MultiPartData, fileSize: String)

//    /**
//     * Get a file from the database.
//     * @param id the file unique identifier
//     * @return [ByteArray] the database file bytes.
//     */
//    suspend fun downloadFile(id: Int): ByteArray

    /**
     * Create a user to the database.
     * @param newUser [NewUser] the information about the user.
     * @return [User] the created user record.
     */
    suspend fun registerUser(newUser: NewUser): User

    /**
     * Validate the Google id token string and, reads the user information from the database.
     * @param idTokenString the user email.
     * @return [User] the database user record.
     * @throws Exception if the operation wasn't successful.
     */
    suspend fun googleUser(idTokenString: String): User

    /**
     * Validate the email and password combination and, reads the user information from the database.
     * @param email user email address
     * @param password user raw password
     * @return [User] the database user record.
     * @throws Exception if the operation wasn't successful.
     */
    suspend fun readUser(email: String, password: String): User

    /**
     * Reads the user information from the database.
     * @return [User] list from a database, ordered by email.
     */
    suspend fun readUsers(limit: Int): List<User>

    /**
     * Get a new reset password OTP
     * @param email user email to reset password.
     * @param isUser true is the email is for existing user.
     * @return [OTP] the raw otp to be sent by email.
     */
    suspend fun getOTP(email: String, isUser: Boolean? = null): OTP

    /**
     * Reset the user password using OTP or old password.
     * @param newPassword [NewPassword] the user new password values from a request.
     */
    suspend fun passwordReset(resetPassword: ResetPassword)

    /**
    * Deletes the user on a database.
    * @param email the user unique email.
    */
    suspend fun deleteUser(email: String)


//    /**
//     * Validate an email and password combination for a user account
//     * @param email user email address
//     * @param password user raw password
//     * @param adminPin admin raw PIN
//     * @return [User] the user record.
//     * @throws Exception if the operation wasn't successful.
//     */
//    suspend fun readAdminUser(email: String, password: String, adminPin: String): User
/*
    */
    /**
     * Updates the user information on the database.
     * @param user [UpdateUser] the updated user information.
     * @return [User] the database new user information.
     */
    suspend fun updateUser(user: UpdateUser): User

    /**
     * Create a admin user verification code.
     * @param adminPin [NewAdminPin]
     *//*
    suspend fun createAdminPin(adminPin: NewAdminPin)

    *//**
     * Checks if admin code is valid.
     * @param email admin email
     * @param pin admin verification pin
     * @return the row number of the admin pin.
     * @throws Exception if the pin not valid or does not exist.
     *//*
    suspend fun validateAdminPin(email: String, pin: String): Int


    */
}