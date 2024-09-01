package com.compscicomputations.client.auth.data.source.remote

import android.util.Log
import com.compscicomputations.client.auth.data.model.AuthCredentials
import com.compscicomputations.client.auth.data.model.remote.ResetPassword
import com.compscicomputations.client.auth.data.model.remote.NewUser
import com.compscicomputations.client.auth.data.model.remote.RemoteUser
import com.compscicomputations.client.auth.data.model.remote.UpdateUser
import com.compscicomputations.client.auth.data.source.local.UserDataStore.Companion.AuthCredentialsUseCase
import com.compscicomputations.client.utils.Users
import com.compscicomputations.client.utils.ktorRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.onUpload
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val client: HttpClient,
    private val authCredentials: AuthCredentialsUseCase
) {
    companion object {
        const val TAG = "AuthDataSource"

//        class NotFoundException(message: String? = null): Exception(message)
        class UnauthorizedException: Exception("Invalid credentials!")
        class ExpectationFailedException(message: String? = null): Exception(message)
        class OtpException(message: String? = null): Exception(message)
    }

    /**
     * @param newUser [NewUser] the new user information.
     * @param onProgress the image upload progress callback.
     * @return [RemoteUser] the database user record.
     * @throws ExpectationFailedException if the was server side error.
     * @throws OtpException if the otp was wrong or expired.
     */
    internal suspend fun createUser(
        newUser: NewUser,
        onProgress: (bytesSent: Long, totalBytes: Long) -> Unit
    ): RemoteUser = ktorRequest {
        val response = client.post(Users()) {
            contentType(ContentType.Application.Json)
            setBody(newUser)
            onUpload { bytesSentTotal, contentLength ->
                Log.d(TAG, "on upload image: Sent $bytesSentTotal bytes from $contentLength")
                onProgress(bytesSentTotal, contentLength)
            }
        }
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.PreconditionFailed -> throw OtpException(response.bodyAsText())
            HttpStatusCode.Created -> response.body<RemoteUser>()
            else -> throw Exception(response.bodyAsText())
        }
    }

    /**
     * @param updateUser [UpdateUser] the new user information.
     * @param onProgress the image upload progress callback.
     * @return [RemoteUser] the database user record.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun updateUser(
        updateUser: UpdateUser,
        onProgress: (bytesSent: Long, totalBytes: Long) -> Unit
    ): RemoteUser = ktorRequest {
        val response = client.put(Users.Me()) {
            contentType(ContentType.Application.Json)
            setBody(updateUser)
            onUpload { bytesSentTotal, contentLength ->
                Log.d(TAG, "on upload image: Sent $bytesSentTotal bytes from $contentLength")
                onProgress(bytesSentTotal, contentLength)
            }
        }
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<RemoteUser>()
            else -> throw Exception(response.bodyAsText())
        }
    }

    internal suspend fun passwordResetOtp(email: String): String = ktorRequest {
        val response = client.get(Users.PasswordReset.Email(email = email))
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.bodyAsText()
            else -> throw Exception(response.bodyAsText())
        }
    }

    internal suspend fun registerOtp(email: String): String = ktorRequest {
        val response = client.get(Users.Email.Otp(Users.Email(email = email)))
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.bodyAsText()
            else -> throw Exception(response.bodyAsText())
        }
    }

    internal suspend fun passwordReset(resetPassword: ResetPassword): Unit = ktorRequest {
        val response = client.post(Users.PasswordReset()) {
            contentType(ContentType.Application.Json)
            setBody(resetPassword)
        }
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> { }
            else -> throw Exception(response.bodyAsText())
        }
    }

    /**
     * Login or register with google credentials, or login with password credentials.
     * @param onProgress user image download progress callback.
     * @return [RemoteUser] the database user record.
     * @throws UnauthorizedException if the user credentials are not correct.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun getRemoteUser(
        authCredentials: AuthCredentials? = null,
        onProgress: (bytesReceived: Long, totalBytes: Long) -> Unit
    ): RemoteUser = ktorRequest {
        val credentials = authCredentials ?: this.authCredentials()
        val response = client.get(Users.Me()) {
            headers {
                append(HttpHeaders.Authorization, credentials())
            }
            onDownload { bytesSentTotal, contentLength ->
                println("on download image: Received $bytesSentTotal bytes from $contentLength")
                onProgress(bytesSentTotal, contentLength)
            }
        }
        when (response.status) {
            HttpStatusCode.Unauthorized -> throw UnauthorizedException()
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<RemoteUser>()
            else -> throw Exception(response.bodyAsText())
        }
    }

//    /**
//     * @param id this user unique identifier.
//     * @param onProgress the image download progress callback.
//     * @throws ExpectationFailedException if the was server side error.
//     * @return [ByteArray] the user profile image bytearray.
//     */
//    internal suspend fun downloadProfileImage(
//        id: Int,
//        onProgress: (bytesReceived: Long, totalBytes: Long) -> Unit
//    ): ByteArray = ktorRequest {
//        val response = client.get(Files.Images.Id(id = id)) {
//            onDownload { bytesSentTotal, contentLength ->
//                println("onDownload: Received $bytesSentTotal bytes from $contentLength")
//                onProgress(bytesSentTotal, contentLength)
//            }
//        }
//        when (response.status) {
//            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
//            HttpStatusCode.OK -> response.body<ByteArray>()
//            else -> throw Exception("Unexpected response.")
//        }
//    }








//    /**
//     * @param id the user unique identifier
//     * @return [RemoteUser] the database user record.
//     * @throws NotFoundException if the is no corresponding user.
//     * @throws UnauthorizedException if the current user is unauthorized to get the requested user record.
//     * @throws ExpectationFailedException if the was server side error.
//     */
//    internal suspend fun getUser(id: String): RemoteUser = ktorRequest {
//        val response = client.get(Users.Id(id = id))
//        when (response.status) {
//            HttpStatusCode.NotFound -> throw NotFoundException(response.body<String?>())
//            HttpStatusCode.Unauthorized -> throw UnauthorizedException(response.body<String?>())
//            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
//            HttpStatusCode.OK -> response.body<RemoteUser>()
//            else -> throw Exception("Unexpected response.")
//        }
//    }
//
//    /**
//     * @param email the user email identifier
//     * @return [RemoteUser] the database user record.
//     * @throws NotFoundException if the is no corresponding user.
//     * @throws UnauthorizedException if the current user is unauthorized to get the requested user record.
//     * @throws ExpectationFailedException if the was server side error.
//     */
//    internal suspend fun getUserByEmail(email: String): RemoteUser = ktorRequest {
//        val response = client.get(Users.Email(email = email))
//        when (response.status) {
//            HttpStatusCode.NotFound -> throw NotFoundException(response.body<String?>())
//            HttpStatusCode.Unauthorized -> throw UnauthorizedException(response.body<String?>())
//            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
//            HttpStatusCode.OK -> response.body<RemoteUser>()
//            else -> throw Exception("Unexpected response.")
//        }
//    }
}