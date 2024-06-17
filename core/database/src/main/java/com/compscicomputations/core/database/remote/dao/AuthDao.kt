package com.compscicomputations.core.database.remote.dao

import android.content.Context
import com.compscicomputations.core.database.model.Usertype
import com.google.firebase.auth.FirebaseUser


interface AuthDao {
    fun authUser() : FirebaseUser?
    suspend fun logout()

    suspend fun login(email: String, password: String)
    suspend fun loginWithGoogle(context: Context, userType: Usertype = Usertype.STUDENT)

    suspend fun register(
        email: String,
        password: String,
        displayName: String,
        phone: String,
        photoUrl: String?,
        userType: Usertype
    )

    suspend fun sendEmailVerification()

    suspend fun sendResetEmail(email: String)
}