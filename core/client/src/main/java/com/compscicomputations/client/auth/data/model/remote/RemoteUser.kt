package com.compscicomputations.client.auth.data.model.remote

import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.utils.Image
import com.compscicomputations.client.utils.asBitmap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteUser(
    val id: String,
    val email: String,
    val names: String,
    val lastname: String,
    val image: Image?,
    val phone: String?,
    @SerialName("is_admin")
    val isAdmin: Boolean,
    @SerialName("is_student")
    val isStudent: Boolean,
    @SerialName("is_email_verified")
    val isEmailVerified: Boolean,
) {
    val asUser: User
        get() = User(
            id = id,
            email = email,
            names = names,
            lastname = lastname,
            imageBitmap = image?.bytes?.asBitmap,
            phone = phone,
            isAdmin = isAdmin,
            isStudent = isStudent,
            isEmailVerified = isEmailVerified,
            university = null,
            school = null,
            course = null,
        )
}