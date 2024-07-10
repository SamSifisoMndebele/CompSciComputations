package com.compscicomputations.services.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val email: String,
    val password: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val phone: String? = null,
    val usertype: Usertype = Usertype.STUDENT,
    @SerialName("admin_code")
    val adminCode: String? = null
) {
    val isAdmin get() = usertype == Usertype.ADMIN
}
