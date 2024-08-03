package com.compscicomputations.client.auth.data.model.remote

import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.utils.asBitmap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteUser(
    val id: Int,
    val email: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("image_bytes")
    val imageBytes: ByteArray?,
    val phone: String?,
    @SerialName("is_admin")
    val isAdmin: Boolean,
    @SerialName("is_student")
    val isStudent: Boolean,
    @SerialName("is_email_verified")
    val isEmailVerified: Boolean,
) {
    val asUser: User
        get() = User(
            id = id,
            email = email,
            displayName = displayName,
            imageBitmap = imageBytes?.asBitmap,
            phone = phone,
            isAdmin = isAdmin,
            isStudent = isStudent,
            isEmailVerified = isEmailVerified,
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RemoteUser

        if (id != other.id) return false
        if (email != other.email) return false
        if (displayName != other.displayName) return false
        if (imageBytes != null) {
            if (other.imageBytes == null) return false
            if (!imageBytes.contentEquals(other.imageBytes)) return false
        } else if (other.imageBytes != null) return false
        if (phone != other.phone) return false
        if (isAdmin != other.isAdmin) return false
        if (isStudent != other.isStudent) return false
        if (isEmailVerified != other.isEmailVerified) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + isAdmin.hashCode()
        result = 31 * result + isStudent.hashCode()
        result = 31 * result + isEmailVerified.hashCode()
        return result
    }
}