package com.compscicomputations.client.auth.data.model

import android.graphics.Bitmap
import com.compscicomputations.client.auth.data.source.local.LocalUser
import com.compscicomputations.client.auth.data.source.remote.RemoteUser
import com.compscicomputations.client.utils.asByteArray
import com.compscicomputations.client.utils.asByteString

data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val imageBitmap: Bitmap?,
    val phone: String?,
    val isAdmin: Boolean,
    val isStudent: Boolean,
    val isEmailVerified: Boolean,
) {
    internal fun asLocalUser(): LocalUser {
         val builder = LocalUser.newBuilder()
            .setId(id)
            .setEmail(email)
            .setDisplayName(displayName)
            .setIsAdmin(isAdmin)
            .setIsStudent(isStudent)
            .setIsEmailVerified(isEmailVerified)
        imageBitmap?.let { builder.setImageBytes(it.asByteString) }
        phone?.let { builder.setPhone(it) }

        return builder.build()
    }

    internal val asRemoteUser
        get() = RemoteUser(
            id = id,
            email = email,
            displayName= displayName,
            phone = phone,
            imageBytes = imageBitmap?.asByteArray,
            isAdmin = isAdmin,
            isStudent = isStudent,
            isEmailVerified = isEmailVerified
        )
}
