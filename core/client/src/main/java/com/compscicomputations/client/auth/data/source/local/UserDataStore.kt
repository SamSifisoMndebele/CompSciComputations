package com.compscicomputations.client.auth.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.compscicomputations.client.auth.data.model.AuthCredentials
import com.compscicomputations.client.auth.data.model.Student
import com.compscicomputations.client.auth.data.model.local.UserSerializer.asUser
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.auth.data.model.local.LocalUser
import com.compscicomputations.client.auth.data.model.local.UserSerializer
import com.compscicomputations.client.auth.data.model.remote.UpdateUser
import com.compscicomputations.client.utils.asByteString
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
        suspend fun Context.isUserSignedIn(): Boolean {
            val localUser = userDataStore.data.first()
            return !(localUser.id.isNullOrBlank() || localUser.email.isNullOrBlank())
        }

        class AuthCredentialsUseCase @Inject constructor(
            @ApplicationContext private val context: Context
        ) {
            suspend operator fun invoke(): AuthCredentials {
                return context.userDataStore.data
                    .first()
                    .let {
                        AuthCredentials(it.email, it.password, it.idToken)
                    }
            }
        }
    }

    internal suspend fun saveUser(user: User) {
        context.userDataStore.updateData { currentUser ->
            val builder = currentUser.toBuilder()
                .setId(user.id)
                .setEmail(user.email)
                .setNames(user.names)
                .setLastname(user.lastname)
                .setLastname(user.phone ?: "")
                .setIsEmailVerified(user.isEmailVerified)
                .setIsAdmin(user.isAdmin)
                .setIsStudent(user.isStudent)
                .setUniversity(user.university ?: "")
                .setSchool(user.school ?: "")
                .setCourse(user.course ?: "")
            user.imageBitmap?.let { builder.setImageBytes(it.asByteString) }
            builder.build()
        }
    }

    internal suspend fun updateUser(user: UpdateUser) {
        context.userDataStore.updateData { currentUser ->
            val builder = currentUser.toBuilder()
                .setNames(user.names)
                .setLastname(user.lastname)
                .setLastname(user.phone ?: "")
                .setIsStudent(user.isStudent)
                .setUniversity(user.university ?: "")
                .setSchool(user.school ?: "")
                .setCourse(user.course ?: "")
            user.image?.bytes?.let { builder.setImageBytes(it.asByteString) }
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

}