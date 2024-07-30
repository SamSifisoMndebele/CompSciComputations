package com.compscicomputations.client.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.protobuf.ByteString
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

/**
 * Converts bitmap to byte string
 */
inline val Bitmap.asByteArray: ByteArray
    get() = ByteArrayOutputStream().use {
        compress(Bitmap.CompressFormat.PNG, 100, it)
        it.toByteArray()
    }

/**
 * Converts bitmap to byte string
 */
inline val Bitmap.asByteString: ByteString
    get() = ByteString.copyFrom(asByteArray)

/**
 * Converts byte array to bitmap
 */
inline val ByteArray.asBitmap: Bitmap
    get() = BitmapFactory.decodeByteArray(this, 0, this.size)

/**
 * Converts byte string to bitmap
 */
inline val ByteString.asBitmap: Bitmap
    get() = toByteArray().asBitmap

/**
 * Converts Uri to byte string
 * @throws IOException
 */
class ScaledByteArrayUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @Throws(IOException::class)
    operator fun invoke(uri: Uri, maxSize: Int = 96): ByteArray {
        val bytes = context.contentResolver.openInputStream(uri)?.use { io -> io.buffered().readBytes() }
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
        val size = if(bitmap.width < bitmap.height) bitmap.width else bitmap.height
        val croppedBitmap = Bitmap.createBitmap(bitmap, (bitmap.width - size) / 2, (bitmap.height - size) / 2, size, size)
        val newSize = if (size <= maxSize) size else maxSize
        val scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, newSize, newSize, true)
        return ByteArrayOutputStream().use {
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.toByteArray()
        }
    }
}