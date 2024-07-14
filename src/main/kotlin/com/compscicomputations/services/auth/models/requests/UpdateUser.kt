package com.compscicomputations.services.auth.models.requests

import com.compscicomputations.services.auth.models.Usertype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUser(
    val email: String? = null,
    val password: String? = null,
    @SerialName("display_name")
    val displayName: String? = null,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val phone: String? = null,
    val usertype: Usertype? = null,
    @SerialName("admin_code")
    val adminCode: String? = null
)
