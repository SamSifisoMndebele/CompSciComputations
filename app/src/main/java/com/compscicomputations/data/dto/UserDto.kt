package com.compscicomputations.data.dto

import android.net.Uri
import com.compscicomputations.data.model.User
import com.compscicomputations.ui.auth.UserType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        Uri.parse(photoUrl), UserType.valueOf(userType), createdAt.toDate(), lastSeenAt.toDate())

    companion object {
        private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        private fun String.toDate(): Date = df.parse(this.replace("T", " "))!!
        fun Date.toDateString(): String = df.format(this)
    }

}
