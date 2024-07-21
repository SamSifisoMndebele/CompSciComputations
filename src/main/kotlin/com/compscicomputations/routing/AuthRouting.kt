package com.compscicomputations.routing

import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.plugins.authenticateAdmin
import com.compscicomputations.plugins.authenticateGoogle
import com.compscicomputations.plugins.validateAdminPinLimit
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.*
import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.RegisterUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.auth.models.response.AuthFile
import com.compscicomputations.utils.OKOrNotFound
import io.ktor.http.*
import io.ktor.http.content.*
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
import java.io.File
import java.io.FileNotFoundException
import java.util.*

fun Routing.authRouting() {
    val authService by inject<AuthService>()

    authenticateGoogle {
        // Create a user or create if not exists
        get<Users.Google> {
            try {
                val user = call.principal<User>()!!
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }
    }

    // Create a user
    post<Users> {
        try {
            val userRequest = call.receive<RegisterUser>()
            val user = authService.createUser(userRequest)
            call.respond(HttpStatusCode.Created, user)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    //Upload user image
    post<Users.Id.Images> {
        try {
            val multipartData = call.receiveMultipart()
            val fileSize = call.request.header(HttpHeaders.ContentLength)
            val fileId = authService.uploadFile(multipartData, fileSize.toString())
            call.respond(HttpStatusCode.Created, fileId)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    //Get user image by id
    get<Users.Id.Images> {
        try {
            val directory = File("file-storage/users/${it.parent.id}/images")
            val file = File(directory, "profile_image.png")
            if (!file.exists()) throw FileNotFoundException("The user image does not exists.")
            if (!file.canRead()) throw Exception("The user image is corrupted.")
            call.respondBytes(file.readBytes(), contentType = ContentType.Image.PNG)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
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