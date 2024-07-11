package com.compscicomputations.plugins

import com.compscicomputations.routing.authRouting
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File

fun Application.configureRouting() {
    routing {
        get {
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
}