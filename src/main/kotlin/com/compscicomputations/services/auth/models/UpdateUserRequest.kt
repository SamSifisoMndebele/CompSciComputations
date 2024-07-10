package com.compscicomputations.services.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val email: String? = null,
    val password: String? = null,
    @SerialName("display_name")
    val displayName: String? = null,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val phone: String? = null,
    val usertype: Usertype? = null,
    @SerialName("admin_code")
    val adminCode: String? = null,
    @SerialName("is_email_verified")
    val isEmailVerified: Boolean? = null
)
