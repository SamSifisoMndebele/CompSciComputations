package com.compscicomputations.services.auth.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPassword(
    val email: String,
    @SerialName("new_password")
    val newPassword: String,
    val otp: String?,
    val password: String?,
)
