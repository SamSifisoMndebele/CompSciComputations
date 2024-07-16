package com.compscicomputations.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Converts bitmap to encoded string in PNG format
 */
fun Bitmap.encodeToString(): String {
    var baos: ByteArrayOutputStream? = null
    try {
        baos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val bytes = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    } finally {
        try {
            baos?.close()
        } catch (e: IOException) {
            Log.e("BitmapUtils::encodeToString", "ByteArrayOutputStream was not closed")
        }
    }
}

/**
 * Converts compressed encoded string to bitmap
 */
fun String.decodeToBitmap(): Bitmap {
    val bytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}



//        val uri: Uri = data.getData()
//        val bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri)

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
