package com.compscicomputations.services.other.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    val email: String,
    val suggestion: String? = null,
    @SerialName("subscribed_at")
    val subscribedAt: String = ""
)
