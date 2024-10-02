package com.compscicomputations.client.publik.data.model.remote

import com.compscicomputations.client.utils.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteFeedback(
    val id: Int,
    val subject: String,
    val message: String,
    val suggestion: String = "",
    val image: Image? = null,
    @SerialName("user_email")
    val userEmail: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("response_message")
    val responseMessage: String? = null,
    @SerialName("response_image")
    val responseImage: Image? = null,
    @SerialName("responded_at")
    val respondedAt: String? = null,
    @SerialName("responded_by_email")
    val respondedByEmail: String? = null,
)