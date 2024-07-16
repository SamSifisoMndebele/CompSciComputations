package com.compscicomputations.core.ktor_client.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val email: String,
    val names: String,
    val lastname: String,
    @SerialName("is_admin")
    val isAdmin: Boolean = false,
    @SerialName("is_student")
    val isStudent: Boolean = false,
    val password: String? = null,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val phone: String? = null,
    @SerialName("admin_pin")
    val adminPin: String? = null,
    val course: String? = null,
    val school: String? = null,
)
