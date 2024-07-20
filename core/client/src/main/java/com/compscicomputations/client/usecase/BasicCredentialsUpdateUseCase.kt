package com.compscicomputations.client.usecase

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BasicAuthProvider
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.plugin
import javax.inject.Inject

class BasicCredentialsUpdateUseCase @Inject constructor(
    private val client: HttpClient
) {
    operator fun invoke(email: String, password: String) {
        val auth = client.plugin(Auth)
        auth.providers.removeIf { it is BasicAuthProvider }
        auth.basic {
            credentials {
                Log.d("PasswordCredentials Update", "Email: $email\nPassword: $password")
                BasicAuthCredentials(email, password)
            }
            sendWithoutRequest { true }
        }
    }
}