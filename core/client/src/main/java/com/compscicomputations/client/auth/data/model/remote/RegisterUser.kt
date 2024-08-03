package com.compscicomputations.client.auth.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUser(
    val email: String,
    val password: String?,
    @SerialName("display_name")
    val displayName: String?,
)
