package com.compscicomputations.plugins

import com.compscicomputations.services.auth.impl.GoogleVerifier
import com.compscicomputations.services.auth.impl.GoogleToken
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.requests.RegisterUser
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.utils.PasswordEncryptor
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

    suspend fun GoogleToken.createUser(password: String): User {
        logger.warn("Goggle user does not exists.")
        return authService.createUser(RegisterUser(
            email = email,
            password = password,
            names = names,
            lastname = lastname,
            photoUrl = photoUrl
        )).copy(tempPassword = password)
    }

    install(Authentication) {

        bearer("google") {
            realm = "Authenticate google user."
            authenticate {
                try {
                    googleVerifier.authenticate(it.token)
                        ?.let { googleToken ->
                            val tempPassword = PasswordEncryptor.generatePassword(length = 36)
                            authService.readUserByEmail(googleToken.email)
                                ?.let { user ->
                                    authService.updatePassword(user.id, tempPassword)
                                    user.copy(tempPassword = tempPassword)
                                }
                                ?: let {
                                    googleToken.createUser(tempPassword)
                                }
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
                    authService.validatePassword(email, credentials.password).let {


                        it
                    }
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
