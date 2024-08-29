package com.compscicomputations.client.auth.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class NewPassword(
    val email: String,
    val otp: String,
    val password: String,
)