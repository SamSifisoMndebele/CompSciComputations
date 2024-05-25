package com.compscicomputations.data.repository

import com.compscicomputations.data.Task
import com.compscicomputations.data.model.User
import java.io.File
import java.util.Date

interface UserRepository {
    fun createUser(
        uid: String,
        displayName: String,
        email: String,
        photoUrl: String?,
        userType: String,
        createdAt: Date,
        lastSeenAt: Date
    ) : Task<Unit>
    fun getUsers(): Task<List<User>>

    fun isUserSigned() : Boolean
    suspend fun getUser(uid: String? = null): User
    fun checkUser(uid: String? = null): Task<Unit>
    fun logout(): Task<Unit>
    fun deleteUser(uid: String): Task<Unit>

    fun updateUser(
        uid: String,
        displayName: String,
        email: String,
        imageFile: File?,
        userType: String,
        createdAt: Date,
        lastSeenAt: Date
    ) : Task<Unit>

}