package com.compscicomputations.plugins

import com.compscicomputations.services.auth.AuthService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.apache.http.auth.InvalidCredentialsException
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

internal fun Application.configureSecurity() {
    val logger = LoggerFactory.getLogger("Security")
    val authService by inject<AuthService>()

    install(Authentication) {
        bearer("google") {
            realm = "Authenticate google user."
            authenticate {
                try {
                    authService.readUser(it.token)
                } catch (e: Exception) {
                    logger.warn("GoggleBearer", e)
                    null
                }
            }
        }

        basic {
            realm = "User access."
            validate { credentials ->
                val email = credentials.name
                try {
                    authService.readUser(email, credentials.password)
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
                    authService.readUser(email, credentials.password).let {
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
