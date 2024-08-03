package com.compscicomputations.client.auth.data.model

import android.graphics.Bitmap

data class User(
    val id: Int,
    val email: String,
    val displayName: String,
    val imageBitmap: Bitmap?,
    val phone: String?,
    val isAdmin: Boolean,
    val isStudent: Boolean,
    val isEmailVerified: Boolean,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (email != other.email) return false
        if (displayName != other.displayName) return false
        if (imageBitmap != other.imageBitmap) return false
        if (phone != other.phone) return false
        if (isAdmin != other.isAdmin) return false
        if (isStudent != other.isStudent) return false
        if (isEmailVerified != other.isEmailVerified) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + email.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + (imageBitmap?.hashCode() ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + isAdmin.hashCode()
        result = 31 * result + isStudent.hashCode()
        result = 31 * result + isEmailVerified.hashCode()
        return result
    }
}
