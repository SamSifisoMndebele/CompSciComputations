package com.compscicomputations.client.auth.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.compscicomputations.client.auth.data.source.local.UserDataStore.Companion.userDataStore
import com.compscicomputations.client.auth.data.source.local.UserSerializer.asUser
import com.compscicomputations.client.auth.data.source.remote.RemoteUser
import com.compscicomputations.client.auth.models.User
import com.compscicomputations.core.client.LocalUser
import com.google.protobuf.ByteString
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okio.ByteString.Companion.toByteString
import javax.inject.Inject

class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "UserDataStore"
        private val Context.userDataStore: DataStore<LocalUser> by dataStore(
            fileName = "user.pb",
            serializer = UserSerializer
        )
        suspend fun Context.isUserSignedIn(): Boolean = !userDataStore.data.first().id.isNullOrBlank()


        internal val Context.basicAuthCredentialsFlow: Flow<BasicAuthCredentials?>
            get() = flow {
                userDataStore.data.collect { localUser ->
                    if (localUser.email.isNullOrBlank() || localUser.password.isNullOrBlank()) emit(null)
                    else emit(BasicAuthCredentials(
                        username = localUser.email,
                        password = localUser.password
                    ))
                }
            }.catch { e ->
                Log.w("Error collecting password credentials.", e)
                emit(null)
            }

        internal val Context.bearerCredentialsFlow: Flow<BearerTokens?>
            get() = flow {
                userDataStore.data.collect { localUser ->
                    if (localUser.idToken.isNullOrBlank()) emit(null)
                    else emit(BearerTokens(localUser.idToken, "refreshToken"))
                }
            }.catch { e ->
                Log.w("Error collecting password credentials.", e)
                emit(null)
            }
    }

    internal suspend fun saveUser(user: User) {
        context.userDataStore.updateData { currentUser ->
            val builder = currentUser.toBuilder()
                .setId(user.id)
                .setEmail(user.email)
                .setDisplayName(user.displayName)
                .setImageId(user.imageId ?: 0)
                .setPhone(user.phone ?: "")
                .setIsAdmin(user.isAdmin)
                .setIsStudent(user.isStudent)
                .setIsEmailVerified(user.isEmailVerified)
            user.imageBytes?.let { builder.setImageBytes(ByteString.copyFrom(it)) }

            builder.build()
        }
    }

    internal suspend fun savePasswordCredentials(email: String, password: String) {
        context.userDataStore.updateData { currentUser ->
            currentUser.toBuilder()
                .setEmail(email)
                .setPassword(password)
                .build()
        }
    }

    internal suspend fun saveGoogleIdToken(idToken: String) {
        context.userDataStore.updateData { currentUser ->
            currentUser.toBuilder()
                .setIdToken(idToken)
                .build()
        }
    }

    internal suspend fun clearLocalUser() {
        try {
            context.userDataStore.updateData { LocalUser.getDefaultInstance() }
        } catch (e: Exception) {
            Log.w(TAG, "Clear User Error:", e)
            context.userDataStore.updateData { LocalUser.getDefaultInstance() }
        }
    }


    internal val userFlow: Flow<User?>
        get() = flow {
            context.userDataStore.data.collect { localUser ->
                if (localUser.id.isNullOrBlank() || localUser.email.isNullOrBlank()) emit(null)
                else emit(localUser.asUser)
            }
        }.catch { e ->
            Log.e(TAG, "Error fetching user", e)
            emit(null)
        }






//    internal suspend fun Context.updateEmail(email: String) {
//        userDataStore.updateData { currentUser ->
//            currentUser.toBuilder()
//                .setEmail(email)
//                .build()
//        }
//    }
}