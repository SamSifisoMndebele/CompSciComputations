package com.compscicomputations.client.auth

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

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

//
//        internal fun FirebaseUser.tokenResultFlow(refresh: Boolean = false): Flow<GetTokenResult?> = flow {
//            val token = getIdToken(refresh).await()
//            Log.d(TAG, "TokenResult::$token")
//            emit(token)
//        }.catch { e ->
//            Log.e(TAG, "TokenResult::error", e)
//            emit(null)
//        }

//        private val FirebaseUser.usertypeFlow: Flow<Usertype?>
//            get() = flow {
//                val usertypeString = tokenResultFlow().first()?.claims?.get("usertype")
//                Log.d(TAG, "Usertype::$usertypeString")
//                when (usertypeString) {
//                    is String -> emit(Usertype.valueOf(usertypeString))
//                    else -> emit(null)
//                }
//            }.catch { e ->
//                Log.e(TAG, "usertypeFlow::error", e)
//                emit(null)
//            }
    }
}