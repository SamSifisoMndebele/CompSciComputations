package com.compscicomputations.routing

import com.compscicomputations.firebase.FirebasePrincipal
import com.compscicomputations.firebase.authenticateAdmin
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.*
import com.compscicomputations.services.auth.models.requests.NewAdminCode
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.User
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private inline val User?.OKOrNotFound: HttpStatusCode
    get() = if (this == null) HttpStatusCode.NotFound else HttpStatusCode.OK

fun Routing.authRouting() {
    val authService by inject<AuthService>()

    // Create a user
    post<Users> {
        try {
            val userRequest = call.receive<NewUser>()
            authService.createUser(userRequest)
            call.respond(HttpStatusCode.Created)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
        }
    }

    authenticate {
        // Read myself as a user
        get<Users.Me> {
            try {
                val firebase = call.principal<FirebasePrincipal>() ?:
                return@get call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                val user = authService.readUser(firebase.uid) ?: call.respond(HttpStatusCode.NotFound)
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        post<Users.Me.LastSeen> {
            try {
                val firebase = call.principal<FirebasePrincipal>()!!

                authService.updateLastSeen(firebase.uid)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Update myself as a user
        put<Users.Me> {
            try {
                val userRequest = call.receive<UpdateUser>()
                val firebase = call.principal<FirebasePrincipal>() ?:
                return@put call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                val user = authService.updateUser(firebase.uid, userRequest)
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Delete myself as a user
        delete<Users.Me> {
            try {
                val firebase = call.principal<FirebasePrincipal>() ?:
                return@delete call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                authService.deleteUser(firebase.uid)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }
    }

    authenticateAdmin {
        // Read all database users
        get<Users> {
            try {
                val users = authService.readUsers(it.limit)

                call.respond(HttpStatusCode.OK, users)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Read a user with uid
        get<Users.Uid> { uid ->
            try {
                val user = authService.readUser(uid.uid)
                call.respondNullable(user.OKOrNotFound, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Update a user with uid
        put<Users.Uid> { uid ->
            try {
                val userRequest = call.receive<UpdateUser>()

                val user = authService.updateUser(uid.uid, userRequest)
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Delete user with uid
        delete<Users.Uid> { uid ->
            try {
                authService.deleteUser(uid.uid)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }


        // Create admin code
        post<Admins.Codes> {
            try {
                val request = call.receive<NewAdminCode>()
                authService.createAdminCode(request)
                call.respond(HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Read admin code by email address
        get<Admins.Codes.Email> {
            TODO("Not yet implemented.")
        }

        // Update admin code by email address
        put<Admins.Codes.Email> {
            TODO("Not yet implemented.")
        }

        // Delete admin code
        delete<Admins.Codes.Email> {
            TODO("Not yet implemented.")
        }
    }
}