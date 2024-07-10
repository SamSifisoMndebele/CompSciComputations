package com.compscicomputations.firebase

import io.ktor.http.auth.*
import io.ktor.http.parsing.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

internal class FirebaseAuthProvider(config: FirebaseAuthConfig, private val isAdmin: Boolean = false) : AuthenticationProvider(config) {

    private val realm = config.realm
    private val authenticate = config.authenticate
    private val firebaseAdmin: FirebaseAdmin = config.admin

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val authHeader = try {
            context.call.request.parseAuthorizationHeader()
        } catch (e: ParseException) {
            null
        } ?: let {
            context.challenge(CHALLENGE, AuthenticationFailedCause.NoCredentials) { challenge, call ->
                call.respond(createUnauthorizedResponse())
                challenge.complete()
            }
            return
        }

        val principal = (authHeader as? HttpAuthHeader.Single)
            ?.takeIf { authHeader.authScheme.lowercase() == AuthScheme.Bearer.lowercase() }
            ?.let { firebaseAdmin.authenticateToken(it.blob, isAdmin) }
            ?.let { authorizedToken ->
                authenticate(context.call, authorizedToken)
            }
            ?: let {
                context.challenge(
                    CHALLENGE,
                    AuthenticationFailedCause.InvalidCredentials
                ) { challenge, call ->
                    call.respond(createUnauthorizedResponse())
                    challenge.complete()
                }
                return
            }

        context.principal(principal)
    }

    private fun createUnauthorizedResponse() =
        UnauthorizedResponse(HttpAuthHeader.bearerAuthChallenge(scheme = AuthScheme.Bearer, realm = realm))

}

private const val CHALLENGE = "FirebaseAuth"

internal fun AuthenticationConfig.firebase(name: String? = null, configure: FirebaseAuthConfig.() -> Unit) {
    val config = FirebaseAuthConfig(name).apply(configure)
    val provider = FirebaseAuthProvider(config, name?.lowercase() == "admin")
    register(provider)
}

internal fun AuthenticationConfig.firebaseAdmin(configure: FirebaseAuthConfig.() -> Unit) {
    val config = FirebaseAuthConfig("admin").apply(configure)
    val provider = FirebaseAuthProvider(config, true)
    register(provider)
}
fun Route.authenticateAdmin(build: Route.() -> Unit): Route {
    return authenticate("admin", build = build)
}