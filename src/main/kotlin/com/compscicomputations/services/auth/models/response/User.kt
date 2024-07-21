package com.compscicomputations.services.auth.models.response

import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("photo_url")
    val photoUrl: String?,
    val phone: String?,
    @SerialName("is_admin")
    val isAdmin: Boolean,
    @SerialName("is_student")
    val isStudent: Boolean,
    @SerialName("is_email_verified")
    val isEmailVerified: Boolean,
    @SerialName("temp_password")
    val tempPassword: String? = null,
) : Principal

//    id uuid primary key default ext.gen_random_uuid(),
//    email text unique not null,
//    password_hash text not null,
//    display_name text not null,
//    photo_url text default null,
//    phone text default null,
//    is_admin boolean default false not null,
//    is_student boolean default false not null,
//    is_email_verified boolean default false not null,
//    created_at timestamptz default (now() at time zone 'SAST') not null,
//    updated_at timestamptz default null
