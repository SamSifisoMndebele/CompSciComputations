package com.compscicomputations.core.ktor_client.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val email: String,
    val password: String?,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("photo_url")
    val photoUrl: String?,
    val phone: String?,
    val usertype: Usertype,
    @SerialName("admin_code")
    val adminCode: String? = null,
)
