package com.compscicomputations.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.datastore.preferences.protobuf.ByteString
import dagger.hilt.android.qualifiers.ApplicationContext
import okio.ByteString.Companion.toByteString
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

//@Throws(IOException::class)
//private fun Context.readBytes(uri: Uri): ByteArray? =
//    contentResolver.openInputStream(uri)?.use { io -> io.buffered().readBytes() }
//
//
//@Throws(IOException::class)
//private fun Context.scaledByteArrayOf(uri: Uri, maxSize: Int = 540): ByteArray {
//    val bytes = readBytes(uri)
//    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
//    Log.d("ScaledBitmap::bitmap", bitmap.byteCount.toString())
//    val size = if(bitmap.width < bitmap.height) bitmap.width else bitmap.height
//    val newSize = if (size <= maxSize) size else maxSize
//    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newSize, newSize, true)
//    Log.d("ScaledBitmap::scaledBitmap", scaledBitmap.byteCount.toString())
//    return ByteArrayOutputStream().use {
//        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
//        it.toByteArray()
//    }
//}
//
///**
// * Converts Uri to byte string
// * @throws IOException
// */
//class ScaledByteArrayUseCase @Inject constructor(
//   @ApplicationContext private val context: Context
//) {
//    operator fun invoke(uri: Uri, maxSize: Int = 540): ByteString =
//        ByteString.copyFrom(context.scaledByteArrayOf(uri, maxSize))
//
//}
//
///**
// * Converts byte array to bitmap
// */
//private fun ByteArray.decodeToBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
//
///**
// * Converts byte string to bitmap
// */
//fun ByteString.decodeToBitmap(): Bitmap = toByteArray().decodeToBitmap()
//
//
///**
// * Converts byte string to bitmap
// */
//val Bitmap.asByteString: ByteString
//    get() = ByteArrayOutputStream().use {
//        compress(Bitmap.CompressFormat.PNG, 100, it)
//        ByteString.copyFrom(it.toByteArray())
//    }


///**
// * Converts bitmap to encoded string in PNG format
// */
//fun Bitmap.encodeToString(): String {
//    val bytes = ByteArrayOutputStream().use {
//        compress(Bitmap.CompressFormat.PNG, 100, it)
//        it.toByteArray()
//    }
//    return Base64.encodeToString(bytes, Base64.DEFAULT)
//}

///**
// * Converts compressed encoded string to bitmap
// */
//fun String.decodeToBitmap(): Bitmap {
//    val bytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
//    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//}




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
