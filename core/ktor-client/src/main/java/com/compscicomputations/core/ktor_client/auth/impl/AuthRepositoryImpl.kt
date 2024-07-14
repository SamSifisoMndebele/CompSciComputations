package com.compscicomputations.core.ktor_client.auth.impl

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.compscicomputations.core.ktor_client.auth.AuthRepository
import com.compscicomputations.core.ktor_client.auth.models.Users
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.post
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

internal class AuthRepositoryImpl(
    private val request: GetCredentialRequest,
    private val client: HttpClient
) : AuthRepository {
    override suspend fun register(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun login(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun continueWithGoogle(activity: Activity): Boolean {
        val credentialManager = CredentialManager.create(activity)
        val result = try {
            credentialManager.getCredential(activity, request)
        } catch (e: GetCredentialCancellationException) {
            throw CancellationException(e.message, e.cause)
        }

        val credential = result.credential
        when {
            credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                // Use googleIdTokenCredential and extract id to validate and
                // authenticate on your server.
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val authResult = Firebase.auth.signInWithCredential(googleCredential).await()
                return authResult.additionalUserInfo!!.isNewUser
            }
            else -> {
                // Catch any unrecognized credential type here.
                throw Exception("Unexpected type of credential")
            }
        }
    }

    override suspend fun logout(): Boolean {
        try {
            val request = client.post(Users.Me.LastSeen())
            return if (request.status == HttpStatusCode.OK) {
                Firebase.auth.signOut()
                true
            } else false
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "logout: ", e)
            throw e
        }
    }
}