package com.compscicomputations.core.database.dao

import com.compscicomputations.core.database.UserType
import com.compscicomputations.core.database.model.User
import java.io.File
import java.util.Date

interface UserDao {
    companion object {
        const val CURRENT_USER_UID = "DEFAULT_USER_UID"
        val CURRENT_USER_IMAGE = File(CURRENT_USER_UID)
    }

    suspend fun getUser(uid: String = CURRENT_USER_UID): User?
    suspend fun getUsers(): List<User>

    suspend fun insertUser(
        uid: String = CURRENT_USER_UID,
        displayName: String,
        email: String,
        photoUrl: String?,
        userType: UserType,
        createdAt: Date,
        lastSeenAt: Date
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
    suspend fun updateUserLastSeen(uid: String = CURRENT_USER_UID, lastSeenAt: Date)
}