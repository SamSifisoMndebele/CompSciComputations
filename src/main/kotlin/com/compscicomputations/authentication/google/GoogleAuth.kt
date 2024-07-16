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

    internal fun authenticateToken(idTokenString: String, asAdmin: Boolean): GoogleToken? {
        return try {
            val idToken = verifier.verify(idTokenString) ?: throw BadRequestException("Invalid ID token.")
            val payload = idToken.payload
            if (asAdmin) {
                //TODO("Admin verifier")
                val isAdmin = payload["admin"] as Boolean? ?: false
                if (isAdmin.not()) throw Exception("User is not recognized as an admin.")
            }

            payload?.let {
                GoogleToken(
                    email = it.email,
                    emailVerified = it.emailVerified,
                    name = it["name"] as String? ?: it.email?.substringBefore("@"),
                    pictureUrl = it["picture"] as String?,
                    givenName = it["given_name"] as String?,
                    familyName = it["family_name"] as String?,
                    isAdmin = asAdmin,
                )
            }
        } catch (e: Exception) {
            logger.warn(e.message)
            throw AuthProvider.InvalidCredentialsException(e.message)
        }
    }
}