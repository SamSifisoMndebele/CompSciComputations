package com.compscicomputations.plugins

import com.compscicomputations.firebase.FirebaseUser
import com.compscicomputations.firebase.authenticateAdmin
import com.compscicomputations.routers.authRouter
import com.compscicomputations.routers.otherRouter
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.exceptions.NoSuchUserException
import com.compscicomputations.services.auth.models.CreateUserRequest
import com.compscicomputations.services.auth.models.User
import com.compscicomputations.services.auth.models.UserResponse
import com.compscicomputations.services.auth.models.Usertype
import com.compscicomputations.services.auth.resources.Users
import com.compscicomputations.utils.asString
import com.compscicomputations.utils.parameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.resources.post
import io.ktor.server.resources.get
import io.ktor.server.resources.put
import io.ktor.server.resources.delete
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

fun Routing.authRouting() {
    val authService by inject<AuthService>()

    post<Users> {
        // Create a user
        try {
            val userRequest = call.receive<CreateUserRequest>()
            authService.createUser(userRequest)
            call.respond(HttpStatusCode.Created, userRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
        }
    }

    authenticate {
        get<Users.Me> {
            // Read myself as a user
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
    }

    authenticateAdmin {
        get<Users> {
            // Read all users
            try {
                call.principal<FirebaseUser>() ?:
                return@get call.respond(HttpStatusCode.Unauthorized, "Authentication failed")

                val users = authService.readUsers()

                call.respond(HttpStatusCode.OK, users.map {
                    UserResponse(
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

        get<Users.Uid> { uid ->
            // Read a user with uid ${uid.uid}
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
    }
}

fun Application.configureRouting() {
    routing {
//        get {
//            call.respondText("Hello CompSci Computations API!")
//        }
//        authRouter()
        authRouting()
        otherRouter()



//        put<Users> {
//            //TODO: Update myself as user
//            call.respondText("Me as user updated: $it", status = HttpStatusCode.OK)
//        }
//        put<Users.Uid> { uid ->
//            //TODO: Update a user with uid ${user.uid}
//            call.respondText("User with uid ${uid.uid} updated", status = HttpStatusCode.OK)
//        }
//        delete<Users.Uid> { uid ->
//            //TODO: Delete user with uid
//            call.respondText("An article with uid ${uid.uid} deleted", status = HttpStatusCode.OK)
//        }

    }
}