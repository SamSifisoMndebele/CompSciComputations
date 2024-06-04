package com.compscicomputations.core.database.remote.dao

import com.compscicomputations.core.database.remote.model.User
import com.compscicomputations.core.database.remote.model.UserType
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import java.io.File
import java.util.Date

interface UserDao {
    companion object {
        const val CURRENT_USER_UID = "DEFAULT_USER_UID"
        val CURRENT_USER_IMAGE = File(CURRENT_USER_UID)
    }

    /**
     * Get the user from database
     * @throws RestException or one of its subclasses if receiving an error response
     * @throws HttpRequestTimeoutException if the request timed out
     * @throws HttpRequestException on network related issues
     */
    suspend fun getUser(uid: String = CURRENT_USER_UID): User?

    /**
     * Insert a user to the database
     * @throws RestException or one of its subclasses if receiving an error response
     * @throws HttpRequestTimeoutException if the request timed out
     * @throws HttpRequestException on network related issues
     */
    suspend fun insertUser(
        uid: String = CURRENT_USER_UID,
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
    suspend fun insertAdminUser(
        uid: String = CURRENT_USER_UID,
        displayName: String,
        email: String,
        phone: String? = null,
        photoUrl: String? = null,
        role: String,
        code: String
    )

    /**
     * Insert a student user to the database
     * @throws RestException or one of its subclasses if receiving an error response
     * @throws HttpRequestTimeoutException if the request timed out
     * @throws HttpRequestException on network related issues
     */
    suspend fun insertStudentUser(
        uid: String = CURRENT_USER_UID,
        displayName: String,
        email: String,
        phone: String? = null,
        photoUrl: String? = null,
        course: String,
        school: String
    )

    suspend fun deleteUser(uid: String = CURRENT_USER_UID)

    suspend fun updateUser(
        uid: String = CURRENT_USER_UID,
        displayName: String,
        imageFile: File? = CURRENT_USER_IMAGE,
        userType: UserType,
        lastSeenAt: Date
    )
    suspend fun updateUserDisplayName(uid: String = CURRENT_USER_UID, displayName: String)
    suspend fun updateUserImage(uid: String = CURRENT_USER_UID, imageFile: File?)
    suspend fun updateUserUserType(uid: String = CURRENT_USER_UID, userType: UserType)
    suspend fun updateUserLastSignIn(uid: String = CURRENT_USER_UID)
    suspend fun updateUserLastSeen(uid: String = CURRENT_USER_UID)
}