package com.compscicomputations.core.ktor_client.auth.models

import kotlinx.coroutines.flow.Flow

data class AuthUser(
    val uid: String,
    val email: String,
    val displayName: String,
    val phoneNumber: String?,
    val photoUrl: String?,
    val isEmailVerified: Boolean,
    val usertypeFlow: Flow<Usertype>,
)
