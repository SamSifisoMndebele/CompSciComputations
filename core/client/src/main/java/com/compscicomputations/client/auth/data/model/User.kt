package com.compscicomputations.client.auth.data.model

import android.graphics.Bitmap

data class User(
    val id: String,
    val email: String,
    val names: String,
    val lastname: String,
    val imageBitmap: Bitmap?,
    val phone: String?,
    val isAdmin: Boolean,
    val isStudent: Boolean,
    val isEmailVerified: Boolean,
) {
    val displayName: String
        get() = "$names $lastname".trim()
}