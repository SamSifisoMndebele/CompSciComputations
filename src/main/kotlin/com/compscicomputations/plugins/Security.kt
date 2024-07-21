package com.compscicomputations.plugins

import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.impl.GoogleVerifier
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.apache.http.auth.InvalidCredentialsException
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

internal fun Application.configureSecurity() {
    val logger = LoggerFactory.getLogger("Security")
    val googleVerifier = GoogleVerifier()
    val authService by inject<AuthService>()

    install(Authentication) {
        bearer("google") {
            realm = "Authenticate google user."
            authenticate {
                try {
                    val googleToken = googleVerifier.authenticate(it.token)
                        ?: throw InvalidCredentialsException("Invalid google token.")
                    authService.readUserByEmail(googleToken.email)
                        ?: let {
                            logger.warn("Goggle user does not exists.")
                            authService.createUser(googleToken)
                        }
                } catch (e: Exception) {
                    logger.warn("Goggle Auth", e)
                    null
                }
            }
        }

        basic {
            realm = "User access."
            validate { credentials ->
                val email = credentials.name
                try {
                    authService.validatePassword(email, credentials.password)
                } catch (e: Exception) {
                    logger.warn("Basic::User", e)
                    null
                }
            }
        }

        basic("admin") {
            realm = "Admin access."
            validate { credentials ->
                val email = credentials.name
                try {
                    authService.validatePassword(email, credentials.password).let {
                        if (it.isAdmin) it
                        else throw InvalidCredentialsException("User with email: $email is not an admin.")
                    }
                } catch (e: Exception) {
                    logger.warn("Basic::Admin", e)
                    null
                }
            }
        }
    }
}

internal fun Route.authenticateGoogle(
    build: Route.() -> Unit
): Route = authenticate("google", build = build)

internal fun Route.authenticateAdmin(
    optional: Boolean = false,
    build: Route.() -> Unit
): Route = authenticate("admin", optional = optional, build = build)
