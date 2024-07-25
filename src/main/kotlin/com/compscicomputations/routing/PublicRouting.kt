package com.compscicomputations.routing

import com.compscicomputations.plugins.Onboarding
import com.compscicomputations.plugins.authenticateAdmin
import com.compscicomputations.services.publik.PublicService
import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
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

}