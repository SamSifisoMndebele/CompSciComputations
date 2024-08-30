package com.compscicomputations.client.auth.data.model.remote

import com.compscicomputations.client.utils.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUser(
    val names: String? = null,
    val lastname: String? = null,
    val image: Image? = null,
    val phone: String? = null,

    @SerialName("is_student")
    val isStudent: Boolean = false,
    val university: String? = null,
    val school: String? = null,
    val course: String? = null,
)