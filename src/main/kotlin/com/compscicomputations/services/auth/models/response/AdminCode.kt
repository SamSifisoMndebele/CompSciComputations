package com.compscicomputations.services.auth.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminCode(
    val id: Int,
    val email: String,
    @SerialName("hash_code")
    val hashCode: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("valid_until")
    val validUntil: String,
)
