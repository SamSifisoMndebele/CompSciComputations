package com.compscicomputations.client.auth.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.compscicomputations.client.auth.data.source.local.UserSerializer.asUser
import com.compscicomputations.client.auth.models.RemoteUser
import com.compscicomputations.client.auth.models.User
import com.compscicomputations.core.client.UserLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

object UserDataStore {
    private const val TAG = "UserDataStore"
    private val Context.userDataStore: DataStore<UserLocal> by dataStore(
        fileName = "user.pb",
        serializer = UserSerializer
    )

    internal suspend fun Context.saveUser(remoteUser: RemoteUser) {
        userDataStore.updateData {
            remoteUser.asLocalUser
        }
    }

    val Context.isUserSignedInFlow: Flow<Boolean>
        get() = flow {
            val currentUser = userDataStore.data.first()
            emit(currentUser != UserLocal.getDefaultInstance())
        }.catch { e ->
            Log.e(TAG, "Error checking if user is signed in", e)
            emit(false)
        }

    internal val Context.userFlow: Flow<User?>
        get() = flow {
            userDataStore.data.collect { localUser ->
                if (localUser != UserLocal.getDefaultInstance()) {
                    emit(localUser.asUser)
                } else {
                    emit(null)
                }
            }
        }.catch { e ->
            Log.e(TAG, "Error fetching user", e)
            emit(null)
        }


    internal suspend fun Context.clearUser() {
        try {
            userDataStore.updateData {
                UserLocal.getDefaultInstance()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Clear User Error:", e)
        }
    }









//    internal suspend fun Context.updateEmail(email: String) {
//        userDataStore.updateData { currentUser ->
//            currentUser.toBuilder()
//                .setEmail(email)
//                .build()
//        }
//    }
}