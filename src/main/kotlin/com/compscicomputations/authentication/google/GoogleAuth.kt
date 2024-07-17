package com.compscicomputations.authentication.google

import com.compscicomputations.authentication.AuthProvider
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.plugins.*
import org.slf4j.LoggerFactory

internal class GoogleAuth {
    private val logger = LoggerFactory.getLogger("GoogleAuth")
    private val transport by lazy { ApacheHttpTransport() }
    private val jsonFactory by lazy { GsonFactory.getDefaultInstance() }
    private val verifier by lazy {
        GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(listOf(System.getenv("GOOGLE_CLIENT_ID")))
            .build()
    }

    internal fun authenticateToken(idTokenString: String): GoogleToken? {
        return try {
            val idToken = verifier.verify(idTokenString) ?: throw BadRequestException("Invalid ID token.")
            val payload = idToken.payload
            payload?.let {
                GoogleToken(
                    email = it.email,
                    pictureUrl = it["picture"] as String?,
                    givenName = it["given_name"] as String?,
                    familyName = it["family_name"] as String?
                )
            }
        } catch (e: Exception) {
            logger.warn(e.message)
            throw AuthProvider.InvalidCredentialsException(e.message)
        }
    }
}