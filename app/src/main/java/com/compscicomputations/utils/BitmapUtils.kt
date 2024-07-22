package com.compscicomputations.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException

@Throws(IOException::class)
private fun Context.readBytes(uri: Uri): ByteArray? =
    contentResolver.openInputStream(uri)?.use { io -> io.buffered().readBytes() }


@Throws(IOException::class)
fun Context.asScaledByteArray(uri: Uri, maxSize: Int = 540): ByteArray {
    val bytes = readBytes(uri)
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
    val size = if(bitmap.width < bitmap.height) bitmap.width else bitmap.height
    val newSize = if (size <= maxSize) size else maxSize
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newSize, newSize, true)
    return ByteArrayOutputStream().use {
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        it.toByteArray()
    }
}

/**
 * Converts bitmap to encoded string in PNG format
 */
fun Bitmap.encodeToString(): String {
    val bytes = ByteArrayOutputStream().use {
        compress(Bitmap.CompressFormat.PNG, 100, it)
        it.toByteArray()
    }
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

/**
 * Converts compressed encoded string to bitmap
 */
fun String.decodeToBitmap(): Bitmap {
    val bytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

/**
 * Converts encoded bytearray to bitmap
 */
fun ByteArray.decodeToBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}




//    /**
//     * Converts bitmap to the byte array without compression
//     * @param bitmap source bitmap
//     * @return result byte array
//     */
//    fun convertBitmapToByteArrayUncompressed(bitmap: Bitmap): ByteArray {
//
//        val byteBuffer = ByteBuffer.allocate(bitmap.byteCount)
//        bitmap.copyPixelsToBuffer(byteBuffer)
//        byteBuffer.rewind()
//        return byteBuffer.array()
//    }
