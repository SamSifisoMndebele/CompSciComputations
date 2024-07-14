package com.compscicomputations.core.ktor_client.auth

import android.app.Activity
import android.util.Log
import com.compscicomputations.core.ktor_client.auth.models.AuthUser
import com.compscicomputations.core.ktor_client.auth.models.Usertype
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    /**
     * Register with email and password
     * @param email new user email address
     * @param password new password
     * @throws Exception if is not successful.
     */
    suspend fun register(email: String, password: String)

    /**
     * Login with email and password
     * @param email user email address
     * @param password user password
     * @throws NoSuchElementException if there's no user wih email.
     * @throws IllegalArgumentException if wrong credentials
     */
    suspend fun login(email: String, password: String)

    /**
     * Login or register with google credentials.
     * @param activity activity level context.
     * @return `true` if the user is new or `false` if the user is existing.
     */
    suspend fun continueWithGoogle(activity: Activity): Boolean

    /**
     * Logout the user and set new last seen.
     */
    suspend fun logout(): Boolean

    companion object {
        private const val TAG = "AuthRepository"
        class NullAuthUserException : Exception("UserRepository: No user logged in because auth.currentUser is null")
        internal val FirebaseUser.tokenResultFlow: Flow<GetTokenResult?>
            get() = flow {
                val token = getIdToken(false).await()
                emit(token)
                Log.d(TAG, "TokenResult::$token")
            }.catch { e ->
                emit(null)
                Log.e(TAG, "TokenResult::error", e)
            }

        private val FirebaseUser.usertypeFlow: Flow<GetTokenResult?>
            get() = flow {
                val usertypeString = tokenResultFlow.first()?.claims?.get("usertype")
                Log.d(TAG, "Usertype::$usertypeString")
                when (usertypeString) {
                    is String -> emit(Usertype.valueOf(usertypeString))
                    null -> emit(null)
                    else -> emit(null)
                }
            }.catch { e ->
                emit(null)
                if (e !is NullPointerException) Log.e(TAG, "usertypeFlow::error", e)
            }

        fun isUserSignedIn(): Boolean = Firebase.auth.currentUser != null

        /**
         * @return [AuthUser] the currently signed-in FirebaseUser or error if there is none.
         * @throws NullAuthUserException if user is not logged in and is null. Make sure the user is logged in before calling this method.
         */
        fun getAuthUser(): AuthUser = Firebase.auth.currentUser?.let {
            AuthUser(
                uid = it.uid,
                email = it.email!!,
                displayName = it.displayName,
                photoUrl = it.photoUrl?.toString(),
                phoneNumber = it.phoneNumber,
                usertypeFlow = it.usertypeFlow
            )
        } ?: throw NullAuthUserException()
    }
}