package com.compscicomputations.services.publik.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewFeedback(
    val subject: String,
    val message: String,
    val suggestion: String? = null,
    val image: ByteArray? = null,
    @SerialName("user_id")
    val userId: Int? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewFeedback

        if (subject != other.subject) return false
        if (message != other.message) return false
        if (suggestion != other.suggestion) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = subject.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + (suggestion?.hashCode() ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + (userId ?: 0)
        return result
    }
}