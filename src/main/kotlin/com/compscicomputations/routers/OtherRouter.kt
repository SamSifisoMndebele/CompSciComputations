package com.compscicomputations.routers

import com.compscicomputations.services.other.SubscriptionService
import com.compscicomputations.services.other.models.Subscription
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.koin.ktor.ext.inject
import java.io.File

fun Route.otherRouter() {
    val subscriptionService by inject<SubscriptionService>()

    post("subscribe") {
        try {
            val subscription = call.receive<Subscription>()

            subscriptionService.subscribe(subscription)
            call.respond(HttpStatusCode.OK, "Subscribed successfully.")
        } catch (e: Exception) {
            val message = e.localizedMessage.toString()
            if (message.contains("subscriptions_email_unique") )
                call.respond(HttpStatusCode.Conflict, "Email subscription already exists.")
            else
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
        }
    }
    authenticate {
        get("/subscriptions") {
            try {
                val subscriptions = subscriptionService.getSubscriptions() ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "No subscriptions available yet."
                )

                call.respond(HttpStatusCode.OK, subscriptions)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
//                if (e.message.toString().contains("subscriptions_email_unique")) {
//                    call.respond(HttpStatusCode.Conflict, "Subscription for this email exists.")
//                } else {
//                    call.respond(HttpStatusCode.ExpectationFailed, e.localizedMessage)
//                }
            }
        }
    }


    post("/upload") {
        val file = File("uploads/ktor_logo.png")
        call.receiveChannel().copyAndClose(file.writeChannel())
//        call.respondText("A file is uploaded"+String(file.readBytes()))
        call.respondFile(file)
    }
}