package com.compscicomputations.core.database.model

import android.net.Uri
import java.util.Date

data class User(
    val uid: String,
    val displayName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val photoUrl: Uri? = null,
    val userType: UserType,
)
