package com.compscicomputations.services.auth.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class NewAdminCode(
    val email: String,
    val code: String,
)
