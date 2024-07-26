package com.compscicomputations.services.auth.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class NewPassword(
    val email: String,
    val otp: String,
    val password: String,
)
