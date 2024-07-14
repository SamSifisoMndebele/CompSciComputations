package com.compscicomputations.services.auth.models.requests

import com.compscicomputations.services.auth.models.Usertype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val email: String,
    val password: String? = null,
    val displayName: String?,
    val photoUrl: String?,
    val phone: String?,
    val usertype: Usertype,
    val adminCode: String? = null,
)
