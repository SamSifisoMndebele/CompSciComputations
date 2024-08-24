package com.compscicomputations.services.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class OTP(
    val id: Int,
    val email: String,
    val otp: String,
    val validUntil: String
)
