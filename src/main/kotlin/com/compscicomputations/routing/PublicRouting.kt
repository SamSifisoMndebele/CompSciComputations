package com.compscicomputations.routing

import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.authentication.authenticateAdmin
import com.compscicomputations.plugins.validateAdminPinLimit
import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.models.*
import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.publik.PublicService
import com.compscicomputations.services.publik.models.Onboarding
import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.services.publik.models.requests.UpdateOnboardingItem
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject



fun Routing.publicRouting() {
    val publicService by inject<PublicService>()

    post<Onboarding.Items> {
        try {
            val request = call.receive<NewOnboardingItem>()
            publicService.createOnboardingItem(request)
            call.respond(HttpStatusCode.Created)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    get<Onboarding.Items> {
        try {
            val items = publicService.getOnboardingItems()
            call.respond(HttpStatusCode.OK, items)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    get<Onboarding.Items.Id> {
        try {
            val item = publicService.getOnboardingItem(it.id)
            call.respondNullable(HttpStatusCode.OK, item)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    put<Onboarding.Items.Id> {
        try {
            val request = call.receive<UpdateOnboardingItem>()
            publicService.updateOnboardingItem(it.id, request)
            call.respond(HttpStatusCode.OK)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

    delete<Onboarding.Items.Id> {
        try {
            publicService.deleteOnboardingItems(it.id)
            call.respond(HttpStatusCode.OK)
        } catch (e: Exception) {
            call.respondNullable(HttpStatusCode.ExpectationFailed, e.message)
        }
    }

}