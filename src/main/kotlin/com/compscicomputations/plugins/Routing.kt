package com.compscicomputations.plugins

import com.compscicomputations.routers.authRouter
import com.compscicomputations.routers.otherRouter
import com.compscicomputations.services.auth.models.CreateUserRequest
import com.compscicomputations.services.auth.resources.CreateUserRes
import com.compscicomputations.services.auth.resources.UsersRes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.resources.post
import io.ktor.server.resources.get
import io.ktor.server.resources.put
import io.ktor.server.resources.delete

fun Application.configureRouting() {
    routing {
//        get {
//            call.respondText("Hello CompSci Computations API!")
//        }
        authRouter()
        otherRouter()

        post<CreateUserRes> { createUserRes ->
            // Create a user
            try {
                val user = call.receive<CreateUserRequest>()
//            authService.createUser(userRequest)
                call.respond(HttpStatusCode.Created, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
            }
        }
        get<UsersRes> { userRes ->
            // Get all users
            call.respondText("List of UsersRes: $userRes")
            userRes.email
        }
        get<UsersRes.Uid> { userRes ->
            // Show a user with uid ${userRes.uid} ...
            call.respondText("An article with uid ${userRes.uid}", status = HttpStatusCode.OK)
            userRes.parent.email
        }
        put<UsersRes.Uid> { userRes ->
            // Update a user
            call.respondText("An article with uid ${userRes.uid} updated", status = HttpStatusCode.OK)
        }
        delete<UsersRes.Uid> { userRes ->
            // Delete a user
            call.respondText("An article with uid ${userRes.uid} deleted", status = HttpStatusCode.OK)
        }

    }
}