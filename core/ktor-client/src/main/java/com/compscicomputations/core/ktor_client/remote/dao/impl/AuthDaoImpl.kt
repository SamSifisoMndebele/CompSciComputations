package com.compscicomputations.core.ktor_client.remote.dao.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.compscicomputations.core.ktor_client.model.Usertype
import com.compscicomputations.core.ktor_client.remote.dao.AuthDao
import com.compscicomputations.core.ktor_client.remote.repo.UserRepo
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.exceptions.UnknownRestException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AuthDaoImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @ApplicationContext
    private val context: Context,
    private val credentialManager: CredentialManager,
    @Named("login")
    private val loginCredentialRequest: GetCredentialRequest,
    @Named("register")
    private val registerCredentialRequest: GetCredentialRequest,
    private val userRepo: UserRepo
) : AuthDao {
    override fun authUser(): FirebaseUser? = auth.currentUser

    override suspend fun logout() {
        userRepo.updateUserLastSeen()
        auth.signOut()
    }
    override suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun loginWithGoogle(context1: Context, userType: Usertype) {
        val credentialManager = CredentialManager.create(context)
        val credential = credentialManager.getCredential(
            request = loginCredentialRequest,
            context = context,
        ).credential

//        val googleIdToken = try {
//            if (credential is CustomCredential
//                && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//                GoogleIdTokenCredential.createFrom(credential.data).idToken
//            } else throw Exception("Unexpected type of credential")
//        } catch (e: GoogleIdTokenParsingException) {
//            throw Exception("Received an invalid google id token response")
//        }
        val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
        Log.d("AuthDaoImpl", credential.data.toString())
        Log.d("AuthDaoImpl", GoogleIdTokenCredential.createFrom(credential.data).idToken)
        auth.signInWithCredential(GoogleAuthProvider.getCredential(googleIdToken, null)).await()
        val user = authUser()!!
        try {
            userRepo.getUser(user.uid)
//            userDao.updateUserLastSignIn(user.uid)
        } catch (e: UnknownRestException) {
            if (e.message?.split('=')?.get(0) == "NoSuchUserException") {
                userRepo.insertUser(
                    uid = user.uid,
                    displayName = user.displayName.toString(),
                    email = user.email!!,
                    photoUrl = user.photoUrl?.toString(),
                    phone = user.phoneNumber,
                )
//                when(userType) {
//                    Usertype.ADMIN -> TODO()
//                    Usertype.STUDENT -> userDao.insertStudentUser(
//                        uid = user.uid,
//                        displayName = user.displayName.toString(),
//                        email = user.email!!,
//                        photoUrl = user.photoUrl?.toString(),
//                        course = "_course",
//                        school = "_school"
//                    )
//                    Usertype.OTHER -> TODO()
//                }
            } else throw e
        }
    }

    override suspend fun register(email: String, password: String,
                                  displayName: String,
                                  phone: String,
                                  photoUrl: String?,
                                  userType: Usertype
    ) {
        val loginResult = auth.createUserWithEmailAndPassword(email, password).await()
        val profileUpdates = userProfileChangeRequest {
            this.displayName = displayName
            this.photoUri = if (photoUrl.isNullOrBlank()) null else Uri.parse(photoUrl)
        }

        val user = loginResult.user!!
        user.updateProfile(profileUpdates).await()

        try {
            /*userDao.insertStudentUser(
                uid = user.uid,
                displayName = user.displayName.toString(),
                email = user.email!!,
                phone = phone,
                photoUrl = user.photoUrl?.toString(),
                course = "_course",
                school = "_school"
            )*/
        } catch (e: Exception) {
            Log.e("AuthDaoImpl", "register:insertUser:failure profile insert user", e)
        }
    }

    override suspend fun sendEmailVerification() {
        TODO("Not yet implemented")
    }


    override suspend fun sendResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }
}