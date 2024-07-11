package com.compscicomputations.firebase

import io.ktor.server.auth.*

data class FirebaseUser(
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String?,
    val isEmailVerified: Boolean,
    val claims: Map<String, Any?>
): Principal
