package com.compscicomputations.services.auth.models.requests

import com.compscicomputations.utils.Image
import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val email: String,
    val names: String,
    val lastname: String,
    val image: Image?,
    val password: String,
    val otp: String,
)