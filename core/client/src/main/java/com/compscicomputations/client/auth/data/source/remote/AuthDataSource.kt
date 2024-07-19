package com.compscicomputations.client.auth.data.source.remote

import android.util.Log
import com.compscicomputations.client.auth.data.source.local.UserCredentialsDataStore.idTokenCredentialsFlow
import com.compscicomputations.client.auth.data.source.local.UserCredentialsDataStore.passwordCredentialsFlow
import com.compscicomputations.client.auth.models.NewUser
import com.compscicomputations.client.auth.models.PasswordCredentials
import com.compscicomputations.client.auth.models.Users
import com.compscicomputations.client.utils.ktorRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BasicAuthProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.plugin
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val client: HttpClient
) {
    companion object {
        const val TAG = "AuthDataSource"

        class NotFoundException(message: String? = null): Exception(message)
        class UnauthorizedException(message: String? = null): Exception(message)
        class ExpectationFailedException(message: String? = null): Exception(message)
    }

    internal fun basicCredentialsUpdate(email: String, password: String) {
        val auth = client.plugin(Auth)
        auth.providers.removeIf { it is BasicAuthProvider }
        auth.basic {
            credentials {
                Log.d("PasswordCredentialsUpdate", "email: $email, password: $password")
                BasicAuthCredentials(email, password)
            }
            sendWithoutRequest { true }
        }
    }
    internal fun bearerCredentialsUpdate(idToken: String) {
        val auth = client.plugin(Auth)
        auth.providers.removeIf { it is BearerAuthProvider }
        auth.bearer {
            loadTokens {
                Log.d("IdTokenUpdate", idToken)
                BearerTokens(idToken, "refreshToken")
            }
            sendWithoutRequest { request ->
                request.url.pathSegments == listOf("users", "google")
            }
        }
    }


    /**
     * @return [RemoteUser] the database user record.
     * @throws UnauthorizedException if the user credentials are not correct.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun getUser(): RemoteUser = ktorRequest {
        val response = client.get(Users.Me())
        when (response.status) {
            HttpStatusCode.Unauthorized -> throw UnauthorizedException(response.body<String?>())
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<RemoteUser>()
            else -> throw Exception("Unexpected response.")
        }
    }

    /**
     * Login or register with google credentials.
     * @return [RemoteUser] the database user record.
     * If theres non, it will create a new user with google user information.
     * @throws UnauthorizedException if the id token is not valid.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun continueWithGoogle(): RemoteUser = ktorRequest {
        val response = client.get(Users.Google())
        when (response.status) {
            HttpStatusCode.Unauthorized -> throw UnauthorizedException(response.body<String?>())
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<RemoteUser>()
            else -> throw Exception("Unexpected response.")
        }
    }

    /**
     * @param id the user unique identifier
     * @return [RemoteUser] the database user record.
     * @throws NotFoundException if the is no corresponding user.
     * @throws UnauthorizedException if the current user is unauthorized to get the requested user record.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun getUser(id: String): RemoteUser = ktorRequest {
        val response = client.get(Users.Id(id = id))
        when (response.status) {
            HttpStatusCode.NotFound -> throw NotFoundException(response.body<String?>())
            HttpStatusCode.Unauthorized -> throw UnauthorizedException(response.body<String?>())
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<RemoteUser>()
            else -> throw Exception("Unexpected response.")
        }
    }

    /**
     * @param email the user email identifier
     * @return [RemoteUser] the database user record.
     * @throws NotFoundException if the is no corresponding user.
     * @throws UnauthorizedException if the current user is unauthorized to get the requested user record.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun getUserByEmail(email: String): RemoteUser = ktorRequest {
        val response = client.get(Users.Email(email = email))
        when (response.status) {
            HttpStatusCode.NotFound -> throw NotFoundException(response.body<String?>())
            HttpStatusCode.Unauthorized -> throw UnauthorizedException(response.body<String?>())
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<RemoteUser>()
            else -> throw Exception("Unexpected response.")
        }
    }

    /**
     * @param newUser [NewUser] the new user information.
     * @return [RemoteUser] the database user record.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun createUser(newUser: NewUser): RemoteUser = ktorRequest {
        val response = client.post(Users()) {
            contentType(ContentType.Application.Json)
            setBody(newUser)
        }
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.Created -> response.body<RemoteUser>()
            else -> throw Exception("Unexpected response.")
        }
    }
}