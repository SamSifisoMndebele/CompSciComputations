package com.compscicomputations.client.auth.data.source.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.compscicomputations.core.client.UserCredentials
import java.io.InputStream
import java.io.OutputStream

internal object CredentialsSerializer : Serializer<UserCredentials> {
    override val defaultValue: UserCredentials = UserCredentials.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserCredentials {
        try {
            return UserCredentials.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserCredentials, output: OutputStream) = t.writeTo(output)
}