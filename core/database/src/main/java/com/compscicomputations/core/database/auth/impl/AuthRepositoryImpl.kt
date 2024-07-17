package com.compscicomputations.core.database.auth.impl

import android.util.Log
import com.compscicomputations.core.database.auth.AuthRepository
import com.compscicomputations.core.database.auth.models.Users
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
            val request = client.post(Users.Me.LastSeen())
            return if (request.status == HttpStatusCode.OK) {
//                Firebase.auth.signOut()
                TODO()
                true
            } else false
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "logout: ", e)
            throw e
        }
    }
}