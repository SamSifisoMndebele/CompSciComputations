package com.compscicomputations.number_systems.ui.floating_point

import com.compscicomputations.number_systems.utils.BinaryArithmetic.padBits
import com.compscicomputations.number_systems.utils.decimalNumberRegex
import com.compscicomputations.utils.notMatches

object FloatingPointConverter {
    private const val SIZE_ERROR = "Size error"
    private const val INVALID_DECIMAL = "Invalid decimal number"

    fun FloatingPointUiState.fromDecimal(decimal: String): FloatingPointUiState {
        val decimalStr = decimal.replace(',', '.')
        if (decimalStr.notMatches(decimalNumberRegex))
            return copy(
                decimal = decimalStr,
//                miniFloat = "",
                binary16 = "",
                binary32 = "",
                binary64 = "",
                error = INVALID_DECIMAL
            )

        try {
            val parseDouble = decimalStr.toDouble()
            val parseFloat = decimalStr.toFloat()

            val posBit = if (parseFloat > 0.0f) "0" else ""
            val float = posBit + Integer.toBinaryString(java.lang.Float.floatToRawIntBits(parseFloat))
            val double = posBit + java.lang.Long.toBinaryString(java.lang.Double.doubleToLongBits(parseDouble))

            return copy(
                decimal = decimalStr,
//                miniFloat = "",
                binary16 = "",
                binary32 = float,
                binary64 = double,
                error = null
            )
        } catch (e: NumberFormatException) {
            return copy(
                decimal = decimalStr,
//                miniFloat = "",
                binary16 = "",
                binary32 = "",
                binary64 = "",
                error = SIZE_ERROR
            )
        }
    }

    fun FloatingPointUiState.fromIEEE754(floatStr: String): FloatingPointUiState {
        try {
            if (floatStr.length > 9) {
                val long = java.lang.Long.parseUnsignedLong(floatStr.padBits(64), 2)
                val decimal = java.lang.Float.intBitsToFloat(long.toInt())
                if (1.0f + decimal < 0.0f) {
                    return copy(
                        decimal = "",
//                        miniFloat = "",
                        binary16 = "",
                        binary32 = floatStr,
                        binary64 = "",
                        error = INVALID_DECIMAL
                    )
                }
                val double = (if (decimal < 0.0f) "1" else "0") + java.lang.Long.toBinaryString(
                    java.lang.Double.doubleToLongBits(decimal.toDouble())
                )

                return copy(
                    decimal = decimal.toString(),
//                    miniFloat = "",
                    binary16 = "",
                    binary32 = floatStr,
                    binary64 = double,
                    error = null
                )
            }
            else return FloatingPointUiState()
        } catch (e: Exception) {
            return copy(
                decimal = "",
//                miniFloat = "",
                binary16 = "",
                binary32 = floatStr,
                binary64 = "",
                error = SIZE_ERROR
            )
        }
    }

    fun FloatingPointUiState.fromDoubleIEEE754(doubleStr: String): FloatingPointUiState {
        try {
            if (doubleStr.length > 12) {
                val long = java.lang.Long.parseUnsignedLong(doubleStr.padBits(64), 2)
                val decimal = java.lang.Double.longBitsToDouble(long)
                if (1.0 + decimal < 0.0) {
                    return copy(
                        decimal = "",
//                        miniFloat = "",
                        binary16 = "",
                        binary32 = "",
                        binary64 = doubleStr,
                        error = SIZE_ERROR
                    )
                }

                val float = (if (decimal < 0.0) "1" else "0") + java.lang.Long.toBinaryString(
                    java.lang.Float.floatToIntBits(decimal.toFloat()).toLong()
                )

                return copy(
                    decimal = decimal.toString(),
//                    miniFloat = "",
                    binary16 = "",
                    binary32 = float,
                    binary64 = doubleStr,
                    error = null
                )
            }
            else return FloatingPointUiState()
        } catch (e: Exception) {
            return copy(
                decimal = "",
//                miniFloat = "",
                binary16 = "",
                binary32 = "",
                binary64 = doubleStr,
                error = SIZE_ERROR
            )
        }
    }
}