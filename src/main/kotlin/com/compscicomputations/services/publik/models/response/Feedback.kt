package com.compscicomputations.services.publik.models.response

import com.compscicomputations.utils.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class Feedback(
    val id: Int,
    val subject: String,
    val message: String,
    val suggestion: String = "",
    val image: Image? = null,
    @SerialName("user_email")
    val userEmail: String? = null,
    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("response_message")
    val responseMessage: String? = null,
    @SerialName("response_image")
    val responseImage: Image? = null,
    @SerialName("responded_at")
    val respondedAt: String? = null,
    @SerialName("responded_by_email")
    val respondedByEmail: String? = null,
)