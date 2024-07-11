package com.compscicomputations.routing

import com.compscicomputations.firebase.FirebaseUser
import com.compscicomputations.firebase.authenticateAdmin
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.exceptions.NoSuchUserException
import com.compscicomputations.services.auth.models.*
import com.compscicomputations.services.auth.models.Admins
import com.compscicomputations.services.auth.models.Users
import com.compscicomputations.services.auth.models.requests.CreateAdminCodeRequest
import com.compscicomputations.services.auth.models.requests.CreateUserRequest
import com.compscicomputations.services.auth.models.requests.UpdateUserRequest
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.utils.asString
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import io.ktor.server.resources.post
import io.ktor.server.resources.get
import io.ktor.server.resources.put
import io.ktor.server.resources.delete

private fun createUserResponse(firebaseUser: FirebaseUser, user: User) = User(
    uid = firebaseUser.uid,
    email = firebaseUser.email,
    displayName = firebaseUser.displayName,
    photoUrl = firebaseUser.photoUrl,
    isEmailVerified = firebaseUser.isEmailVerified,
    phone = user.phone,
    usertype = user.usertype,
    createdAt = user.createdAt.asString,
    updatedAt = user.updatedAt?.asString,
    lastSeenAt = user.lastSeenAt?.asString,
    bannedUntil = user.bannedUntil?.asString,
    deletedAt = user.deletedAt?.asString,
)

fun Routing.authRouting() {
    val authService by inject<AuthService>()

    // Create a user
    post<Users> {
        try {
            val userRequest = call.receive<CreateUserRequest>()
            authService.createUser(userRequest)
            call.respond(HttpStatusCode.Created, userRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
        }
    }

    authenticate {
        // Read myself as a user
        get<Users.Me> {
            try {
                val firebaseUser = call.principal<FirebaseUser>() ?:
                return@get call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                val user = authService.readUser(firebaseUser.uid)
                val userResponse = createUserResponse(firebaseUser, user)
                call.respond(HttpStatusCode.OK, userResponse)
            } catch (e: NoSuchUserException) {
                call.respond(HttpStatusCode.NotFound, e.localizedMessage)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Update myself as a user
        put<Users.Me> {
            try {
                val userRequest = call.receive<UpdateUserRequest>()
                val firebaseUser = call.principal<FirebaseUser>() ?:
                return@put call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                val user = authService.updateUser(firebaseUser, userRequest)
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Delete myself as a user
        delete<Users.Me> {
            try {
                val firebaseUser = call.principal<FirebaseUser>() ?:
                return@delete call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                authService.deleteUser(firebaseUser.uid)
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
                call.principal<FirebaseUser>() ?:
                return@get call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                val users = authService.readUsers()

                call.respond(HttpStatusCode.OK, users.map {
                    User(
                        uid = it.uid,
                        email = it.email,
                        displayName = "",
                        photoUrl = null,
                        isEmailVerified = false,
                        phone = it.phone,
                        usertype = it.usertype,
                        createdAt = it.createdAt.asString,
                        updatedAt = it.updatedAt?.asString,
                        lastSeenAt = it.lastSeenAt?.asString,
                        bannedUntil = it.bannedUntil?.asString,
                        deletedAt = it.deletedAt?.asString,
                    )
                })
            } catch (e: NoSuchUserException) {
                call.respond(HttpStatusCode.NotFound, e.localizedMessage)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Read a user with uid
        get<Users.Uid> { uid ->
            try {
                val firebaseUser = call.principal<FirebaseUser>() ?:
                return@get call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                val user = authService.readUser(uid.uid)
                val userResponse = if (user.uid == firebaseUser.uid) createUserResponse(firebaseUser, user)
                else createUserResponse(authService.readFirebaseUser(uid.uid), user)

                call.respond(HttpStatusCode.OK, userResponse)
            } catch (e: NoSuchUserException) {
                call.respond(HttpStatusCode.NotFound, e.localizedMessage)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Update a user with uid
        put<Users.Uid> { uid ->
            try {
                val userRequest = call.receive<UpdateUserRequest>()
                var firebaseUser = call.principal<FirebaseUser>() ?:
                return@put call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                if (uid.uid != firebaseUser.uid) firebaseUser = authService.readFirebaseUser(uid.uid)

                val user = authService.updateUser(firebaseUser, userRequest)
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Delete user with uid
        delete<Users.Uid> { uid ->
            try {
                call.principal<FirebaseUser>() ?:
                return@delete call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                authService.deleteUser(uid.uid)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }


        // Create admin code
        post<Admins.Codes> {
            try {
                val request = call.receive<CreateAdminCodeRequest>()
                authService.createAdminCode(request)
                call.respond(HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }

        // Read admin code
        get<Admins.Codes> {
            TODO("Not yet implemented.")
        }

        // Update admin code
        put<Admins.Codes> {
            TODO("Not yet implemented.")
        }

        // Delete admin code
        delete<Admins.Codes> {
            TODO("Not yet implemented.")
        }
    }
}