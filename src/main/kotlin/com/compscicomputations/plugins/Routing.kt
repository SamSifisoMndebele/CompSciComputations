package com.compscicomputations.plugins

import com.compscicomputations.firebase.authenticateAdmin
import com.compscicomputations.routing.authRouting
import com.compscicomputations.services.auth.models.Admins
import com.compscicomputations.services.auth.models.Users
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File
import io.ktor.server.routing.get as getA
import io.ktor.server.resources.get

fun Application.configureRouting() {

    routing {
        getA {
            call.respondText("Hello CompSci Computations API!")
        }

        authRouting()

        post("upload") {
            val file = File("uploads/ktor_logo.png")
            call.receiveChannel().copyAndClose(file.writeChannel())
//        call.respondText("A file is uploaded"+String(file.readBytes()))
            call.respondFile(file)
        }
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