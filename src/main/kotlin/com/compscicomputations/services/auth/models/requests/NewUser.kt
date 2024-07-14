package com.compscicomputations.services.auth.models.requests

import com.compscicomputations.services.auth.models.Usertype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val email: String,
    val password: String? = null,
    @SerialName("display_name")
    val displayName: String?,
    @SerialName("photo_url")
    val photoUrl: String?,
    val phone: String?,
    val usertype: Usertype,
    @SerialName("admin_code")
    val adminCode: String? = null,
)
