package com.compscicomputations.client.auth.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.compscicomputations.client.auth.models.PasswordCredentials
import com.compscicomputations.core.client.UserCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserCredentialsDataStore {
    private const val TAG = "UserCredentialsDataStore"
    private val Context.credentialsDataStore: DataStore<UserCredentials> by dataStore(
        fileName = "credentials.pb",
        serializer = CredentialsSerializer
    )


    internal val Context.passwordCredentialsFlow: Flow<PasswordCredentials?>
        get() = credentialsDataStore.data.map {
            if (it == UserCredentials.getDefaultInstance()) null
            else PasswordCredentials(
                it.email,
                it.password
            )
        }
    internal val Context.idTokenCredentialsFlow: Flow<String?>
        get() = credentialsDataStore.data.map {
            if (it == UserCredentials.getDefaultInstance()) null
            else it.idToken
        }

    internal suspend fun Context.savePasswordCredentials(email: String, password: String) {
        credentialsDataStore.updateData {
            UserCredentials.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build()
        }
    }
    internal suspend fun Context.saveIdTokenCredentials(idToken: String) {
        credentialsDataStore.updateData {
            UserCredentials.newBuilder()
                .setIdToken(idToken)
                .build()
        }
    }

    internal suspend fun Context.clearUserCredentials() {
        try {
            credentialsDataStore.updateData {
                UserCredentials.getDefaultInstance()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Clear Credentials Error:", e)
        }
    }

}