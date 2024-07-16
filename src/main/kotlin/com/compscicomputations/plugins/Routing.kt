package com.compscicomputations.plugins

import com.compscicomputations.authentication.google.GoogleToken
import com.compscicomputations.authentication.authenticateUserOrAdmin
import com.compscicomputations.firebase.authenticateAdmin
import com.compscicomputations.routing.authRouting
import com.compscicomputations.services.auth.models.Admins
import com.compscicomputations.services.auth.models.Users
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get as getA
import io.ktor.server.resources.get

fun Application.configureRouting() {

    routing {
        getA {
            call.respondText("Hello CompSci Computations API!")
        }
        staticResources("/.well-known", "well-known")

        authRouting()

//        post("upload") {
//            val file = File("uploads/ktor_logo.png")
//            call.receiveChannel().copyAndClose(file.writeChannel())
////        call.respondText("A file is uploaded"+String(file.readBytes()))
//            call.respondFile(file)
//        }
        authenticateUserOrAdmin {
            getA("/test/auth") {
                val principal = call.principal<GoogleToken>()
                call.respondText("Hello\n$principal")
            }
        }


//        authenticate("google") {
//            get("login") {
//                call.respondRedirect("/callback")
//            }
//
//            get("/callback") {
//                val principal: OAuthAccessTokenResponse.OAuth2 = call.authentication.principal() ?: return@get
////                call.sessions.set(UserSession(principal?.accessToken.toString()))
////                call.respondRedirect("/test/auth")
//
//                call.respondText("Hello \n" +
//                        "accessToken: ${principal?.accessToken}\n" +
//                        "refreshToken: ${principal?.refreshToken}\n" +
//                        "expiresIn: ${principal?.expiresIn}\n" +
//                        "tokenType: ${principal?.tokenType}\n" +
//                        "state: ${principal?.state}\n" +
//                        "id_token: ${principal?.extraParameters?.get("id_token")}\n" +
//                        "scope: ${principal?.extraParameters?.get("scope")}\n" +
//                        ""
//                )
//
////                call.respondRedirect {
////                    path("/test/auth")
////                    headers {
////                        append(HttpHeaders.Authorization, "Bearer ${principal.extraParameters["id_token"]}")
////                    }
////                }
//            }
//        }
    }

    // Redirects
    val resourcesFormat = plugin(Resources).resourcesFormat
    routing {
        authenticateAdmin {
            get<Admins.Me> { call.respondRedirect(href(resourcesFormat, Users.Me())) }
            get<Admins.Uid> { call.respondRedirect(href(resourcesFormat, Users.Uid(uid = it.uid))) }

        }
    }
}