package com.compscicomputations.client.auth.data.model.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.compscicomputations.client.auth.data.model.Student
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.utils.asBitmap
import java.io.InputStream
import java.io.OutputStream

internal object StudentSerializer : Serializer<LocalStudent> {
    override val defaultValue: LocalStudent = LocalStudent.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LocalStudent {
        try {
            return LocalStudent.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: LocalStudent, output: OutputStream) = t.writeTo(output)

    val LocalStudent.asUser
        get() = Student(
            id = id,
            university = university,
            school = school,
            course = course,
        )
}