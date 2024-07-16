package com.compscicomputations.services.auth.models.other

import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentUser(
    val id: String,
    val email: String,
    val names: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("password_hash")
    val passwordHash: String?,
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
    val course: String,
    val school: String,
) : Principal

//    id uuid primary key default ext.gen_random_uuid() not null,
//    email text unique not null,
//    names text not null,
//    last_name text not null,
//password_hash text default null,
//    photo_url text default null,
//    phone text default null,
//is_admin boolean default false not null,
//    is_student boolean default false not null,
//    created_at timestamptz default (now() at time zone 'SAST') not null,
//    updated_at timestamptz default null

//    id uuid primary key not null,
//    course text not null,
//    school text not null,