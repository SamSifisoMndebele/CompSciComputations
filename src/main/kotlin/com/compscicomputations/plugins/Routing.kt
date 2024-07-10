package com.compscicomputations.plugins

import com.compscicomputations.routers.authRouter
import com.compscicomputations.routers.otherRouter
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello CompSci Computations API!")
        }
        authRouter()
        otherRouter()
    }
}