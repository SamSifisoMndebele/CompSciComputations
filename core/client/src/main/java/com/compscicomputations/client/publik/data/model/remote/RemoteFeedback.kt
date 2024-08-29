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
)