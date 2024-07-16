package com.compscicomputations.authentication

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.plugins.*
import org.intellij.lang.annotations.Language


class Auth {
    val idTokenString = "idTokenString"

    private val transport = ApacheHttpTransport()
    private val jsonFactory = GsonFactory.getDefaultInstance()
    private var verifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(listOf(System.getenv("GOOGLE_CLIENT_ID")))
        .build()
    // Verify received token
    private val idToken = verifier.verify(idTokenString) ?: throw BadRequestException("Invalid ID token.")

    val payload = idToken.payload

    // Get user identifier
    var userId = payload.subject

    var iss = payload.issuer
    var sub = payload.subject
    var aud = payload.audience
    var iat = payload.issuedAtTimeSeconds
    var exp = payload.expirationTimeSeconds


    // Get profile information you need to get/create a user
    var email = payload.email
    var emailVerified = payload.emailVerified
    val name = payload["name"] as String? ?: email.substringBefore("@")
    var pictureUrl = payload["picture"] as String?
    var givenName = payload["given_name"] as String?
    var familyName = payload["family_name"] as String?
    var locale = payload["locale"] as String?


    // Use or store profile information
    @Language("json")
    val json = """
        {
         "iss": "https://accounts.google.com",
         "sub": "110169484474386276334",
         "azp": "1008719970978-hb24n2dstb40o45d4feuo2ukqmcc6381.apps.googleusercontent.com",
         "aud": "1008719970978-hb24n2dstb40o45d4feuo2ukqmcc6381.apps.googleusercontent.com",
         "iat": "1433978353",
         "exp": "1433981953",

         "email": "testuser@gmail.com",
         "email_verified": "true",
         "name" : "Test User",
         "picture": "https://lh4.googleusercontent.com/-kYgzyAWpZzJ/ABCDEFGHI/AAAJKLMNOP/tIXL9Ir44LE/s99-c/photo.jpg",
         "given_name": "Test",
         "family_name": "User",
         "locale": "en"
        }
    """.trimIndent()

//    val user = userApi.getUserByEmail(email) ?: registerUser(email, name, pictureUrl)

//    val tokens = tokenProvider.createTokens(user)
}