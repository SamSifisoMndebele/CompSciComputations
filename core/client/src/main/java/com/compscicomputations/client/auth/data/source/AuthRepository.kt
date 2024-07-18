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
import com.compscicomputations.client.auth.data.source.remote.RemoteUser
import com.compscicomputations.client.auth.models.NewUser
import com.compscicomputations.client.auth.models.PasswordCredentials
import com.compscicomputations.client.auth.models.User
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: AuthDataSource,
) {




//    val refreshUserFlow: Flow<User?>
//        get() = flow<User?> {
//            val networkUser = withContext(Dispatchers.IO) {
//                networkDataSource.getUser()
//            }
//            networkUser?.let { user ->
//                emit(user.toExternal)
//                context.userDataStore.updateData {
//                    user.asLocalUser
//                }
//            } ?: let {
//                context.userDataStore.updateData {
//                    LocalUser.getDefaultInstance()
//                }
//            }
//        }.catch { e ->
//            Log.e(TAG, "Error fetching user", e)
//            emit(null)
//        }






    /// --- Credentials --------------------------------------------------------

    suspend fun savePasswordCredentials(email: String, password: String) = context.savePasswordCredentials(email, password)

    suspend fun saveIdTokenCredentials(idToken: String) = context.saveIdTokenCredentials(idToken)

    suspend fun clearUserCredentials() = context.clearUserCredentials()

    val passwordCredentialsFlow: Flow<PasswordCredentials?>
        get() = context.passwordCredentialsFlow

    val idTokenCredentialsFlow: Flow<String?>
        get() = context.idTokenCredentialsFlow




    /// --- User --------------------------------------------------------

    suspend fun createUser(newUser: NewUser): User = networkDataSource.createUser(newUser).asUser

    val currentUserFlow: Flow<User?>
        get() = context.userFlow

    /**
     * Login or register with google credentials.
     * If theres non, it will create a new user with google user information.
     * @throws UnauthorizedException if the id token is not valid.
     * @throws ExpectationFailedException if the was server side error.
     */
    suspend fun continueWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential) {
        try {
            context.saveIdTokenCredentials(googleIdTokenCredential.idToken)
            networkDataSource.continueWithGoogle().let { user ->
                Log.d(TAG, "RemoteUser: $user")
                context.savePasswordCredentials(user.email, user.googlePassword!!)
                context.saveUser(user)
            }
        } catch (e: UnauthorizedException) {
            Log.e(TAG, "UnauthorizedException::continueWithGoogle", e)
        } catch (e: ExpectationFailedException) {
            Log.e(TAG, "ExpectationFailedException::continueWithGoogle", e)
        } catch (e: Exception) {
            Log.e(TAG, "Exception::continueWithGoogle", e)
        }
    }

    /**
     * Login with email and password
     */
    suspend fun continuePassword(email: String, password: String) {
        context.savePasswordCredentials(email, password)
        networkDataSource.getUser().let { user ->
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