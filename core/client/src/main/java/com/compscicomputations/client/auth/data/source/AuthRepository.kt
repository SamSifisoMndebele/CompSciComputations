package com.compscicomputations.client.auth.data.source

import android.content.Context
import android.util.Log
import com.compscicomputations.client.auth.data.model.AuthCredentials
import com.compscicomputations.client.auth.data.model.Student
import com.compscicomputations.client.auth.data.source.local.UserDataStore
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource.Companion.ExpectationFailedException
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource.Companion.UnauthorizedException
import com.compscicomputations.client.auth.data.model.remote.NewUser
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.auth.data.model.remote.NewPassword
import com.compscicomputations.client.auth.data.model.remote.UpdateUser
import com.compscicomputations.client.utils.Image
import com.compscicomputations.client.utils.asBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: AuthDataSource,
    private val localDataStore: UserDataStore,
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
        remoteDataSource.getRemoteUser(AuthCredentials(email, password, null)) { _, _->} .let { user ->
            localDataStore.savePasswordCredentials(email, password)
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
     * @param email user email
     * @param password user new password
     * @param names user full names
     * @param lastname user last names
     * @param imageBytes user profile image bytearray
     * @param onProgress the upload progress callback.
     * @throws ExpectationFailedException if the was server side error.
     */
    suspend fun createUser(
        email: String,
        password: String,
        names: String,
        lastname: String,
        imageBytes: ByteArray?,
        onProgress: (bytesSent: Long, totalBytes: Long) -> Unit
    ) {
        remoteDataSource.createUser(
            NewUser(
                email = email,
                password = password,
                names = names,
                lastname = lastname,
                image = imageBytes?.let { Image(it) }
            ), onProgress
        ).let {
            localDataStore.saveUser(it.asUser)
            localDataStore.savePasswordCredentials(email, password)
        }
    }

    /**
     * Update user information
     * @param id user unique identifier
     * @param updateUser updated user information
     * @param onProgress the upload progress callback.
     */
    suspend fun updateUser(
        id: String,
        updateUser: UpdateUser,
        onProgress: (bytesSent: Long, totalBytes: Long) -> Unit
    ) {
        if (updateUser.isStudent) {
            val student = Student(
                id = id,
                university = "university",
                course = "course",
                school = "school",
            )
            //Todo: Save student
        }
        //Todo: Save user
        localDataStore.updateUser(updateUser)
    }

    suspend fun requestOtp(email: String) = remoteDataSource.requestOtp(email)

    suspend fun passwordReset(
        email: String,
        otp: String,
        password: String,
    ) = remoteDataSource.passwordReset(
        NewPassword(
            email = email,
            otp = otp,
            password = password,
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

//    val refreshUserFlow: Flow<User?>
//        get() = flow {
//            remoteDataSource.getRemoteUser { bytesReceived, totalBytes ->
//                Log.d(TAG, "onDownload User Image: Received $bytesReceived bytes from $totalBytes")
//            }.asUser
//                .let { user ->
//                    localDataStore.saveUser(user)
//                    emit(user)
//                }
//        }.retry(2) {
//            Log.w(TAG, "Error fetching user, Retrying.", it)
//            it is ExpectationFailedException
//        }


}