package com.compscicomputations.core.database.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto2(
    @SerialName("uid") val uid: String,
    @SerialName("email") val email: String?,
    @SerialName("display_name") val displayName: String?,
    @SerialName("phone") val phone: String?,
    @SerialName("photo_url") val photoUrl: String?,
    @SerialName("usertype") val usertype: String,
    @SerialName("is_admin") val isAdmin: String,
    @SerialName("is_email_verified") val isEmailVerified: String,
    @SerialName("metadata") val metadata: String?,
) {
//    val asUser get() = User(uid, displayName, email, phone, photoUrl, usertype.asUserType)
}