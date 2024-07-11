package com.compscicomputations.routers

import com.compscicomputations.firebase.FirebaseUser
import com.compscicomputations.firebase.authenticateAdmin
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.exceptions.NoSuchUserException
import com.compscicomputations.services.auth.models.*
import com.compscicomputations.utils.asString
import com.compscicomputations.utils.parameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private fun createUserResponse(firebaseUser: FirebaseUser, user: User) = UserResponse(
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


fun Route.authRouter() {
    val authService by inject<AuthService>()


    route("/user") {

        authenticate {

            // Update user
            put {
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

            // Delete user
            delete {
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
    }

    route("/admin") {
        route("/code") {
            // Add admin code
            post {
                try {
                    val request = call.receive<CreateAdminCodeRequest>()
                    authService.createAdminCode(request)
                    call.respond(HttpStatusCode.Created)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
                }
            }
            authenticateAdmin {

                // Update admin code by email
                put("/{email}") {
                    TODO("Not yet implemented.")
                }

                // Delete admin code by email
                delete("/{email}") {
                    TODO("Not yet implemented.")
                }
            }
        }

        authenticateAdmin {

            // Update any user by uid
            put("/{uid}") {
                try {
                    val uid = call.parameter("uid") ?: return@put
                    val userRequest = call.receive<UpdateUserRequest>()
                    var firebaseUser = call.principal<FirebaseUser>() ?:
                    return@put call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                    if (uid != firebaseUser.uid) firebaseUser = authService.readFirebaseUser(uid)

                    val user = authService.updateUser(firebaseUser, userRequest)
                    call.respond(HttpStatusCode.OK, user)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
                }
            }

            // Delete any user by uid
            delete("/{uid}") {
                try {
                    val uid = call.parameter("uid") ?: return@delete
                    call.principal<FirebaseUser>() ?:
                    return@delete call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                    authService.deleteUser(uid)
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
                }
            }
        }
    }
}