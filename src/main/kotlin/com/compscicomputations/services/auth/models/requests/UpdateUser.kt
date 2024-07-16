package com.compscicomputations.services.auth.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUser(
    val email: String? = null,
    val names: String? = null,
    val lastname: String? = null,
    val password: String? = null,
    @SerialName("is_admin")
    val isAdmin: Boolean? = null,
    @SerialName("is_student")
    val isStudent: Boolean? = null,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val phone: String? = null,
    @SerialName("admin_pin")
    val adminPin: String? = null,
)