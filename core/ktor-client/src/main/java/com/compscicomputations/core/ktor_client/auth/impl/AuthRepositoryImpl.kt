package com.compscicomputations.core.ktor_client.auth.impl

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.compscicomputations.core.ktor_client.auth.AuthRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.ktor.client.HttpClient
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

internal class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val request: GetCredentialRequest,
    private val client: HttpClient
) : AuthRepository {
    override suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun loginWithGoogle(activity: Activity) {
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
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                auth.signInWithCredential(firebaseCredential).await()
            }
            else -> {
                // Catch any unrecognized credential type here.
                throw Exception("Unexpected type of credential")
            }
        }
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun register() {
        TODO("Not yet implemented")
    }

    override suspend fun registerWithGoogle(activity: Activity) {
        TODO("Not yet implemented")
    }
}