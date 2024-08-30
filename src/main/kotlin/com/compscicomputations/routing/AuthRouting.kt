package com.compscicomputations.routing

import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.plugins.Users
import com.compscicomputations.plugins.authenticateUser
import com.compscicomputations.services.auth.models.requests.NewPassword
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.utils.EMAIL_VERIFICATION_EMAIL
import com.compscicomputations.utils.RESET_PASSWORD_EMAIL
import com.compscicomputations.utils.sendEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.resources.get
import io.ktor.server.resources.delete
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.authRouting() {
    val authService by inject<AuthService>()

    // Create a user
    post<Users> {
        try {
            val userInfo = call.receive<NewUser>()
            val user = authService.registerUser(userInfo)
            call.respond(HttpStatusCode.Created, user)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    // Update a user
    put<Users.Me> {
        try {
            val userInfo = call.receive<UpdateUser>()
            val user = authService.updateUser(userInfo)
            call.respond(HttpStatusCode.OK, user)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    authenticateUser {
        // Read a user
        get<Users.Me> {
            try {
                val user = call.principal<User>() ?:
                return@get call.respond(HttpStatusCode.Unauthorized, "User credentials are incorrect.")

                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }

        // Delete a user
        delete<Users.Me> {
            try {
                val user = call.principal<User>() ?:
                return@delete call.respond(HttpStatusCode.Unauthorized, "User credentials are incorrect.")

                authService.deleteUser(user.email)
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }
    }

    get<Users.PasswordReset.Email> {
        try {
            val passwordOTP = authService.getOTP(it.email)
            sendEmail(
                subject = "Reset your password for CompSci Computations.",
                htmlMsg = RESET_PASSWORD_EMAIL
                    .replaceFirst("{{email_to}}", passwordOTP.email)
                    .replaceFirst("{{otp}}", passwordOTP.otp)
                    .replaceFirst("{{expiration_time}}", passwordOTP.validUntil),
                emailTo = passwordOTP.email
            )

            call.respond(HttpStatusCode.OK, "OTP sent to ${passwordOTP.email}.")
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    get<Users.Email.Otp> {
        try {
            val passwordOTP = authService.getOTP(it.parent.email)
            sendEmail(
                subject = "Reset your password for CompSci Computations.",
                htmlMsg = EMAIL_VERIFICATION_EMAIL
                    .replaceFirst("{{email_to}}", passwordOTP.email)
                    .replaceFirst("{{otp}}", passwordOTP.otp)
                    .replaceFirst("{{expiration_time}}", passwordOTP.validUntil),
                emailTo = passwordOTP.email
            )

            call.respond(HttpStatusCode.OK, "OTP sent to ${passwordOTP.email}.")
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    post<Users.PasswordReset> {
        try {
            val newPassword = call.receive<NewPassword>()
            authService.passwordReset(newPassword)
            call.respond(HttpStatusCode.OK, "Your password reset was successful!")
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    get<Users.Delete> {
        call.respondText("Delete your account on app.")
    }
















    /*






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
    }

    authenticateAdmin {

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