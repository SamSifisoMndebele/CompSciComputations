package com.compscicomputations.client.auth.models

import com.compscicomputations.client.auth.data.source.remote.RemoteUser
import com.compscicomputations.core.client.LocalUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okio.ByteString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val imageId: Int?,
    val imageBytes: ByteArray? = null,
    val phone: String?,
    val isAdmin: Boolean,
    val isStudent: Boolean,
    val isEmailVerified: Boolean,
) {
    internal val asLocalUser: LocalUser
        get() = LocalUser.newBuilder()
            .setId(id)
            .setEmail(email)
            .setDisplayName(displayName)
            .setImageId(imageId ?: 0)
            .setPhone(phone ?: "")
            .setIsAdmin(isAdmin)
            .setIsStudent(isStudent)
            .setIsEmailVerified(isEmailVerified)
            .build()

    internal val asRemoteUser
        get() = RemoteUser(
            id = id,
            email = email,
            displayName= displayName,
            imageId = imageId,
            phone = phone,
            isAdmin = isAdmin,
            isStudent = isStudent,
            isEmailVerified = isEmailVerified
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (email != other.email) return false
        if (displayName != other.displayName) return false
        if (imageId != other.imageId) return false
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
        result = 31 * result + (imageId ?: 0)
        result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + isAdmin.hashCode()
        result = 31 * result + isStudent.hashCode()
        result = 31 * result + isEmailVerified.hashCode()
        return result
    }
}
