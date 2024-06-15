package com.compscicomputations.core.database.remote.dto

import com.compscicomputations.core.database.model.Feedback
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("uid") val uid: String,
) {
    val asFeedback get() = Feedback(id, title, content, imageUrl, uid)
}
