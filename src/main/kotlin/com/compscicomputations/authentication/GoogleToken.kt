package com.compscicomputations.authentication

import io.ktor.server.auth.Principal

data class GoogleToken(
    val email: String,
    val names: String?,
    val lastname: String?,
    val photoUrl: String?,
): Principal
