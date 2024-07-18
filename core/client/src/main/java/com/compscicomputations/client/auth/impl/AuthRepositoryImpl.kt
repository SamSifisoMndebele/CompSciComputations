package com.compscicomputations.client.auth.impl

import android.util.Log
import com.compscicomputations.client.auth.AuthRepository
import com.compscicomputations.client.auth.models.Users
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.post
import io.ktor.http.HttpStatusCode

internal class AuthRepositoryImpl(
    private val client: HttpClient
) : AuthRepository {
    override suspend fun register(email: String, password: String) {
//        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun login(email: String, password: String) {
//        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun continueWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential): Boolean {
        Log.d("AuthRepositoryImpl", "Authorization: Bearer ${googleIdTokenCredential.idToken}")
        Log.d("AuthRepositoryImpl", "email: ${googleIdTokenCredential.id}")
        Log.d("AuthRepositoryImpl", "displayName: ${googleIdTokenCredential.displayName}")
        Log.d("AuthRepositoryImpl", "givenName: ${googleIdTokenCredential.givenName}")
        Log.d("AuthRepositoryImpl", "familyName: ${googleIdTokenCredential.familyName}")
        Log.d("AuthRepositoryImpl", "profilePictureUri: ${googleIdTokenCredential.profilePictureUri}")
        Log.d("AuthRepositoryImpl", "phoneNumber: ${googleIdTokenCredential.phoneNumber}")

//        val googleCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
//        val authResult = Firebase.auth.signInWithCredential(googleCredential).await()
//        return authResult.additionalUserInfo!!.isNewUser
        TODO()
    }

    override suspend fun logout(): Boolean {
        try {
            return false
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "logout: ", e)
            throw e
        }
    }
}