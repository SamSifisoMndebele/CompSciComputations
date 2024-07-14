package com.compscicomputations.services.auth.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminCode(
    val id: Int,
    val email: String,
    val hashCode: String,
    val createdAt: String,
    val validUntil: String,
)
