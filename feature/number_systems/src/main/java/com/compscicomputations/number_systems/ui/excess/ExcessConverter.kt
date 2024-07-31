package com.compscicomputations.number_systems.ui.excess

import com.compscicomputations.number_systems.utils.BinaryArithmetic.padBits

object ExcessConverter {

//    SIZE_ERROR("Size error"),
//    INVALID_DECIMAL("Invalid decimal number"),
//    INVALID_EXCESS_BITS("Invalid number of bits"),
//    INVALID_EXCESS("Invalid excess binary"),
//    INVALID_NUM("Invalid number");


    private fun String.decimalToExcess(excessIdentifier: Long, excessBits : Int): String {
        val excessLong = this.toLong() + excessIdentifier
        val binary = java.lang.Long.toBinaryString(excessLong).padBits(64)

        val long = java.lang.Long.parseUnsignedLong(binary, 2)
        if (excessLong < 0 || excessIdentifier*2-1 < long)
            throw Exception()

        val excessBinary = binary.takeLast(excessBits-1)
        return if (this.toLong() < 0.toLong())
            "0$excessBinary"
        else
            "1$excessBinary"
    }
    private fun Int.bitsToIdentifier(): String {
        return try {
            val excessBits = this
            val excessIdentifier = "1".padEnd(excessBits, '0')
            java.lang.Long.parseUnsignedLong(excessIdentifier, 2).toString()
        } catch (e: Exception) {
            ""
        }
    }
//    fun fromDecimal(decimalStr: String, excessBits : Int): ExcessConversion {
//        val excessIdentifier: Long = excessBits.bitsToIdentifier().toLong()
//        return try {
//            val excess = decimalStr.decimalToExcess(excessIdentifier, excessBits)
//            ExcessConversion(decimalStr, excess, excessIdentifier.toString(), excessBits)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ExcessConversion("", "", excessIdentifier.toString(), excessBits).also {
////                if (decimalString.isNotEmpty() && decimalString != "-") it.error = ExcessError.SIZE_ERROR
//            }
//        }
//    }
//    fun fromExcess(excessStr: String, excessBits : Int): ExcessConversion {
//        val excessIdentifier: Long = excessBits.bitsToIdentifier().toLong()
//        return try {
//            if (excessStr.isNotEmpty()) {
//                val decimal = excessStr.toDecimal(2) - excessIdentifier
//                fromDecimal(decimal.toString(), excessBits)
//            } else {
//                ExcessConversion("", "", excessIdentifier.toString(), excessBits)
//            }
//
//        } catch (_: Exception) {
//            ExcessConversion("", "", excessIdentifier.toString(), excessBits).also {
////                it.error = excessStr.isNotEmpty()
//            }
//        }
//    }

}