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
//    @SerialName("created_at")
//    val created_at: String,
//    @SerialName("updated_at")
//    val updated_at: String?,
    @SerialName("is_email_verified")
    val isEmailVerified: Boolean,
    @SerialName("is_student")
    val isStudent: Boolean,
    val university: String?,
    val school: String?,
    val course: String?,
    @SerialName("is_admin")
    val isAdmin: Boolean,
    @SerialName("is_super_admin")
    val isSuperAdmin: Boolean,
//    @SerialName("admin_since")
//    val admin_since: String?,
//    @SerialName("admin_assigned_by")
//    val admin_assigned_by: String?,
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
            university = university,
            school = school,
            course = course,
        )
}