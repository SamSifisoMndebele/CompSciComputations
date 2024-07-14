package com.compscicomputations.services.auth.models.response

import com.compscicomputations.services.auth.models.Usertype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String?,
    val phone: String?,
    val usertype: Usertype,
    val createdAt: String,
    val updatedAt: String?,
    val lastSeenAt: String?,
)