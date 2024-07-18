package com.compscicomputations.authentication.google

import io.ktor.server.auth.Principal

data class GoogleToken(
    val email: String,
    val names: String?,
    val lastname: String?,
    val photoUrl: String?,
): Principal
