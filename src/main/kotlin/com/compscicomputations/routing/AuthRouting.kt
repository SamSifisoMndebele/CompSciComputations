package com.compscicomputations.routing

import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.plugins.Users
import com.compscicomputations.plugins.authenticateUser
import com.compscicomputations.services.auth.models.requests.RegisterUser
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.utils.RESET_PASSWORD_EMAIL
import com.compscicomputations.utils.asString
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.mail.DefaultAuthenticator
import org.koin.ktor.ext.inject

fun Routing.authRouting() {
    val authService by inject<AuthService>()

    // Create a user
    post<Users> {
        try {
            val userRequest = call.receive<RegisterUser>()
            val user = authService.registerUser(userRequest)
            call.respond(HttpStatusCode.Created, user)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    // Upload user image by id
    post<Users.Id.Image> {
        try {
            val multipartData = call.receiveMultipart()
            authService.updateUserImage(it.parent.id, multipartData)
            call.respond(HttpStatusCode.OK)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    authenticateUser {
        // Read a user
        get<Users.Me> {
            try {
                val user = call.principal<User>()!!
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }
    }


    get<Users.Email.PasswordReset> {
        try {
            val passwordOTP = authService.getPasswordResetOTP(it.parent.email)

            val email = org.apache.commons.mail.HtmlEmail()
            email.hostName = "smtp.gmail.com"
            email.setSmtpPort(465)
            val emailAddress = System.getenv("EMAIL_ADDR")
            val emailPassword = System.getenv("EMAIL_PASS")
            email.setAuthenticator(DefaultAuthenticator(emailAddress, emailPassword))
            email.isSSLOnConnect = true
            email.setFrom(emailAddress)
            email.subject = "Reset your password for CompSci Computations."
            email.setHtmlMsg(
                RESET_PASSWORD_EMAIL
                    .replaceFirst("{{email_to}}", passwordOTP.email)
                    .replaceFirst("{{otp}}", passwordOTP.otp)
                    .replaceFirst("{{expiration_time}}", passwordOTP.validUntil.substring(11, 19))
            )
            email.addTo(passwordOTP.email)
            email.send()

            call.respond(HttpStatusCode.OK, passwordOTP)
//            call.respond(HttpStatusCode.OK, "OTP sent to ${passwordOTP.email}.")

        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }




















   /* authenticateAdmin {
        // Read an admin user
        get<Admins.Me> {
            try {
                val user = call.principal<User>()!!
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }
    }







    authenticate {

        // Update myself as a user
        put<Users.Me> {
            try {
                val userRequest = call.receive<UpdateUser>()
                val firebase = call.principal<User>()!!

                val user = authService.updateUser(firebase.id, userRequest)
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }

        // Delete myself as a user
        delete<Users.Me> {
            try {
                val firebase = call.principal<User>()!!

                authService.deleteUser(firebase.id)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }
    }

    authenticateAdmin {
        // Read all database users
        get<Users> {
            try {
                val users = authService.readUsers(it.limit)

                call.respond(users.OKOrNotFound, users)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }

        // Read a user with id
        get<Users.Id> { id ->
            try {
                val user = authService.readUser(id.id)
                call.respondNullable(user.OKOrNotFound, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }

        // Update a user with id
        put<Users.Id> { id ->
            try {
                val userRequest = call.receive<UpdateUser>()

                val user = authService.updateUser(id.id, userRequest)
                call.respond(user.OKOrNotFound, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }

        // Delete user with id
        delete<Users.Id> { id ->
            try {
                authService.deleteUser(id.id)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }


        // Create admin code
        post<Admins.Pins> {
            try {
                val request = call.receive<NewAdminPin>()
                authService.createAdminPin(request)
                call.respond(HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }

        // Validate admin code by email
        rateLimit(validateAdminPinLimit) {
            get<Admins.Pins.Email> {
                try {
//                    val requestsLeft = call.response.headers["X-RateLimit-Remaining"]
//                    val email = call.parameters["email"]
                    val pin = call.receive<String>()
                    val row = authService.validateAdminPin(it.email, pin)
                    call.respondNullable(row.OKOrNotFound, row)
                } catch (e: Exception) {
                    call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
                }
            }
        }

        // Update admin code by email address
        put<Admins.Pins.Email> {
            try {
                TODO("Not yet implemented.")
//                val user = authService.Codes
//                call.respond(user.OKOrNotFound, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }

        // Delete admin code by email address
        delete<Admins.Pins.Email> {
            try {
                TODO("Not yet implemented.")
//                val user = authService.Codes
//                call.respond(user.OKOrNotFound, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }
    }*/
}