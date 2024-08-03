package com.compscicomputations.client.publik.data.source.remote

import com.compscicomputations.client.publik.data.model.remote.NewFeedback
import com.compscicomputations.client.publik.data.model.remote.RemoteFeedback
import com.compscicomputations.client.publik.data.model.remote.RemoteOnboardingItem
import com.compscicomputations.client.utils.Feedback
import com.compscicomputations.client.utils.ktorRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.onUpload
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject

class PublicDataSource @Inject constructor(
    private val client: HttpClient
) {
    companion object {
        const val TAG = "AuthDataSource"

        class NotFoundException(message: String? = null): Exception(message)
        class ExpectationFailedException(message: String? = null): Exception(message)
    }

    internal suspend fun getFeedback(
        id: Int,
        onDownload: (bytesSent: Long, totalBytes: Long) -> Unit = {_,_ ->}
    ): RemoteFeedback = ktorRequest {
        val response = client.get(Feedback.Id(id = id)) {
            contentType(ContentType.Application.Json)
            onDownload { bytesSentTotal, contentLength ->
                println("onDownload: Received $bytesSentTotal bytes from $contentLength")
                onDownload(bytesSentTotal, contentLength.takeIf { it != 0L } ?: 1L)
            }
        }
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.NotFound -> throw NotFoundException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<RemoteFeedback>()
            else -> throw Exception(response.bodyAsText())
        }
    }
    internal suspend fun getFeedbacks(
        onDownload: (bytesReceived: Long, totalBytes: Long) -> Unit = {_,_ ->}
    ): List<RemoteFeedback> = ktorRequest {
        val response = client.get(Feedback()) {
            onDownload { bytesSentTotal, contentLength ->
                println("onDownload: Received $bytesSentTotal bytes from $contentLength")
                onDownload(bytesSentTotal, contentLength.takeIf { it != 0L } ?: 1L)
            }
        }
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<List<RemoteFeedback>>()
            else -> throw Exception(response.bodyAsText())
        }
    }

    internal suspend fun sendFeedback(
        feedback: NewFeedback,
        onUpload: (bytesSent: Long, totalBytes: Long) -> Unit = {_,_ ->}
    ): Unit = ktorRequest {
        val response = client.post(Feedback()) {
            contentType(ContentType.Application.Json)
            setBody(feedback)
            onUpload { bytesSentTotal, contentLength ->
                println("onUpload: Sent $bytesSentTotal bytes from $contentLength")
                onUpload(bytesSentTotal, contentLength.takeIf { it != 0L } ?: 1L)
            }
        }
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> {response.bodyAsText()}
            else -> throw Exception(response.bodyAsText())
        }
    }

}