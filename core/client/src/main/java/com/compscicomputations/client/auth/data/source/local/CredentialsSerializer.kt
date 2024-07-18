package com.compscicomputations.client.auth.data.source.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.compscicomputations.core.client.LoginCredentials
import java.io.InputStream
import java.io.OutputStream

internal object CredentialsSerializer : Serializer<LoginCredentials> {
    override val defaultValue: LoginCredentials = LoginCredentials.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LoginCredentials {
        try {
            return LoginCredentials.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: LoginCredentials, output: OutputStream) = t.writeTo(output)
}