package com.compscicomputations.services.auth.models.requests

import com.compscicomputations.utils.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUser(
    val email: String? = null,
    val password: String? = null,
    val names: String? = null,
    val lastname: String? = null,
    val image: Image? = null,
    val phone: String? = null,

    @SerialName("is_student")
    val isStudent: Boolean? = null,
    val university: String? = null,
    val school: String? = null,
    val course: String? = null,
)
