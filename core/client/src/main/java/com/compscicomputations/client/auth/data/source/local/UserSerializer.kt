package com.compscicomputations.client.auth.data.source.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.compscicomputations.client.auth.data.source.remote.RemoteUser
import com.compscicomputations.client.auth.models.User
import com.compscicomputations.core.client.UserLocal
import java.io.InputStream
import java.io.OutputStream

internal object UserSerializer : Serializer<UserLocal> {
    override val defaultValue: UserLocal = UserLocal.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserLocal {
        try {
            return UserLocal.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserLocal, output: OutputStream) = t.writeTo(output)

    val UserLocal.asUser
        get() = User(
            id = id,
            email = email,
            names = names,
            lastname = lastname,
            photoUrl = photoUrl,
            phone = phone,
            isAdmin = isAdmin,
            isStudent = isStudent,
            createdAt = createdAt,
            updatedAt = updatedAt
        )

    val UserLocal.toRemote
        get() = RemoteUser(
            id = id,
            email = email,
            names = names,
            lastname = lastname,
            photoUrl = photoUrl,
            phone = phone,
            isAdmin = isAdmin,
            isStudent = isStudent,
            createdAt = createdAt,
            updatedAt = updatedAt
        )

}