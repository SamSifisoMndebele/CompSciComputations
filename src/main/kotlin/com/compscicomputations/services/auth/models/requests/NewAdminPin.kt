package com.compscicomputations.services.auth.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class NewAdminPin(
    val email: String,
    val pin: String,
)
