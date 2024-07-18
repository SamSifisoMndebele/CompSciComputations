package com.compscicomputations.authentication

import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.requests.NewUser
import io.ktor.http.auth.*
import io.ktor.http.parsing.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory
import java.util.*


internal class AuthProvider(config: AuthConfig, private val asAdmin: Boolean = false) : AuthenticationProvider(config) {
    private val logger = LoggerFactory.getLogger("AuthProvider")
    private val realm = config.realm(asAdmin)
    private val authenticate = config.authenticate
    private val googleAuth = config.googleAuth
    private val authService by inject<AuthService>(AuthService::class.java)

    companion object {
        private const val CHALLENGE = "AuthChallenge"
    }

    override suspend fun onAuthenticate(context: AuthenticationContext) {

        val authHeader = try {
            context.call.request.parseAuthorizationHeader() ?: throw Exception("Authorization Header is null")
        } catch (e: ParseException) {
            logger.warn(e.message)
            context.challenge(CHALLENGE, AuthenticationFailedCause.NoCredentials) { challenge, call ->
                call.respond(UnauthorizedResponse(bearerAuthChallenge,basicAuthChallenge))
                challenge.complete()
            }
            return
        }

        val header = (authHeader as HttpAuthHeader.Single)
        when(authHeader.authScheme) {
            // Google authenticating with OAuth 2
            AuthScheme.Bearer -> {
                try {
                    val googleToken = googleAuth.authenticateToken(header.blob) ?: throw InvalidCredentialsException()
                    val user = authService.readUserByEmail(googleToken.email)
                        ?: let {
                            logger.warn("User does not exists.")
                            authService.createUser(NewUser(
                                email = googleToken.email,
                                names = googleToken.names?:"",
                                lastname = googleToken.lastname?:"",
                                isAdmin = false,
                                isStudent = false,
                                password = null,
                                photoUrl = googleToken.photoUrl,
                                phone = null,
                                adminPin = null,
                                course = null,
                                school = null
                            ))
                        }
                    if (asAdmin && !user.isAdmin) throw InvalidCredentialsException()

                    val principal = authenticate(context.call, user) ?: throw InvalidCredentialsException()

                    context.principal(principal)

                } catch (e: InvalidCredentialsException) {
                    context.challenge(CHALLENGE, AuthenticationFailedCause.InvalidCredentials) { challenge, call ->
                        call.respond(unauthorizedBearerResponse)
//                        call.respond("InvalidCredentials "+e.localizedMessage)
                        challenge.complete()
                    }
                } catch (e: Exception) {
                    context.challenge(CHALLENGE, AuthenticationFailedCause.Error(e.localizedMessage)) { challenge, call ->
                        call.respond(unauthorizedBearerResponse)
//                        call.respond("Exception "+e.localizedMessage)
                        challenge.complete()
                    }
                }
            }
            // Password authentication
            AuthScheme.Basic -> {
                try {
                    val credDecoded: ByteArray = Base64.getDecoder().decode(header.blob)
                    val credentials = String(credDecoded, Charsets.UTF_8).split(":".toRegex(), limit = 2)
                    val email = credentials[0]
                    val password = credentials[1]

                    val user = authService.validatePassword(email, password)

                    if (asAdmin && !user.isAdmin) throw InvalidCredentialsException()

                    val principal = authenticate(context.call, user) ?: throw InvalidCredentialsException()
                    context.principal(principal)

                } catch (e: InvalidCredentialsException) {
                    context.challenge(CHALLENGE, AuthenticationFailedCause.InvalidCredentials) { challenge, call ->
                        call.respond(unauthorizedBasicResponse)
//                        call.respond("InvalidCredentials "+e.localizedMessage)
                        challenge.complete()
                    }
                } catch (e: Exception) {
                    context.challenge(CHALLENGE, AuthenticationFailedCause.Error(e.localizedMessage)) { challenge, call ->
                        call.respond(unauthorizedBasicResponse)
//                        call.respond("Exception "+e.localizedMessage)
                        challenge.complete()
                    }
                }
            }
            else -> {
                context.challenge(CHALLENGE, AuthenticationFailedCause.NoCredentials) { challenge, call ->
                    call.respond(UnauthorizedResponse(bearerAuthChallenge,basicAuthChallenge))
                    challenge.complete()
                }
            }
        }
    }

    private val bearerAuthChallenge = HttpAuthHeader.bearerAuthChallenge(AuthScheme.Bearer, realm)
    private val unauthorizedBearerResponse = UnauthorizedResponse(bearerAuthChallenge)
    private val basicAuthChallenge = HttpAuthHeader.basicAuthChallenge(realm, Charsets.UTF_8)
    private val unauthorizedBasicResponse = UnauthorizedResponse(basicAuthChallenge)

    internal class InvalidCredentialsException(message: String? = null): Exception(message)
}

