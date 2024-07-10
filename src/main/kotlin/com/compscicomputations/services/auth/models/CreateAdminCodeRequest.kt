package com.compscicomputations.services.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAdminCodeRequest(
    val email: String,
    @SerialName("hash_code")
    val hashCode: String,
)
