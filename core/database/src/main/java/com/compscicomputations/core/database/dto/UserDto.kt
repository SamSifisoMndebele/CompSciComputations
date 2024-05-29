package com.compscicomputations.core.database.dto

import android.net.Uri
import com.compscicomputations.core.database.UserType
import com.compscicomputations.core.database.asDate
import com.compscicomputations.core.database.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("uid") val uid: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("email") val email: String,
    @SerialName("photo_url") val photoUrl: String?,
    @SerialName("user_type") val userType: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("last_seen_at") val lastSeenAt: String
) {
    val asUser get() = User(uid, displayName, email,
        Uri.parse(photoUrl), UserType.valueOf(userType), createdAt.asDate, lastSeenAt.asDate)
}