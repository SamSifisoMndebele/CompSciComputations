package com.compscicomputations.routing

import com.compscicomputations.plugins.Feedback
import com.compscicomputations.plugins.Onboarding
import com.compscicomputations.plugins.authenticateAdmin
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.publik.PublicService
import com.compscicomputations.services.publik.models.requests.NewFeedback
import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.utils.FEEDBACK_EMAIL
import com.compscicomputations.utils.Image.Companion.isNotNullOrEmpty
import com.compscicomputations.utils.sendEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File


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
            val id = publicService.createFeedback(request)
            sendEmail(
                subject = "Feedback $id",
                htmlMsg = FEEDBACK_EMAIL
                    .replaceFirst("{{email_from}}", request.userEmail ?: "Anonymous")
                    .replaceFirst("{{subject}}", request.subject)
                    .replaceFirst("{{message}}", request.message)
                    .replaceFirst("{{suggestion_html}}",
                        if (request.suggestion.isNotBlank()) "<hr><p><b>Suggestion:</b></p><p>${request.suggestion}</p>" else ""
                    ),
                emailTo = null,
                emailFrom = request.userEmail,
                imageBytes = request.image?.bytes,
            )
            call.respond(HttpStatusCode.OK)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }
    get<Feedback.Image> {
        try {
            val image = publicService.getFeedbackImage(it.id) ?: return@get call.respond(HttpStatusCode.NoContent)
            call.respondBytes(image, ContentType.Image.PNG, HttpStatusCode.OK)
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
    get<Feedback.Email> {
        try {
            val feedbacks = publicService.getMyFeedbacks(it.email)
            if (feedbacks.isEmpty()) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.OK, feedbacks)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

}