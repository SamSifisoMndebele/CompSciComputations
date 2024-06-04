package com.compscicomputations.core.database.remote.model

import android.net.Uri

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: Uri?,
    val anonymous: Boolean,
    val emailVerified: Boolean
)
