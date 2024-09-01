package com.compscicomputations.services.auth

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.json.gson.GsonFactory

data class GoogleToken(
    val email: String,
    val names: String,
    val lastname: String,
    val photoUrl: String?,
    val emailVerified: Boolean,
)

internal class GoogleVerifier {
    private val transport = ApacheHttpTransport()
    private val jsonFactory = GsonFactory.getDefaultInstance()
    private val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(System.getenv("GOOGLE_CLIENT_IDS").split(","))
        .build()

    internal fun authenticate(idTokenString: String): GoogleToken? {
        return verifier.verify(idTokenString)?.payload
            ?.let {
                GoogleToken(
                    email = it.email,
                    names = it["given_name"] as String? ?: "",
                    lastname = it["family_name"] as String? ?: "",
                    photoUrl = it["picture"] as String?,
                    emailVerified = it.emailVerified,
                )
            }
    }
}