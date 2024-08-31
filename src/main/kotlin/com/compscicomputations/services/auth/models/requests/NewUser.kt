package com.compscicomputations.services.auth.models.requests

import com.compscicomputations.utils.Image
import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val email: String,
    val otp: String,
    val password: String?,
    val names: String?,
    val lastname: String?,
    val image: Image?,
)