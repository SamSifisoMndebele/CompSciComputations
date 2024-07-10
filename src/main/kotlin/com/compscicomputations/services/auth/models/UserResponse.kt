package com.compscicomputations.services.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
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
)