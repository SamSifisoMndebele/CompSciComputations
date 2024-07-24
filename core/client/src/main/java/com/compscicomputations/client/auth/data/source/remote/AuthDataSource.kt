package com.compscicomputations.client.auth.data.source.remote

import android.util.Log
import com.compscicomputations.client.utils.Users
import com.compscicomputations.client.utils.ktorRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.onUpload
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.append
import io.ktor.http.contentType
import io.ktor.util.encodeBase64
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val client: HttpClient
) {
    companion object {
        const val TAG = "AuthDataSource"

//        class NotFoundException(message: String? = null): Exception(message)
        class UnauthorizedException(message: String? = null): Exception(message)
        class ExpectationFailedException(message: String? = null): Exception(message)
    }

    /**
     * Login or register with google credentials.
     * @return [RemoteUser] the database user record.
     * If theres non, it will create a new user with google user information.
     * @throws UnauthorizedException if the id token is not valid.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun continueWithGoogle(idTokenString: String): RemoteUser = ktorRequest {
        val response = client.get(Users.Google()) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $idTokenString")
            }
        }
        when (response.status) {
            HttpStatusCode.Unauthorized -> throw UnauthorizedException(response.body<String?>())
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<RemoteUser>()
            else -> throw Exception(response.bodyAsText())
        }
    }

    /**
     * @param registerUser [RegisterUser] the new user information.
     * @return [RemoteUser] the database user record.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun createUser(registerUser: RegisterUser): RemoteUser = ktorRequest {
        val response = client.post(Users()) {
            contentType(ContentType.Application.Json)
            setBody(registerUser)
        }
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.Created -> response.body<RemoteUser>()
            else -> throw Exception(response.bodyAsText())
        }
    }

    /**
     * @param id user unique identifier
     * @param imageBytes the user profile image bytearray.
     * @param onProgress the image upload progress callback.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun uploadProfileImage(
        id: String,
        imageBytes: ByteArray,
        onProgress: (bytesSent: Long, totalBytes: Long) -> Unit
    ): Unit = ktorRequest {
        val response = client.post(Users.Id.Image(Users.Id(id = id))) {
            contentType(ContentType.MultiPart.FormData)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("description", "Profile Image")
                        append("image", imageBytes, Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Image.PNG)
                            append(HttpHeaders.ContentDisposition, "filename=\"profile_image.png\"")
                        })
                    },
                    boundary = "WebAppBoundary"
                )
            )
            onUpload { bytesSentTotal, contentLength ->
                Log.d(TAG, "onUpload: Sent $bytesSentTotal bytes from $contentLength")
                onProgress(bytesSentTotal, contentLength)
            }
        }

        when (response.status) {
            HttpStatusCode.OK -> {}
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            else -> throw Exception(response.bodyAsText())
        }
    }


    /**
     * @param onProgress user image download progress callback.
     * @return [RemoteUser] the database user record.
     * @throws UnauthorizedException if the user credentials are not correct.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun getRemoteUser(
        email: String,
        password: String?,
        onProgress: (bytesReceived: Long, totalBytes: Long) -> Unit
    ): RemoteUser = ktorRequest {
        val response = client.get(Users.Me()) {
            headers {
                append(HttpHeaders.Authorization, "$email $password".encodeBase64())
            }
            onDownload { bytesSentTotal, contentLength ->
                println("onDownload: Received $bytesSentTotal bytes from $contentLength")
                onProgress(bytesSentTotal, contentLength)
            }
        }
        when (response.status) {
            HttpStatusCode.Unauthorized -> throw UnauthorizedException(response.body<String?>())
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