package com.compscicomputations.services.auth.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUser(
    val email: String,
    val password: String?,
    val names: String?,
    val lastname: String?,
)