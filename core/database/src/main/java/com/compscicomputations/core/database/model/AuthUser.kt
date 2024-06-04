package com.compscicomputations.core.database.model

import android.net.Uri
import java.util.Date

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: Uri?,
    val anonymous: Boolean,
    val emailVerified: Boolean
)
