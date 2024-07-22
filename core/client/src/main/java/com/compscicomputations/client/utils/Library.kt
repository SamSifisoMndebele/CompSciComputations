package com.compscicomputations.client.utils

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BasicAuthProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

internal suspend inline fun <T> ktorRequest(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: suspend () -> T
): T = withContext(ioDispatcher) {
    block()
}

// This method might be computationally expensive
//internal fun createUUID() : String {
//    return UUID.randomUUID().toString()
//}



//internal fun HttpClient.basicCredentialsUpdate(email: String, password: String) {
//    val auth = plugin(Auth)
//    auth.providers.removeIf { it is BasicAuthProvider }
//    auth.basic {
//        credentials {
//            Log.d("PasswordCredentials Update", "Email: $email\nPassword: $password")
//            BasicAuthCredentials(email, password)
//        }
//        sendWithoutRequest { true }
//    }
//}
//internal fun HttpClient.bearerCredentialsUpdate(idToken: String) {
//    val auth = plugin(Auth)
//    auth.providers.removeIf { it is BearerAuthProvider }
//    auth.bearer {
//        loadTokens {
//            Log.d("IdToken Update", "IdToken: $idToken")
//            BearerTokens(idToken, "refreshToken")
//        }
//        sendWithoutRequest { request ->
//            request.url.pathSegments == listOf("users", "google")
//        }
//    }
//}