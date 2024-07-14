package com.compscicomputations.services.auth.models.requests

import com.compscicomputations.services.auth.models.Usertype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUser(
    val email: String? = null,
    val password: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val phone: String? = null,
    val usertype: Usertype? = null,
    val adminCode: String? = null
)
