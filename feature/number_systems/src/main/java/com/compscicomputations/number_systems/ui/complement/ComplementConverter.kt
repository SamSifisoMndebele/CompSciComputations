package com.compscicomputations.number_systems.ui.complement

import com.compscicomputations.number_systems.utils.BinaryArithmetic.bitsLength
import com.compscicomputations.number_systems.utils.BinaryArithmetic.fixBits
import com.compscicomputations.number_systems.utils.binaryRegex
import com.compscicomputations.number_systems.utils.numberRegex
import com.compscicomputations.utils.notMatches

object ComplementConverter {
    private const val SIZE_ERROR = "Size error"
    private const val INVALID_DECIMAL = "Invalid decimal number"
    private const val INVALID_BINARY = "Invalid binary number"


    fun ComplementUiState.fromDecimal(decimalStr: String): ComplementUiState {
        if (decimalStr.isBlank()) return ComplementUiState()
        if (decimalStr.endsWith('-') ||
            decimalStr.endsWith('+')) return copy(decimal = decimalStr)
        if (decimalStr.notMatches(numberRegex)) return copy(
            decimal = decimalStr,
            complement1 = "",
            complement2 = "",
            error = INVALID_DECIMAL
        )

        val long = try {
            decimalStr.toLong()
        } catch (e: NumberFormatException) {
            return copy(
                decimal = decimalStr,
                complement1 = "",
                complement2 = "",
                error = SIZE_ERROR
            )
        }

        val complement1 = java.lang.Long.toBinaryString(if (long < 0) long - 1 else long)
        val complement2 = java.lang.Long.toBinaryString(long)

        val length = long.bitsLength
        return copy(
            decimal = decimalStr,
            complement1 = complement1.fixBits(length),
            complement2 = complement2.fixBits(length),
            error = null
        )
    }

    fun ComplementUiState.fromComplement1(complement1Str: String): ComplementUiState {
        if (complement1Str.isBlank()) return ComplementUiState()
        if (complement1Str.notMatches(binaryRegex)) return copy(
            decimal = "",
            complement1 = complement1Str,
            complement2 = "",
            error = INVALID_BINARY
        )

        val long = try {
            java.lang.Long.parseUnsignedLong(complement1Str.fixBits(64, complement1Str.startsWith('1')), 2)
        } catch (e: NumberFormatException) {
            return copy(
                decimal = "",
                complement1 = complement1Str,
                complement2 = "",
                error = SIZE_ERROR
            )
        }
        val complement1Long = if (long < 0) long + 1 else long
        val complement2 = java.lang.Long.toBinaryString(complement1Long)

        return copy(
            decimal = complement1Long.toString(),
            complement1 = complement1Str,
            complement2 = complement2.fixBits(complement1Long.bitsLength),
            error = null
        )
    }

    fun ComplementUiState.fromComplement2(complement2Str: String): ComplementUiState {
        if (complement2Str.isBlank()) return ComplementUiState()
        if (complement2Str.notMatches(binaryRegex)) return copy(
            decimal = "",
            complement1 = "",
            complement2 = complement2Str,
            error = INVALID_BINARY
        )

        val long = try {
            java.lang.Long.parseUnsignedLong(complement2Str.fixBits(64, complement2Str.startsWith('1')), 2)
        } catch (e: NumberFormatException) {
            return copy(
                decimal = "",
                complement1 = "",
                complement2 = complement2Str,
                error = SIZE_ERROR
            )
        }

        val complement1Long = if (long < 0) long - 1 else long
        val complement1 = java.lang.Long.toBinaryString(complement1Long)

        return copy(
            decimal = long.toString(),
            complement1 = complement1.fixBits(long.bitsLength),
            complement2 = complement2Str,
            error = null
        )
    }
}