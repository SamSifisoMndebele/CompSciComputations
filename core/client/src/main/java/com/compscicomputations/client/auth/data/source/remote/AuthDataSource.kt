package com.compscicomputations.client.auth.data.source.remote

import com.compscicomputations.client.auth.models.NewUser
import com.compscicomputations.client.auth.models.RemoteUser
import com.compscicomputations.client.auth.models.Users
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

class AuthDataSource @Inject constructor(
    private val client: HttpClient
) {
    companion object {
        const val TAG = "AuthDataSource"
        class NotFoundException(message: String? = null): Exception(message)
        class UnauthorizedException(message: String? = null): Exception(message)
        class ExpectationFailedException(message: String? = null): Exception(message)
    }

    /**
     * @return [RemoteUser] the database user record.
     * @throws UnauthorizedException if the user credentials are not correct.
     * @throws ExpectationFailedException if the was server side error.
     */
    suspend fun getUser(): RemoteUser {
        val response = client.get(Users.Me)
        return when (response.status) {
            HttpStatusCode.Unauthorized -> throw UnauthorizedException(response.body<String?>())
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<RemoteUser>()
            else -> throw Exception("Unexpected response.")
        }
    }

    /**
     * @return [RemoteUser] the database user record.
     * If theres non, it will create a new user with google user information.
     * @throws UnauthorizedException if the id token is not valid.
     * @throws ExpectationFailedException if the was server side error.
     */
    suspend fun continueWithGoogle(): RemoteUser {
        val response = client.get(Users.Google)
        return when (response.status) {
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
    suspend fun getUser(id: String): RemoteUser {
        val response = client.get(Users.Id(id = id))
        return when (response.status) {
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
    suspend fun getUserByEmail(email: String): RemoteUser {
        val response = client.get(Users.Email(email = email))
        return when (response.status) {
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
    internal suspend fun createUser(newUser: NewUser): RemoteUser {
        val response = client.post(Users) {
            contentType(ContentType.Application.Json)
            setBody(newUser)
        }
        return when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.Created -> response.body<RemoteUser>()
            else -> throw Exception("Unexpected response.")
        }
    }
}