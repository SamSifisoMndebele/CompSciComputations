package com.compscicomputations.services._contrast

import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.RegisterUser
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
     * @param registerUser [RegisterUser] the information about the user.
     * @return [User] the created user record.
     */
    suspend fun registerUser(registerUser: RegisterUser): User

    /**
     * Update user image on database from multipartDara.
     * @param id the user unique identifier.
     * @param multipartData MultiPartData containing the image bytes.
     */
    suspend fun updateUserImage(id: Int, multipartData: MultiPartData)

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
    *//**
     * Updates the user information on the database.
     * @param id the user unique identifier.
     * @param updateUser [UpdateUser] the user information to be updated,
     * if a field value is null it remains unchanged.
     * @return [User] the database user information.
     *//*
    suspend fun updateUser(id: Int, user: UpdateUser): User

    *//**
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

    *//**
     * Deletes the user on database.
     * @param id the user unique identifier.
     *//*
    suspend fun deleteUser(id: String)

    *//**
     * Update user password with email
     * @param id user unique identifier
     * @param password user raw password
     *//*
    suspend fun updatePassword(id: Int, password: String)*/
}