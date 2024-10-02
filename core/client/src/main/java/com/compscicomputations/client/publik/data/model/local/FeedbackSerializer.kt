package com.compscicomputations.client.publik.data.model.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.compscicomputations.client.publik.data.model.Feedback
import com.compscicomputations.client.utils.parseDate
import com.compscicomputations.client.utils.asBitmap
import java.io.InputStream
import java.io.OutputStream

internal object FeedbackSerializer : Serializer<FeedbackLocal> {
    override val defaultValue: FeedbackLocal = FeedbackLocal.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): FeedbackLocal {
        try {
            return FeedbackLocal.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: FeedbackLocal, output: OutputStream) = t.writeTo(output)

    val FeedbackLocal.asFeedback
        get() = Feedback(
            id = id,
            subject = subject,
            message = message,
            suggestion = suggestion,
            imageBitmap = imageBytes?.asBitmap,
            userEmail = userEmail,
            createdAt = createdAt.parseDate!!,
            responseMessage = responseMessage,
            responseImageBitmap = responseImageBytes?.asBitmap,
            respondedAt = respondedAt.parseDate,
            respondedByEmail = respondedByEmail,
        )
}