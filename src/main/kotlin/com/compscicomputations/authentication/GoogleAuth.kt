package com.compscicomputations.authentication

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.json.gson.GsonFactory
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
            verifier.verify(idTokenString)?.payload
                ?.let {
                    GoogleToken(
                        email = it.email,
                        photoUrl = it["picture"] as String?,
                        names = it["given_name"] as String?,
                        lastname = it["family_name"] as String?
                    )
                }
        } catch (e: Exception) {
            logger.warn(e.message)
            throw e
        }
    }
}