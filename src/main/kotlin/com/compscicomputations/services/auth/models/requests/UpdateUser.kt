package com.compscicomputations.services.auth.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUser(
    val email: String? = null,
    val password: String? = null,
    val names: String? = null,
    val lastname: String? = null,
    @SerialName("is_admin")
    val isAdmin: Boolean? = null,
    @SerialName("is_student")
    val isStudent: Boolean? = null,
    val image: ByteArray? = null,
    val phone: String? = null,
    @SerialName("admin_pin")
    val adminPin: String? = null,
    val course: String? = null,
    val school: String? = null,
)
