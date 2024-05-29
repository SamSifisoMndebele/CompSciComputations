package com.compscicomputations.core.database.dao

import android.content.Context
import com.compscicomputations.core.database.UserType

interface AuthDao {
    fun isUserSigned() : Boolean
    suspend fun logout()
    suspend fun login(email: String, password: String): String?
    suspend fun googleLogin(context: Context, userType: UserType = UserType.STUDENT): String?
    suspend fun sendSignInLink(email: String)
    suspend fun linkLogin(email: String, link: String): String?

    suspend fun register(
        email: String,
        password: String,
        displayName: String,
        photoUrl: String?,
        userType: UserType
    )

    suspend fun sendResetEmail(email: String)
}