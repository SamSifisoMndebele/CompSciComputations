package com.compscicomputations.core.ktor_client.remote.repo

import com.compscicomputations.core.ktor_client.model.User
import com.compscicomputations.core.ktor_client.model.Usertype
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import java.io.File
import java.util.Date

interface UserRepo {
    companion object {
        const val USER_UID = "DEFAULT_USER_UID"
        val USER_IMAGE = File(USER_UID)
    }

    /**
     * Get the user from database
     * @throws RestException or one of its subclasses if receiving an error response
     * @throws HttpRequestTimeoutException if the request timed out
     * @throws HttpRequestException on network related issues
     */
    suspend fun getUser(uid: String = USER_UID): User?

    /**
     * Insert a user to the database
     * @throws RestException or one of its subclasses if receiving an error response
     * @throws HttpRequestTimeoutException if the request timed out
     * @throws HttpRequestException on network related issues
     */
    suspend fun insertUser(
        uid: String = USER_UID,
        displayName: String,
        email: String,
        phone: String? = null,
        photoUrl: String? = null,
    )


    /**
     * Insert an admin user to the database
     * @throws RestException or one of its subclasses if receiving an error response
     * @throws HttpRequestTimeoutException if the request timed out
     * @throws HttpRequestException on network related issues
     */
//    suspend fun insertAdminUser(
//        uid: String = CURRENT_USER_UID,
//        displayName: String,
//        email: String,
//        phone: String? = null,
//        photoUrl: String? = null,
//        role: String,
//        code: String
//    )

    /**
     * Insert a student user to the database
     * @throws RestException or one of its subclasses if receiving an error response
     * @throws HttpRequestTimeoutException if the request timed out
     * @throws HttpRequestException on network related issues
     */
//    suspend fun insertStudentUser(
//        uid: String = CURRENT_USER_UID,
//        displayName: String,
//        email: String,
//        phone: String? = null,
//        photoUrl: String? = null,
//        course: String,
//        school: String
//    )

    /**
     * Delete user from the database
     * @throws RestException or one of its subclasses if receiving an error response
     * @throws HttpRequestTimeoutException if the request timed out
     * @throws HttpRequestException on network related issues
     */
    suspend fun deleteUser(uid: String = USER_UID)

    suspend fun updateUser(
        uid: String = USER_UID,
        displayName: String,
        imageFile: File? = USER_IMAGE,
        userType: Usertype,
        lastSeenAt: Date
    )
    suspend fun updateUserDisplayName(uid: String = USER_UID, displayName: String)
    suspend fun updateUserImage(uid: String = USER_UID, imageFile: File?)
    suspend fun updateUserUserType(uid: String = USER_UID, userType: Usertype)
    suspend fun updateUserLastSeen(uid: String = USER_UID)
}