package com.compscicomputations.services.auth.models.other

import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminUser(
    val id: String,
    val email: String,
    val names: String,
    val lastname: String,
    @SerialName("photo_url")
    val photoUrl: String?,
    val phone: String?,
    @SerialName("is_admin")
    val isAdmin: Boolean,
    @SerialName("is_student")
    val isStudent: Boolean,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String?,
    @SerialName("pin_id")
    val pinId: Int,
    @SerialName("admin_since")
    val adminSince: String,
) : Principal

//    id uuid primary key default ext.gen_random_uuid() not null,
//    email text unique not null,
//    names text not null,
//    lastname text not null,
//password_hash text default null,
//    photo_url text default null,
//    phone text default null,
//is_admin boolean default false not null,
//    is_student boolean default false not null,
//    created_at timestamptz default (now() at time zone 'SAST') not null,
//    updated_at timestamptz default null

//    id uuid primary key not null,
//    pin_id int not null,
//    admin_since timestamptz default (now() at time zone 'SAST') not null,
