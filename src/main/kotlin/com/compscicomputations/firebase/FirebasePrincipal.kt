package com.compscicomputations.firebase

import io.ktor.server.auth.Principal

data class FirebasePrincipal(
    val uid: String,
    val isAdmin: Boolean
): Principal
