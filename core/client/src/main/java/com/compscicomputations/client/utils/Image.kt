package com.compscicomputations.client.utils

import io.ktor.util.*
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val bytes: ByteArray
) {
    fun encodeBase64(): String = bytes.encodeBase64()

    operator fun invoke(): ByteArray {
        return bytes
    }

//    operator fun get(index: Int): Byte = bytes[index]
//
//    operator fun set(index: Int, value: Byte) {
//        bytes[index] = value
//    }
//
//    operator fun iterator(): ByteIterator = bytes.iterator()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }

    companion object {
        inline val ByteArray?.asImage: Image?
            get() = if(this == null || this.isEmpty()) null else Image(this)
    }
}