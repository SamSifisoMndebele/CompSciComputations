package com.compscicomputations.services.publik.models.requests

import com.compscicomputations.utils.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewFeedback(
    val subject: String,
    val message: String,
    val suggestion: String = "",
    val image: Image? = null,
    @SerialName("user_email")
    val userEmail: String? = null
)