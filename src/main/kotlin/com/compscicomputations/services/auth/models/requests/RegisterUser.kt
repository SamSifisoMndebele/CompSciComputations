package com.compscicomputations.services.auth.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUser(
    val email: String,
    val password: String?,
    @SerialName("display_name")
    val displayName: String?,
    val image: ByteArray?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegisterUser

        if (email != other.email) return false
        if (password != other.password) return false
        if (displayName != other.displayName) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}
