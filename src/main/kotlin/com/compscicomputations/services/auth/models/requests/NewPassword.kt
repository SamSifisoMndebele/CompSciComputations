package com.compscicomputations.services.auth.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class NewPassword(
    val email: String,
    val password: String,
    val otp: String? = null,
    val oldPassword: String? = null
)
