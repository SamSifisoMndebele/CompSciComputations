package com.compscicomputations.core.database.dao

import android.content.Context
import com.compscicomputations.core.database.model.AuthUser
import com.compscicomputations.core.database.model.UserType

interface AuthDao {
    fun currentUser() : AuthUser?
    suspend fun logout()
    suspend fun login(email: String, password: String): AuthUser
    suspend fun googleLogin(context: Context, userType: UserType = UserType.STUDENT): AuthUser
    suspend fun sendSignInLink(email: String)
    suspend fun linkLogin(email: String, link: String): AuthUser

    suspend fun register(
        email: String,
        password: String,
        displayName: String,
        phone: String,
        photoUrl: String?,
        userType: UserType
    )

    suspend fun sendResetEmail(email: String)
}