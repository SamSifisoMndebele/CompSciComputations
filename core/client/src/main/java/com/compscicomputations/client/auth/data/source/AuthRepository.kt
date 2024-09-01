package com.compscicomputations.client.auth.data.source

import android.content.Context
import android.util.Log
import com.compscicomputations.client.auth.data.model.AuthCredentials
import com.compscicomputations.client.auth.data.source.local.UserDataStore
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource.Companion.ExpectationFailedException
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource.Companion.UnauthorizedException
import com.compscicomputations.client.auth.data.model.remote.NewUser
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.auth.data.model.remote.ResetPassword
import com.compscicomputations.client.auth.data.model.remote.UpdateUser
import com.compscicomputations.client.auth.data.source.local.UserDataStore.Companion.AuthCredentialsUseCase
import com.compscicomputations.client.utils.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: AuthDataSource,
    private val localDataStore: UserDataStore,
    private val authCredentials: AuthCredentialsUseCase,
) {
    companion object {
        private const val TAG = "AuthRepository"
    }

    /**
     * Login with email and password
     * @throws UnauthorizedException if the user credentials are not correct.
     * @throws ExpectationFailedException if the was server side error.
     */
    suspend fun login(email: String, password: String) {
        localDataStore.savePasswordCredentials(email, password)
        remoteDataSource.getRemoteUser(AuthCredentials(email, password, null)) { _, _->} .let { user ->
            localDataStore.saveUser(user.asUser)
        }
    }

    /**
     * Login or register with google credentials.
     * If theres non, it will create a new user with google user information.
     * @throws UnauthorizedException if the id token is not valid.
     * @throws ExpectationFailedException if the was server side error.
     */
    suspend fun continueWithGoogle(googleIdTokenString: String) {
        remoteDataSource.getRemoteUser(AuthCredentials(null, null, googleIdTokenString)) {_,_->}.let { user ->
            localDataStore.saveGoogleIdToken(googleIdTokenString)
            localDataStore.saveUser(user.asUser)
        }
    }

    /**
     * Register and login a user
     * @param newUser user information
     * @param onProgress the upload progress callback.
     * @throws ExpectationFailedException if the was server side error.
     */
    suspend fun createUser(
        newUser: NewUser,
        onProgress: (bytesSent: Long, totalBytes: Long) -> Unit
    ) {
        remoteDataSource.createUser(newUser, onProgress).let {
            localDataStore.savePasswordCredentials(newUser.email, newUser.password)
            localDataStore.saveUser(it.asUser)
        }
    }

    /**
     * Update user information
     * @param updateUser updated user information
     * @param onProgress the upload progress callback.
     */
    suspend fun updateUser(
        updateUser: UpdateUser,
        onProgress: (bytesSent: Long, totalBytes: Long) -> Unit
    ) {
        remoteDataSource.updateUser(updateUser, onProgress)
            .let { user ->
                localDataStore.saveUser(user.asUser)
            }
    }

    suspend fun passwordResetOtp(email: String) = remoteDataSource.passwordResetOtp(email)
    suspend fun registerOtp(email: String) = remoteDataSource.registerOtp(email)

    suspend fun passwordResetOtp(
        email: String,
        otp: String,
        newPassword: String,
    ) = remoteDataSource.passwordReset(
        ResetPassword(
            email = email,
            otp = otp,
            newPassword = newPassword,
            password = null
        )
    )

    suspend fun passwordReset(
        email: String,
        password: String,
        newPassword: String,
    ) = remoteDataSource.passwordReset(
        ResetPassword(
            email = email,
            otp = null,
            password = password,
            newPassword = newPassword,
        )
    )

    /**
     * Logout the current user.
     */
    suspend fun logout() {
        localDataStore.clearLocalUser()
    }

    val currentUserFlow: Flow<User?>
        get() = localDataStore.userFlow

    val refreshUserFlow: Flow<User?>
        get() = flow {
            remoteDataSource.getRemoteUser { bytesReceived, totalBytes ->
                Log.d(TAG, "onDownload User Image: Received $bytesReceived bytes from $totalBytes")
            }.asUser.let { user ->
                localDataStore.saveUser(user)
                emit(user)
            }
        }.retry(2) {
            Log.w(TAG, "Error fetching user, Retrying.", it)
            it is ExpectationFailedException
        }
}