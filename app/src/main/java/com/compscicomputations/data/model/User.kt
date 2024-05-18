package com.compscicomputations.data.model

import android.net.Uri
import com.compscicomputations.ui.auth.UserType
import java.util.Date

data class User(
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: Uri?,
    val userType: UserType,
    val createdAt: Date,
    val lastSeenAt: Date,
)
