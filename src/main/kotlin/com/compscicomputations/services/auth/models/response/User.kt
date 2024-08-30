package com.compscicomputations.services.auth.models.response

import com.compscicomputations.utils.Image
import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
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
) : Principal
