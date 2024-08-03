package com.compscicomputations.client.publik.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteFeedback(
    val id: Int,
    val subject: String,
    val message: String,
    val suggestion: String?,
    @SerialName("image_bytes")
    val imageBytes: ByteArray?,
    @SerialName("user_id")
    val userId: Int?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RemoteFeedback

        if (id != other.id) return false
        if (subject != other.subject) return false
        if (message != other.message) return false
        if (suggestion != other.suggestion) return false
        if (imageBytes != null) {
            if (other.imageBytes == null) return false
            if (!imageBytes.contentEquals(other.imageBytes)) return false
        } else if (other.imageBytes != null) return false
        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + subject.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + (suggestion?.hashCode() ?: 0)
        result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
        result = 31 * result + (userId ?: 0)
        return result
    }
}