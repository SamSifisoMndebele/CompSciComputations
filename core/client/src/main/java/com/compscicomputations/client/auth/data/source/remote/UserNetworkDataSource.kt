package com.compscicomputations.client.auth.data.source.remote

import android.util.Log
import com.compscicomputations.client.auth.models.NewUser
import com.compscicomputations.client.auth.models.User
import com.compscicomputations.client.auth.models.Users
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject

class UserNetworkDataSource @Inject constructor(
    private val client: HttpClient
) {
    /**
     * @return [RemoteUser] the authorized signed-in remote user information or `null` if there is none.
     * @throws Exception
     */
    suspend fun getUser(): RemoteUser? {
        val response = client.get(Users.Me())
        return when {
            response.status == HttpStatusCode.NotFound -> null
            response.status != HttpStatusCode.OK -> throw Exception(response.bodyAsText())
            else -> response.body<RemoteUser>()
        }
    }
    /**
     * @param id the user unique identifier
     * @return [RemoteUser] the authorized signed-in remote user information or `null` if there is none.
     * @throws Exception
     */
    suspend fun getUser(id: String): RemoteUser? {
        val response = client.get(Users.Id(id = id))
        return when {
            response.status == HttpStatusCode.NotFound -> null
            response.status != HttpStatusCode.OK -> throw Exception(response.bodyAsText())
            else -> response.body<RemoteUser>()
        }
    }
    /**
     * @param email the user email identifier
     * @return [RemoteUser] the authorized signed-in remote user information or `null` if there is none.
     * @throws Exception
     */
    suspend fun getUserByEmail(email: String): RemoteUser? {
        val response = client.get(Users.Email(email = email))
        return when {
            response.status == HttpStatusCode.NotFound -> null
            response.status != HttpStatusCode.OK -> throw Exception(response.bodyAsText())
            else -> response.body<RemoteUser>()
        }
    }

    /**
     * @param user [NewUser] the user account information.
     * @return [RemoteUser] the authorized signed-in remote user information or `null` if there is none.
     * @throws Exception
     */
    suspend fun createUser(user: NewUser): RemoteUser {
        val response = client.post(Users()) {
            contentType(ContentType.Application.Json)
            setBody(user)
        }
        return when {
            response.status != HttpStatusCode.Created -> throw Exception(response.bodyAsText())
            else -> response.body<RemoteUser>()
        }
    }


    suspend fun continueWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential): Boolean {
        Log.d(TAG, "Authorization: Bearer ${googleIdTokenCredential.idToken}")
        Log.d(TAG, "email: ${googleIdTokenCredential.id}")
        Log.d(TAG, "displayName: ${googleIdTokenCredential.displayName}")
        Log.d(TAG, "givenName: ${googleIdTokenCredential.givenName}")
        Log.d(TAG, "familyName: ${googleIdTokenCredential.familyName}")
        Log.d(TAG, "profilePictureUri: ${googleIdTokenCredential.profilePictureUri}")
        Log.d(TAG, "phoneNumber: ${googleIdTokenCredential.phoneNumber}")

//        val googleCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
//        val authResult = Firebase.auth.signInWithCredential(googleCredential).await()
//        return authResult.additionalUserInfo!!.isNewUser
        TODO()
    }
    
    companion object {
        const val TAG = "UserNetworkDataSource"
    }
}