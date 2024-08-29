package com.compscicomputations.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

val validateAdminPinLimit = RateLimitName("validate_admin_pin")

fun Application.configureRateLimit() {
    install(RateLimit) {
        register(validateAdminPinLimit) {
            rateLimiter(limit = 3, refillPeriod = 1.minutes)
            requestKey { applicationCall ->
                applicationCall.parameters["email"]!!
            }
        }
    }
}