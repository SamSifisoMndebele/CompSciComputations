package com.compscicomputations.services.auth.models.requests

import com.compscicomputations.utils.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUser(
    val id: String,
    val names: String,
    val lastname: String,
    val image: Image?,
    val phone: String?,
    @SerialName("is_email_verified")
    val isEmailVerified: Boolean,

    @SerialName("is_student")
    val isStudent: Boolean,
    val university: String?,
    val school: String?,
    val course: String?,
)
