package com.compscicomputations.core.ktor_client.auth

import android.util.Log
import com.compscicomputations.core.ktor_client.auth.models.AuthUser
import com.compscicomputations.core.ktor_client.auth.models.Usertype
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
     * @param googleIdTokenCredential [GoogleIdTokenCredential]
     * @return `true` if the user is new or `false` if the user is existing.
     */
    suspend fun continueWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential): Boolean

    /**
     * Logout the user and set new last seen.
     */
    suspend fun logout(): Boolean

    companion object {
        private const val TAG = "AuthRepository"
        class NullAuthUserException : Exception("UserRepository: No user logged in because auth.currentUser is null")

        val isUserSignedIn: Boolean
            get() = Firebase.auth.currentUser != null

        internal fun FirebaseUser.tokenResultFlow(refresh: Boolean = false): Flow<GetTokenResult?> = flow {
            val token = getIdToken(refresh).await()
            Log.d(TAG, "TokenResult::$token")
            emit(token)
        }.catch { e ->
            Log.e(TAG, "TokenResult::error", e)
            emit(null)
        }

        private val FirebaseUser.usertypeFlow: Flow<Usertype?>
            get() = flow {
                val usertypeString = tokenResultFlow().first()?.claims?.get("usertype")
                Log.d(TAG, "Usertype::$usertypeString")
                when (usertypeString) {
                    is String -> emit(Usertype.valueOf(usertypeString))
                    else -> emit(null)
                }
            }.catch { e ->
                Log.e(TAG, "usertypeFlow::error", e)
                emit(null)
            }

        /**
         * @param withUsertype if `true` the usertype flow will be returned otherwise, the flow of `null`.
         * @return [AuthUser] the currently signed-in FirebaseUser or error if there is none.
         * @throws NullAuthUserException if user is not logged in and is null. Make sure the user is logged in before calling this method.
         */
        fun getAuthUser(withUsertype: Boolean = true): AuthUser = Firebase.auth.currentUser?.let {
            AuthUser(
                uid = it.uid,
                email = it.email!!,
                displayName = it.displayName,
                photoUrl = it.photoUrl?.toString(),
                phoneNumber = it.phoneNumber,
                usertypeFlow = if (withUsertype) it.usertypeFlow else flowOf(null)
            )
        } ?: throw NullAuthUserException()
    }
}