package com.compscicomputations.plugins

import com.compscicomputations.services.auth.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.http.auth.InvalidCredentialsException
import org.koin.ktor.ext.inject
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory

internal fun Application.configureSecurity() {
    val logger = LoggerFactory.getLogger("Security")
    val authService by inject<AuthService>()

    install(Authentication) {
        bearer("google") {
            realm = "Authentication with google."
            authenticate {
                try {
                    authService.googleUser(it.token)
                } catch (e: PSQLException) {
                    logger.warn("PSQLException: "+ e.serverErrorMessage)
                    null
                } catch (e: Exception) {
                    logger.warn("Exception", e)
                    null
                }
            }
        }
        basic("password") {
            realm = "Authentication with password."
            validate {
                val email = it.name
                try {
                    authService.readUser(email, it.password)
                } catch (e: Exception) {
                    logger.warn("Basic::User", e)
                    null
                }
            }
        }

        bearer("admin-google") {
            realm = "Admin authentication with google."
            authenticate {
                try {
                    authService.googleUser(it.token).let { user ->
                        if (user.isAdmin) user
                        else throw InvalidCredentialsException("User with email: ${user.email} is not an admin.")
                    }
                } catch (e: PSQLException) {
                    logger.warn("PSQLException: ${e.serverErrorMessage}")
                    null
                } catch (e: Exception) {
                    logger.warn("Exception", e)
                    null
                }
            }
        }
        basic("admin-password") {
            realm = "Admin authentication with password."
            validate {
                val email = it.name
                try {
                    authService.readUser(email, it.password).let { user ->
                        if (user.isAdmin) user
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

internal fun Route.authenticateUser(
    build: Route.() -> Unit
): Route = authenticate("password", "google", optional = true, build = build)

internal fun Route.authenticateAdmin(
    optional: Boolean = false,
    build: Route.() -> Unit
): Route = authenticate("admin-password", "admin-google", optional = optional, build = build)
