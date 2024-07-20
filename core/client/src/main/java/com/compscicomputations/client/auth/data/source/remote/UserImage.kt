package com.compscicomputations.client.auth.data.source.remote

import kotlinx.serialization.Serializable

@Serializable
data class UserImage(
    val name: String,
    val description: String?,
    val path: String,
    val size: String?,
)
