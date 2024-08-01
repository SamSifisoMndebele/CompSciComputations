package com.compscicomputations.number_systems.ui.floating_point

import com.compscicomputations.number_systems.utils.BinaryArithmetic.fixBits
import com.compscicomputations.number_systems.utils.Ieee754Binary16
import com.compscicomputations.number_systems.utils.decimalRegex
import com.compscicomputations.utils.notMatches

object FloatingPointConverter {
    private const val SIZE_ERROR = "Size error"
    private const val INVALID_DECIMAL = "Invalid decimal number"

    fun FloatingPointUiState.fromDecimal(decimal: String): FloatingPointUiState {
        if (decimal.isBlank()) return FloatingPointUiState()
        if (decimal.endsWith('-') ||
            decimal.endsWith('+') ||
            decimal.endsWith('e', true)) return copy(decimal = decimal)
        val decimalStr = decimal.replace(',', '.')
        if (decimalStr.notMatches(decimalRegex))
            return copy(
                decimal = decimalStr,
                miniFloat = "",
                binary16 = "",
                binary32 = "",
                binary64 = "",
                error = INVALID_DECIMAL
            )

        try {
            val float = decimalStr.toFloat()
            val double = decimalStr.toDouble()

            val binary16 = Integer.toBinaryString(Ieee754Binary16.floatToBinary16ShortBits(float).toInt())
            val binary32 = Integer.toBinaryString(java.lang.Float.floatToRawIntBits(float))
            val binary64 = java.lang.Long.toBinaryString(java.lang.Double.doubleToLongBits(double))

            return copy(
                decimal = decimalStr,
                miniFloat = "",
                binary16 = binary16.fixBits(16),
                binary32 = binary32.fixBits(32),
                binary64 = binary64.fixBits(64),
                error = null
            )
        } catch (e: NumberFormatException) {
            return copy(
                decimal = decimalStr,
                miniFloat = "",
                binary16 = "",
                binary32 = "",
                binary64 = "",
                error = SIZE_ERROR
            )
        }
    }

    fun FloatingPointUiState.fromMiniFloat(miniFloat: String): FloatingPointUiState {
        try {
            TODO()
        } catch (e: Exception) {
            return copy(
                decimal = "",
                miniFloat = miniFloat,
                binary16 = "",
                binary32 = "",
                binary64 = "",
                error = SIZE_ERROR
            )
        }
    }

    fun FloatingPointUiState.fromBinary16(binary16Str: String): FloatingPointUiState {
        try {
            val intBits = Integer.parseUnsignedInt(binary16Str.fixBits(16), 2)
            val float = Ieee754Binary16.binary16ShortBitsToFloat(intBits.toShort())

            val binary32 = Integer.toBinaryString(java.lang.Float.floatToIntBits(float))
            val binary64 = java.lang.Long.toBinaryString(java.lang.Double.doubleToLongBits(float.toDouble()))

            return copy(
                decimal = float.toString(),
                miniFloat = "",
                binary16 = binary16Str,
                binary32 = binary32.fixBits(32),
                binary64 = binary64.fixBits(64),
                error = null
            )
        } catch (e: Exception) {
            return copy(
                decimal = "",
                miniFloat = "",
                binary16 = binary16,
                binary32 = "",
                binary64 = "",
                error = SIZE_ERROR
            )
        }
    }

    fun FloatingPointUiState.fromBinary32(binary32Str: String): FloatingPointUiState {
        try {
            val intBits = Integer.parseUnsignedInt(binary32Str.fixBits(32), 2)
            val float = java.lang.Float.intBitsToFloat(intBits)

            val binary16 = Integer.toBinaryString(Ieee754Binary16.floatToBinary16ShortBits(float).toInt())
            val binary64 = java.lang.Long.toBinaryString(java.lang.Double.doubleToLongBits(float.toDouble()))

            return copy(
                decimal = float.toString(),
                miniFloat = "",
                binary16 = binary16.fixBits(16),
                binary32 = binary32Str,
                binary64 = binary64.fixBits(64),
                error = null
            )
        } catch (e: Exception) {
            return copy(
                decimal = "",
                miniFloat = "",
                binary16 = "",
                binary32 = binary32Str,
                binary64 = "",
                error = SIZE_ERROR
            )
        }
    }

    fun FloatingPointUiState.fromBinary64(binary64Str: String): FloatingPointUiState {
        try {
            val long = java.lang.Long.parseUnsignedLong(binary64Str.fixBits(64), 2)
            val double = java.lang.Double.longBitsToDouble(long)

            val binary16 = Integer.toBinaryString(Ieee754Binary16.floatToBinary16ShortBits(double.toFloat()).toInt())
            val binary32 = Integer.toBinaryString(java.lang.Float.floatToIntBits(double.toFloat()))

            return copy(
                decimal = double.toString(),
                miniFloat = "",
                binary16 = binary16.fixBits(16),
                binary32 = binary32.fixBits(32),
                binary64 = binary64Str,
                error = null
            )
        } catch (e: Exception) {
            return copy(
                decimal = "",
                miniFloat = "",
                binary16 = "",
                binary32 = "",
                binary64 = binary64Str,
                error = SIZE_ERROR
            )
        }
    }
}