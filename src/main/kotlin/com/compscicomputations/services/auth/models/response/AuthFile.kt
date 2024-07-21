package com.compscicomputations.services.auth.models.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthFile(
    val name: String,
    val description: String?,
    val data: ByteArray,
    val size: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AuthFile

        if (name != other.name) return false
        if (description != other.description) return false
        if (!data.contentEquals(other.data)) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + data.contentHashCode()
        result = 31 * result + size.hashCode()
        return result
    }
}

//    id integer generated always as identity primary key,
//    name text not null,
//    description text,
//    data bytea not null,
//    size text not null,
//    created_at timestamptz default (now() at time zone 'SAST') not null
