package com.compscicomputations.routing

import com.compscicomputations.plugins.Feedback
import com.compscicomputations.plugins.Onboarding
import com.compscicomputations.plugins.authenticateAdmin
import com.compscicomputations.services.publik.PublicService
import com.compscicomputations.services.publik.models.requests.NewFeedback
import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.utils.EMAIL_VERIFICATION_EMAIL
import com.compscicomputations.utils.FEEDBACK_EMAIL
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.mail.DefaultAuthenticator
import org.koin.ktor.ext.inject


fun Routing.publicRouting() {
    val publicService by inject<PublicService>()

    authenticateAdmin {
        post<Onboarding.Items> {
            try {
                val request = call.receive<NewOnboardingItem>()
                publicService.createOnboardingItem(request)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
            }
        }
    }

    get<Onboarding.Items> {
        try {
            val ids = call.receiveNullable<IntArray?>()
            val items = publicService.getOnboardingItems(ids)
            call.respondNullable(HttpStatusCode.OK, items)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    delete<Onboarding.Items.Id> {
        try {
            publicService.deleteOnboardingItem(it.id)
            call.respond(HttpStatusCode.OK)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }



    post<Feedback> {
        try {
            val request = call.receive<NewFeedback>()
            publicService.createFeedback(request)
            val emailAddress = System.getenv("EMAIL_ADDR")
            val emailPassword = System.getenv("EMAIL_PASS")
            org.apache.commons.mail.HtmlEmail().apply {
                hostName = "smtp.gmail.com"
                setSmtpPort(465)
                setAuthenticator(DefaultAuthenticator(emailAddress, emailPassword))
                isSSLOnConnect = true
                setFrom("sams.mndebele@gmail.com") //Todo: set the user email
                subject = request.subject
                setHtmlMsg(
                    FEEDBACK_EMAIL
                        .replaceFirst("{{message}}", request.message)
                        .let {
                            if (request.suggestion != null)
                                it.replaceFirst("{{suggestion}}", request.suggestion)
                            else it
                        }

                )
                addTo(emailAddress)
            }.send()
            call.respond(HttpStatusCode.OK)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    get<Feedback> {
        try {
            val feedbacks = publicService.getFeedbacks()
            call.respond(HttpStatusCode.OK, feedbacks)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }
    get<Feedback.Id> {
        try {
            val feedback = publicService.getFeedback(it.id)
            if (feedback == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.OK, feedback)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

}