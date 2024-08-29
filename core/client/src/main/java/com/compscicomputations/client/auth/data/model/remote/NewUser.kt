package com.compscicomputations.client.auth.data.model.remote

import com.compscicomputations.client.utils.Image
import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val email: String,
    val password: String?,
    val names: String,
    val lastname: String,
    val image: Image?,
)
