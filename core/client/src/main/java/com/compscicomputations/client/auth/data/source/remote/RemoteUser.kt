package com.compscicomputations.client.auth.data.source.remote

import com.compscicomputations.client.auth.models.User
import com.compscicomputations.core.client.LocalUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteUser(
    val id: String,
    val email: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("image_id")
    val imageId: Int?,
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
            imageId = imageId,
            phone = phone,
            isAdmin = isAdmin,
            isStudent = isStudent,
            isEmailVerified = isEmailVerified,
        )
}