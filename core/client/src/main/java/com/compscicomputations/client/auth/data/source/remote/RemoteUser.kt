package com.compscicomputations.client.auth.data.source.remote

import com.compscicomputations.client.auth.models.User
import com.compscicomputations.core.client.LocalUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteUser(
    val id: String,
    val email: String,
    val names: String,
    val lastname: String,
    @SerialName("photo_url")
    val photoUrl: String?,
    val phone: String?,
    @SerialName("is_admin")
    val isAdmin: Boolean,
    @SerialName("is_student")
    val isStudent: Boolean,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String?,
    @SerialName("temp_password")
    val googlePassword: String? = null,
) {
    companion object {
        fun List<RemoteUser>.toExternal() = map(RemoteUser::asUser)
        fun List<RemoteUser>.toLocal() = map { it.asUser }
    }

    val asUser: User
        get() = User(
            id = id,
            email = email,
            names = names,
            lastname = lastname,
            photoUrl = photoUrl,
            phone = phone,
            isAdmin = isAdmin,
            isStudent = isStudent,
            createdAt = createdAt,
            updatedAt = updatedAt
        )

    val asLocalUser: LocalUser
        get() = LocalUser.newBuilder()
            .setId(id)
            .setEmail(email)
            .setNames(names)
            .setLastname(lastname)
            .setPhotoUrl(photoUrl?:"")
            .setPhone(phone?:"")
            .setIsAdmin(isAdmin)
            .setIsStudent(isStudent)
            .setCreatedAt(createdAt)
            .setUpdatedAt(updatedAt?:"")
            .build()
}