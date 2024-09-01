package com.compscicomputations.client.auth.data.model

import io.ktor.util.encodeBase64

data class AuthCredentials(val email: String?, val password: String?, val googleIdToken: String?) {
    override fun toString(): String {
        return when {
            !email.isNullOrBlank() && !password.isNullOrBlank() -> "Basic ${"$email:$password".encodeBase64()}"
            !googleIdToken.isNullOrBlank() -> "Bearer $googleIdToken"
            else -> throw IllegalArgumentException("Invalid credentials")
        }
    }
    operator fun invoke() = toString()
}