package com.compscicomputations.core.database

import android.content.ContentResolver
import android.net.Uri
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
inline val Long?.asDate: Date get() = if (this == null) Date() else Date(this)
val String.asDate: Date get() = dateFormat.parse(this.replace("T", " "))!!
val Date.asString: String get() = dateFormat.format(this)

private fun getBytes(inputStream: InputStream): ByteArray {
    val byteBuffer = ByteArrayOutputStream()
    val bufferSize = 1024
    val buffer = ByteArray(bufferSize)
    var len = 0
    while (inputStream.read(buffer).also { len = it } != -1) {
        byteBuffer.write(buffer, 0, len)
    }
    return byteBuffer.toByteArray()
}

//    if (imageUrl.host?.contains("supabase") == true) {
//        viewModel.onSaveProduct(image = byteArrayOf())
//    } else {
//        val image = uriToByteArray(contentResolver, imageUrl)
//        viewModel.onSaveProduct(image = image)
//    }

private fun uriToByteArray(contentResolver: ContentResolver, uri: Uri): ByteArray {
    if (uri == Uri.EMPTY) {
        return byteArrayOf()
    }
    val inputStream = contentResolver.openInputStream(uri)
    if (inputStream != null) {
        return getBytes(inputStream)
    }
    return byteArrayOf()
}

// Because I named the bucket as "User Image" so when it turns to an url, it is "%20"
// For better approach, you should create your bucket name without space symbol
fun buildImageUrl(imageFileName: String) =
    "${BuildConfig.SUPABASE_URL}/storage/v1/object/public/${imageFileName}".replace(" ", "%20")


//fun Exception.toCustomExceptions() = when (this) {
//    is ServerResponseException -> Failure.HttpErrorInternalServerError(this)
//    is ClientRequestException ->
//        when (this.response.status.value) {
//            400 -> Failure.HttpErrorBadRequest(this)
//            401 -> Failure.HttpErrorUnauthorized(this)
//            403 -> Failure.HttpErrorForbidden(this)
//            404 -> Failure.HttpErrorNotFound(this)
//            else -> Failure.HttpError(this)
//        }
//    is RedirectResponseException -> Failure.HttpError(this)
//    else -> Failure.GenericError(this)
//}