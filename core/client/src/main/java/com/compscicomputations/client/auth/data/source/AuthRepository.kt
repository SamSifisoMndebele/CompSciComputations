package com.compscicomputations.client.auth.data.source

import android.content.Context
import android.util.Log
import com.compscicomputations.client.auth.data.source.local.UserCredentialsDataStore.clearUserCredentials
import com.compscicomputations.client.auth.data.source.local.UserCredentialsDataStore.idTokenCredentialsFlow
import com.compscicomputations.client.auth.data.source.local.UserCredentialsDataStore.passwordCredentialsFlow
import com.compscicomputations.client.auth.data.source.local.UserCredentialsDataStore.saveIdTokenCredentials
import com.compscicomputations.client.auth.data.source.local.UserCredentialsDataStore.savePasswordCredentials
import com.compscicomputations.client.auth.data.source.local.UserDataStore.clearUser
import com.compscicomputations.client.auth.data.source.local.UserDataStore.saveUser
import com.compscicomputations.client.auth.data.source.local.UserDataStore.userFlow
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource.Companion.ExpectationFailedException
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource.Companion.UnauthorizedException
import com.compscicomputations.client.auth.data.source.remote.NewUser
import com.compscicomputations.client.auth.data.source.remote.UserImage
import com.compscicomputations.client.auth.models.PasswordCredentials
import com.compscicomputations.client.auth.models.User
import com.compscicomputations.client.usecase.BasicCredentialsUpdateUseCase
import com.compscicomputations.client.usecase.BearerCredentialsUpdateUseCase
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: AuthDataSource,
    private val basicCredentialsUpdate: BasicCredentialsUpdateUseCase,
    private val bearerCredentialsUpdate: BearerCredentialsUpdateUseCase,
) {
    /// --- Credentials --------------------------------------------------------

    suspend fun savePasswordCredentials(email: String, password: String) = context.savePasswordCredentials(email, password)

    suspend fun saveIdTokenCredentials(idToken: String) = context.saveIdTokenCredentials(idToken)

    suspend fun clearUserCredentials() = context.clearUserCredentials()

    val passwordCredentialsFlow: Flow<PasswordCredentials?>
        get() = context.passwordCredentialsFlow

    val idTokenCredentialsFlow: Flow<String?>
        get() = context.idTokenCredentialsFlow




    /// --- User --------------------------------------------------------

    suspend fun createUser(
        newUser: NewUser,
        imageBytes: ByteArray?,
        onProgress: (bytesSent: Long, totalBytes: Long) -> Unit
    ): String {
        remoteDataSource.createUser(newUser).let {
            context.saveUser(it)
            if (imageBytes != null) {
                remoteDataSource.uploadProfileImage(it.id, imageBytes, onProgress)
            }
            return it.id
        }
    }

//    private suspend fun uploadProfileImage(
//        id: String,
//        imageBytes: ByteArray,
//        onProgress: (bytesSent: Long, totalBytes: Long) -> Unit
//    ): UserImage = remoteDataSource.uploadProfileImage(id, imageBytes, onProgress)

    val currentUserFlow: Flow<User?>
        get() = context.userFlow

    val refreshUserFlow: Flow<User?>
        get() = flow {
            remoteDataSource.getUser().let { user ->
                context.saveUser(user)
                emit(user.asUser)
            }
        }/*.retry(3) {
            Log.w(TAG, "Error fetching user, Retrying.", it)
            it is ExpectationFailedException
        }*//*.catch {
            Log.e(TAG, "Error fetching user", it)
            context.clearUser()
            emit(null)
        }*/

    /**
     * Login or register with google credentials.
     * If theres non, it will create a new user with google user information.
     * @throws UnauthorizedException if the id token is not valid.
     * @throws ExpectationFailedException if the was server side error.
     */
    suspend fun continueWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential) {
        bearerCredentialsUpdate(googleIdTokenCredential.idToken)
        remoteDataSource.continueWithGoogle().let { user ->
            Log.d(TAG, "RemoteUser: $user")
            context.savePasswordCredentials(user.email, user.googlePassword!!)
            context.saveUser(user)
        }
    }

    /**
     * Login with email and password
     */
    suspend fun continuePassword(email: String, password: String) {
        basicCredentialsUpdate(email, password)
        remoteDataSource.getUser().let { user ->
            context.saveUser(user)
        }
    }

    suspend fun logout() {
        context.clearUserCredentials()
        context.clearUser()
    }

    companion object {
        private const val TAG = "AuthRepository"

    }

}