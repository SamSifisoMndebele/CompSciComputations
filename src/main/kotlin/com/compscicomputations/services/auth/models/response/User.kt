package com.compscicomputations.services.auth.models.response

import com.compscicomputations.services.auth.models.Usertype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val email: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("photo_url")
    val photoUrl: String?,
    val phone: String?,
    val usertype: Usertype,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String?,
    @SerialName("last_seen_at")
    val lastSeenAt: String?,
)