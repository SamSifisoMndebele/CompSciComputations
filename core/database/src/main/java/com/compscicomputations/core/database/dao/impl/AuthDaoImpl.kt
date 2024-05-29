package com.compscicomputations.core.database.dao.impl

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.compscicomputations.core.database.UserType
import com.compscicomputations.core.database.dao.AuthDao
import com.compscicomputations.core.database.dao.UserDao
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.actionCodeSettings
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class AuthDaoImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val credentialRequest: GetCredentialRequest,
    private val userDao: UserDao
) : AuthDao {
    override fun isUserSigned(): Boolean {
        return auth.currentUser != null
    }
    override suspend fun logout() {
        userDao.updateUserLastSeen(lastSeenAt = Date())
        auth.signOut()
    }
    override suspend fun login(email: String, password: String): String? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.displayName
    }

    override suspend fun googleLogin(context: Context, userType: UserType): String? {
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
            Log.e("LoginViewModel", "Received an invalid google id token response", e)
            throw Exception("Received an invalid google id token response")
        }

        val result = auth.signInWithCredential(GoogleAuthProvider.getCredential(googleIdToken, null)).await()
        val user = result.user!!
        try {
            if (userDao.getUser(user.uid) == null) {
                userDao.insertUser(
                    uid = user.uid,
                    displayName = user.displayName.toString(),
                    email = user.email!!,
                    photoUrl = user.photoUrl?.toString(),
                    userType = userType,
                    createdAt = if (user.metadata?.creationTimestamp != null)
                        Date(user.metadata?.creationTimestamp!!) else Date(),
                    lastSeenAt = if (user.metadata?.lastSignInTimestamp != null)
                        Date(user.metadata?.lastSignInTimestamp!!) else Date()
                )
            }
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "googleLogin:insertUser:failure profile insert user", e)
        }
        return user.displayName
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

    override suspend fun linkLogin(email: String, link: String): String? {
        val isLinkSignIn = auth.isSignInWithEmailLink(link)
        if (!isLinkSignIn) throw Exception("Invalid link.")
        val result = auth.signInWithEmailLink(email, link).await()
        return result.user!!.displayName
    }

    override suspend fun register(email: String, password: String,
                                  displayName: String,
                                  photoUrl: String?,
                                  userType: UserType) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val creationTimestamp = result.user!!.metadata?.creationTimestamp
        val lastSignInTimestamp = result.user!!.metadata?.lastSignInTimestamp
        try {
            userDao.insertUser(
                uid = result.user!!.uid,
                displayName = displayName,
                email = email,
                photoUrl = photoUrl,
                userType = userType,
                createdAt = if (creationTimestamp != null) Date(creationTimestamp) else Date(),
                lastSeenAt = if (lastSignInTimestamp != null) Date(lastSignInTimestamp) else Date()
            )
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "register:insertUser:failure profile insert user", e)
        }
    }

    override suspend fun sendResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }
}