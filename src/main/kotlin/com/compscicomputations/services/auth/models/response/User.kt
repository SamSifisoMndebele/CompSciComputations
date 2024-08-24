package com.compscicomputations.services.auth.models.response

import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val email: String,
    val names: String,
    val lastname: String,
    val image: ByteArray?,
    val phone: String?,
    @SerialName("is_admin")
    val isAdmin: Boolean,
    @SerialName("is_student")
    val isStudent: Boolean,
    @SerialName("is_email_verified")
    val isEmailVerified: Boolean,
) : Principal
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (email != other.email) return false
        if (names != other.names) return false
        if (lastname != other.lastname) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (phone != other.phone) return false
        if (isAdmin != other.isAdmin) return false
        if (isStudent != other.isStudent) return false
        if (isEmailVerified != other.isEmailVerified) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + names.hashCode()
        result = 31 * result + lastname.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + isAdmin.hashCode()
        result = 31 * result + isStudent.hashCode()
        result = 31 * result + isEmailVerified.hashCode()
        return result
    }
}

//    id uuid primary key default ext.gen_random_uuid(),
//    email text unique not null,
//    password text default null,
//    display_name text not null,
//    image_bytes bytea default null,
//    phone text default null,
//    is_admin boolean default false not null,
//    is_student boolean default false not null,
//    is_email_verified boolean default false not null,
//    created_at timestamptz default (now() at time zone 'SAST') not null,
//    updated_at timestamptz default null
