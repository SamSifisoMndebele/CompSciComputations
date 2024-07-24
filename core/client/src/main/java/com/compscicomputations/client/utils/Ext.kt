package com.compscicomputations.client.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.protobuf.ByteString
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US)


/**
 * Converts bitmap to byte string
 */
val Bitmap.asByteArray: ByteArray
    get() = ByteArrayOutputStream().use {
        compress(Bitmap.CompressFormat.PNG, 100, it)
        it.toByteArray()
    }

/**
 * Converts bitmap to byte string
 */
val Bitmap.asByteString: ByteString
    get() = ByteString.copyFrom(asByteArray)

/**
 * Converts byte array to bitmap
 */
val ByteArray.asBitmap: Bitmap
    get() = BitmapFactory.decodeByteArray(this, 0, this.size)

/**
 * Converts byte string to bitmap
 */
val ByteString.asBitmap: Bitmap
    get() = toByteArray().asBitmap


@Throws(IOException::class)
private fun Context.readBytes(uri: Uri): ByteArray? =
    contentResolver.openInputStream(uri)?.use { io -> io.buffered().readBytes() }


@Throws(IOException::class)
private fun Context.scaledByteArrayOf(uri: Uri, maxSize: Int = 540): ByteArray {
    val bytes = readBytes(uri)
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
    Log.d("ScaledBitmap::bitmap", bitmap.byteCount.toString())
    val size = if(bitmap.width < bitmap.height) bitmap.width else bitmap.height
    val newSize = if (size <= maxSize) size else maxSize
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newSize, newSize, true)
    Log.d("ScaledBitmap::scaledBitmap", scaledBitmap.byteCount.toString())
    return ByteArrayOutputStream().use {
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        it.toByteArray()
    }
}

/**
 * Converts Uri to byte string
 * @throws IOException
 */
class ScaledByteArrayUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(uri: Uri, maxSize: Int = 540): ByteArray =
        context.scaledByteArrayOf(uri, maxSize)

}
