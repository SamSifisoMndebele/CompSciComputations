package com.compscicomputations.core.ktor_client.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class User(
    val uid: String,
    val email: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("photo_url")
    val photoUrl: String?,
    @SerialName("is_email_verified")
    val isEmailVerified: Boolean,
    val phone: String?,
    val usertype: Usertype,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String?,
    @SerialName("last_seen_at")
    val lastSeenAt: String?,
    @SerialName("banned_until")
    val bannedUntil: String?,
    @SerialName("deleted_at")
    val deletedAt: String?,
) {
    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US)
    }
    fun createdAt(): Date = dateFormat.parse(createdAt.replace("T", " "))!!
    fun updatedAt(): Date? = updatedAt?.replace("T", " ")?.let { dateFormat.parse(it) }
    fun lastSeenAt(): Date? = lastSeenAt?.replace("T", " ")?.let { dateFormat.parse(it) }
    fun bannedUntil(): Date? = bannedUntil?.replace("T", " ")?.let { dateFormat.parse(it) }
    fun deletedAt(): Date? = deletedAt?.replace("T", " ")?.let { dateFormat.parse(it) }
}
