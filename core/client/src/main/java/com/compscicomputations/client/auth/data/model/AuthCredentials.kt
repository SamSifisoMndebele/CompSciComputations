package com.compscicomputations.client.auth.data.model

import io.ktor.util.encodeBase64

data class AuthCredentials(val email: String?, val password: String?, val googleIdToken: String?) {
    override fun toString(): String {
        when {
            !email.isNullOrBlank() && !password.isNullOrBlank() -> {
                val encoded = "$email:$password".encodeBase64()
                return "Basic $encoded"
            }
            !googleIdToken.isNullOrBlank() -> return "Bearer $googleIdToken"
            else -> throw IllegalArgumentException("Invalid credentials")
        }
    }
    operator fun invoke() = toString()
}