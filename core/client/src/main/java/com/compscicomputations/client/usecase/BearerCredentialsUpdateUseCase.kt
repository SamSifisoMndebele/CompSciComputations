package com.compscicomputations.client.usecase

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.plugin
import javax.inject.Inject

class BearerCredentialsUpdateUseCase @Inject constructor(
    private val client: HttpClient
) {
    operator fun invoke(idToken: String) {
        val auth = client.plugin(Auth)
        auth.providers.removeIf { it is BearerAuthProvider }
        auth.bearer {
            loadTokens {
                Log.d("IdToken Update", "IdToken: $idToken")
                BearerTokens(idToken, "refreshToken")
            }
            sendWithoutRequest { request ->
                request.url.pathSegments == listOf("users", "google")
            }
        }
    }
}