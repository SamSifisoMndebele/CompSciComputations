package com.compscicomputations.client.auth.data.model.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.utils.asBitmap
import java.io.InputStream
import java.io.OutputStream

internal object UserSerializer : Serializer<LocalUser> {
    override val defaultValue: LocalUser = LocalUser.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LocalUser {
        try {
            return LocalUser.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: LocalUser, output: OutputStream) = t.writeTo(output)

    val LocalUser.asUser
        get() = User(
            id = id,
            email = email,
            names = names,
            lastname = lastname,
            imageBitmap = imageBytes?.asBitmap,
            phone = phone,
            isAdmin = isAdmin,
            isStudent = isStudent,
            isEmailVerified = isEmailVerified,
            university = university,
            school = school,
            course = course
        )
}