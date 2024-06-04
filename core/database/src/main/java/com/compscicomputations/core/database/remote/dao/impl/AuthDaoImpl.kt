package com.compscicomputations.core.database.remote.dao.impl

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.compscicomputations.core.database.dao.UserDao
import com.compscicomputations.core.database.remote.dao.AuthDao
import com.compscicomputations.core.database.remote.model.AuthUser
import com.compscicomputations.core.database.remote.model.UserType
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.actionCodeSettings
import io.github.jan.supabase.exceptions.UnknownRestException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthDaoImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val credentialRequest: GetCredentialRequest,
    private val userDao: UserDao
) : AuthDao {
    private fun firebaseUser(firebaseUser: FirebaseUser?): AuthUser? {
        val user = firebaseUser ?: auth.currentUser ?: return null
        return AuthUser(
            user.uid,
            user.email,
            user.displayName,
            user.photoUrl,
            user.isAnonymous,
            user.isEmailVerified
        )
    }

    override fun currentUser(): AuthUser? = firebaseUser(null)

    override suspend fun logout() {
        userDao.updateUserLastSeen()
        auth.signOut()
    }
    override suspend fun login(email: String, password: String): AuthUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val authUser = firebaseUser(result.user)!!
        try {
            userDao.updateUserLastSignIn(authUser.uid)
        } catch (e: Exception) {
            Log.e("AuthDaoImpl", "login:failure", e)
        }
        return firebaseUser(result.user)!!
    }

    override suspend fun googleLogin(context: Context, userType: UserType): AuthUser {
        val credential = credentialManager.getCredential(
            request = credentialRequest,
            context = context,
        ).credential

        val googleIdToken = try {
            if (credential is CustomCredential
                && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                GoogleIdTokenCredential.createFrom(credential.data).idToken
            } else {
                throw Exception("Unexpected type of credential")
            }
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("AuthDaoImpl", "Received an invalid google id token response", e)
            throw Exception("Received an invalid google id token response")
        }

        val result = auth.signInWithCredential(GoogleAuthProvider.getCredential(googleIdToken, null)).await()
        val authUser = firebaseUser(result.user)!!
        try {
            userDao.getUser(authUser.uid)
            userDao.updateUserLastSignIn(authUser.uid)
        } catch (e: UnknownRestException) {
            if (e.message?.split('=')?.get(0) == "NoSuchUserException") {
                when(userType) {
                    UserType.ADMIN -> TODO()
                    UserType.STUDENT -> userDao.insertStudentUser(
                        uid = authUser.uid,
                        displayName = authUser.displayName.toString(),
                        email = authUser.email!!,
                        photoUrl = authUser.photoUrl?.toString(),
                        course = "_course",
                        school = "_school"
                    )
                    UserType.OTHER -> TODO()
                }
            } else throw e
        }
        return authUser
    }

    override suspend fun sendSignInLink(email: String) {
        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://www.example.com/finishSignUp?cartId=1234"
            handleCodeInApp = true // This must be true
            setAndroidPackageName(
                "com.compscicomputations",
                true, // installIfNotAvailable
                "1", // minimumVersion
            )
        }
        auth.sendSignInLinkToEmail(email, actionCodeSettings).await()
    }

    override suspend fun linkLogin(email: String, link: String): AuthUser {
        val isLinkSignIn = auth.isSignInWithEmailLink(link)
        if (!isLinkSignIn) throw Exception("Invalid link.")
        val result = auth.signInWithEmailLink(email, link).await()
        return firebaseUser(result.user)!!
    }

    override suspend fun register(email: String, password: String,
                                  displayName: String,
                                  phone: String,
                                  photoUrl: String?,
                                  userType: UserType
    ) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val authUser = firebaseUser(result.user)!!
        try {
            if (!authUser.anonymous) {
                userDao.insertStudentUser(
                    uid = authUser.uid,
                    displayName = authUser.displayName.toString(),
                    email = authUser.email!!,
                    phone = phone,
                    photoUrl = authUser.photoUrl?.toString(),
                    course = "_course",
                    school = "_school"
                )
            }
        } catch (e: Exception) {
            Log.e("AuthDaoImpl", "register:insertUser:failure profile insert user", e)
        }
    }

    override suspend fun sendResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }
}