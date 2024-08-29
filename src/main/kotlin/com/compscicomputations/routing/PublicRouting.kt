package com.compscicomputations.routing

import com.compscicomputations.plugins.Feedback
import com.compscicomputations.plugins.Onboarding
import com.compscicomputations.plugins.authenticateAdmin
import com.compscicomputations.plugins.authenticateUser
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.services.publik.PublicService
import com.compscicomputations.services.publik.models.requests.NewFeedback
import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.utils.EMAIL_VERIFICATION_EMAIL
import com.compscicomputations.utils.FEEDBACK_EMAIL
import com.compscicomputations.utils.sendEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.commons.mail.DefaultAuthenticator
import org.koin.ktor.ext.inject
import javax.mail.internet.InternetAddress


fun Routing.publicRouting() {
    val publicService by inject<PublicService>()
    val authService by inject<AuthService>()

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
            val userEmail = request.userId?.let { id -> authService.getEmail(id) }
            sendEmail(
                subject = "Feedback",
                htmlMsg = FEEDBACK_EMAIL
                    .replaceFirst("{{email_from}}", userEmail ?: "Anonymous")
                    .replaceFirst("{{subject}}", request.subject)
                    .replaceFirst("{{message}}", request.message)
                    .replaceFirst("{{suggestion_html}}",
                        if (request.suggestion != null) "<hr><h3>Suggestion:</h3><p>${request.suggestion}</p>" else ""
                    ),
                emailTo = null,
                emailFrom = userEmail
            )
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