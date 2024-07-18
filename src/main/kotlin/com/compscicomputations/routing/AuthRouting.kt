package com.compscicomputations.routing

import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.authentication.authenticateAdmin
import com.compscicomputations.plugins.validateAdminPinLimit
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.*
import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private inline val Any?.OKOrNotFound: HttpStatusCode
    get() = if (this == null) HttpStatusCode.NotFound else HttpStatusCode.OK
private inline val List<Any>?.OKOrNotFound: HttpStatusCode
    get() = if (isNullOrEmpty()) HttpStatusCode.NotFound else HttpStatusCode.OK

fun Routing.authRouting() {
    val authService by inject<AuthService>()

    // Create a user
    post<Users> {
        try {
            val userRequest = call.receive<NewUser>()
            authService.createUser(userRequest)
            call.respond(HttpStatusCode.Created)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    authenticate("google") {
        get<Users.Me.Google> {
            try {
                val user = call.principal<User>()!!
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }
    }

    authenticateAdmin {
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
        // Read myself as a user
        get<Users.Me> {
            try {
                val user = call.principal<User>()!!
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }

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
    }
}