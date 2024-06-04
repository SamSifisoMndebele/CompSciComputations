package com.compscicomputations.core.database.remote.dto

import android.net.Uri
import com.compscicomputations.core.database.remote.model.User
import com.compscicomputations.core.database.remote.model.UserType.Companion.asUserType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("uid") val uid: String,
    @SerialName("display_name") val displayName: String?,
    @SerialName("email") val email: String?,
    @SerialName("phone") val phone: String?,
    @SerialName("photo_url") val photoUrl: String?,
    @SerialName("user_type") val userType: String,
) {
    val asUser get() = User(uid, displayName, email, phone, Uri.parse(photoUrl), userType.asUserType)
}