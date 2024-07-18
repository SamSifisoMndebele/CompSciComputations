package com.compscicomputations.client.auth.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.compscicomputations.client.auth.models.PasswordCredentials
import com.compscicomputations.core.client.LoginCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserCredentialsDataStore {
    private const val TAG = "UserCredentialsDataStore"
    private val Context.credentialsDataStore: DataStore<LoginCredentials> by dataStore(
        fileName = "credentials.pb",
        serializer = CredentialsSerializer
    )


    internal val Context.passwordCredentialsFlow: Flow<PasswordCredentials?>
        get() = credentialsDataStore.data.map {
            if (it == LoginCredentials.getDefaultInstance()) null
            else PasswordCredentials(
                it.email,
                it.password
            )
        }
    internal val Context.idTokenCredentialsFlow: Flow<String?>
        get() = credentialsDataStore.data.map {
            if (it == LoginCredentials.getDefaultInstance()) null
            else it.idToken
        }

    internal suspend fun Context.savePasswordCredentials(email: String, password: String) {
        credentialsDataStore.updateData { credentials ->
            credentials.toBuilder()
                .setEmail(email)
                .setPassword(password)
                .build()
        }
    }
    internal suspend fun Context.saveIdTokenCredentials(idToken: String) {
        credentialsDataStore.updateData { credentials ->
            credentials.toBuilder()
                .setIdToken(idToken)
                .build()
        }
    }

    internal suspend fun Context.clearUserCredentials() {
        try {
            credentialsDataStore.updateData {
                LoginCredentials.getDefaultInstance()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Clear Credentials Error:", e)
        }
    }

}