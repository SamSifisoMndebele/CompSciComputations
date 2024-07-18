package com.compscicomputations.plugins

import com.compscicomputations.authentication.GoogleAuth
import com.compscicomputations.authentication.GoogleToken
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.requests.NewUser
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
    val googleAuth = GoogleAuth()
    val authService by inject<AuthService>()

    suspend fun createGoogleUser(googleToken: GoogleToken, password: String): User {
        logger.warn("Goggle user does not exists.")
        return authService.createUser(NewUser(
            email = googleToken.email,
            names = googleToken.names?:"",
            lastname = googleToken.lastname?:"",
            isAdmin = false,
            isStudent = false,
            password = password,
            photoUrl = googleToken.photoUrl,
            phone = null,
            adminPin = null,
            course = null,
            school = null
        ))
    }

    install(Authentication) {

        bearer("google") {
            realm = "Google user server access."
            authenticate {
                try {
                    googleAuth.authenticateToken(it.token)
                        ?.let { googleToken ->
                            val tempPassword = PasswordEncryptor.generatePassword(length = 36)
                            authService.readUserByEmail(googleToken.email)
                                ?.let { user ->
                                    authService.updatePassword(user.id, tempPassword)
                                    user.copy(tempPassword = tempPassword)
                                }
                                ?: let {
                                    createGoogleUser(googleToken, tempPassword).copy(
                                        tempPassword = tempPassword
                                    )
                                }
                        }
                } catch (e: Exception) {
                    logger.warn("Goggle", e)
                    null
                }
            }
        }

        basic {
            realm = "User server access"
            validate { credentials ->
                val email = credentials.name
                val password = credentials.password

                try {
                    authService.validatePassword(email, password)
                } catch (e: Exception) {
                    logger.warn("Basic::User", e)
                    null
                }
            }
        }

        basic("admin") {
            realm = "Admin server access"
            validate { credentials ->
                val email = credentials.name
                val password = credentials.password

                try {
                    authService.validatePassword(email, password).let {
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
