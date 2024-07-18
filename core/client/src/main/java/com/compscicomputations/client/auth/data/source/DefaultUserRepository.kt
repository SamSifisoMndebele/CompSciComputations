package com.compscicomputations.client.auth.data.source

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.compscicomputations.client.auth.data.source.local.CredentialsSerializer
import com.compscicomputations.client.auth.data.source.local.UserSerializer
import com.compscicomputations.client.auth.data.source.local.UserSerializer.asUser
import com.compscicomputations.client.auth.data.source.remote.UserNetworkDataSource
import com.compscicomputations.client.auth.models.User
import com.compscicomputations.core.client.LoginCredentials
import com.compscicomputations.core.client.UserLocal
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: UserNetworkDataSource,
) {

    internal suspend fun Context.updateEmail(email: String) {
        userDataStore.updateData { currentUser ->
            currentUser.toBuilder()
                .setEmail(email)
                .build()
        }
    }

    val currentUserFlow: Flow<User?>
        get() = flow {
            context.userDataStore.data.collect { currentUser ->
                if (currentUser != UserLocal.getDefaultInstance()) {
                    emit(currentUser.asUser)
                } else {
                    emit(null)
                }
            }
        }.catch { e ->
            Log.e(TAG, "Error fetching user", e)
            emit(null)
        }

    val refreshUserFlow: Flow<User?>
        get() = flow<User?> {
            val networkUser = withContext(Dispatchers.IO) {
                networkDataSource.getUser()
            }
            networkUser?.let { user ->
                emit(user.toExternal)
                context.userDataStore.updateData {
                    user.asLocalUser
                }
            } ?: let {
                context.userDataStore.updateData {
                    UserLocal.getDefaultInstance()
                }
            }
        }.catch { e ->
            Log.e(TAG, "Error fetching user", e)
            emit(null)
        }

    suspend fun login() {
        networkDataSource.getUser()?.let { user ->
            context.userDataStore.updateData {
                user.asLocalUser
            }
        }
    }

    suspend fun saveCredentials(email: String, password: String) {
        context.credentialsDataStore.updateData {
            LoginCredentials.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build()
        }
    }
    suspend fun clearCredentials() {
        try {
            context.credentialsDataStore.updateData {
                LoginCredentials.getDefaultInstance()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Clear Credentials Error:", e)
        }
    }

    data class PasswordCredentials(
        val email: String,
        val password: String,
    )

    val passwordCredentialsFlow: Flow<PasswordCredentials?>
        get() = context.passwordCredentialsFlow

    suspend fun continueWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential) {
        TODO()
    }

    suspend fun logout() {
        context.credentialsDataStore.updateData {
            LoginCredentials.getDefaultInstance()
        }
        context.userDataStore.updateData {
            UserLocal.getDefaultInstance()
        }
    }

    companion object {
        private const val TAG = "DefaultUserRepository"

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
        internal fun Context.idTokenCredentialsFlow(refresh: Boolean = false): Flow<String?> =
            credentialsDataStore.data.map {
                if (it == LoginCredentials.getDefaultInstance()) null
                else it.idToken
            }


        private val Context.userDataStore: DataStore<UserLocal> by dataStore(
            fileName = "user.pb",
            serializer = UserSerializer
        )

        val Context.isUserSignedInFlow: Flow<Boolean>
            get() = flow {
                val currentUser = userDataStore.data.first()
                emit(currentUser != UserLocal.getDefaultInstance())
            }.catch { e ->
                Log.e(TAG, "Error checking if user is signed in", e)
                emit(false)
            }
    }

}