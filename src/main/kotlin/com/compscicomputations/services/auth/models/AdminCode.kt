package com.compscicomputations.services.auth.models

import java.time.Instant

data class AdminCode(
    val email: String,
    val hashCode: String,
    val createdAt: Instant,
    val validUntil: Instant,
)
