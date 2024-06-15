package com.compscicomputations.core.database.model

import androidx.room.ColumnInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserMetadata(
    @ColumnInfo(name = "display_name")
    val displayName: String? = null,
    @ColumnInfo(name = "photo_url")
    val photoUrl: String? = null,
    @SerialName("created_at")
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    @SerialName("updated_at")
    @ColumnInfo(name = "updated_at")
    val updatedAt: String? = null,
    @SerialName("admin_since")
    @ColumnInfo(name = "admin_since")
    val adminSince: String? = null,
    @SerialName("last_seen_at")
    @ColumnInfo(name = "last_seen_at")
    val lastSeenAt: String? = null,
    @SerialName("banned_until")
    @ColumnInfo(name = "banned_until")
    val bannedUntil: String? = null,
    @SerialName("deleted_at")
    @ColumnInfo(name = "deleted_at")
    val deletedAt: String? = null,
    @SerialName("emailVerified")
    @ColumnInfo(name = "email_verified")
    val emailVerified: Boolean = false,
)
