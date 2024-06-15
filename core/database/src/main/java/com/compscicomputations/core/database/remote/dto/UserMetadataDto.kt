package com.compscicomputations.core.database.remote.dto

import com.compscicomputations.core.database.asDate
import com.compscicomputations.core.database.model.UserMetadata
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class



UserMetadataDto(
    @SerialName("uid") val uid: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("last_sign_in_at") val lastSignInAt: String,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("last_seen_at") val lastSeenAt: String?,
    @SerialName("banned_until") val bannedUntil: String?,
    @SerialName("deleted_at") val deletedAt: String?,
) {
//    val asUserMetadata get() = UserMetadata(uid, createdAt.asDate, lastSignInAt.asDate,
//        updatedAt?.asDate, lastSeenAt?.asDate, bannedUntil?.asDate, deletedAt?.asDate)
}
