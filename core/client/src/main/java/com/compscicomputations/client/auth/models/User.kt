package com.compscicomputations.client.auth.models

import com.compscicomputations.client.auth.data.source.remote.RemoteUser
import com.compscicomputations.core.client.UserLocal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class User(
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
) {
    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US)
        fun List<User>.toLocal() = map(User::toLocal)
        fun List<User>.toRemote() = map(User::toRemote)
    }
    @SerialName("display_name")
    val displayName: String
        get() = "$names $lastname".trim()

    fun createdAt(): Date = dateFormat.parse(createdAt.replace("T", " "))!!
    fun updatedAt(): Date? = updatedAt?.replace("T", " ")?.let { dateFormat.parse(it) }


    val toLocal: UserLocal
        get() = UserLocal.newBuilder()
            .setId(id)
            .setEmail(email)
            .setNames(names)
            .setLastname(lastname)
            .setPhotoUrl(photoUrl)
            .setPhone(phone)
            .setIsAdmin(isAdmin)
            .setIsStudent(isStudent)
            .setCreatedAt(createdAt)
            .setUpdatedAt(updatedAt)
            .build()

    val toRemote
        get() = RemoteUser(
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
}
