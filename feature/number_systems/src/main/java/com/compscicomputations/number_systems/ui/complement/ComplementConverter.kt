package com.compscicomputations.number_systems.ui.complement

import com.compscicomputations.number_systems.utils.BinaryArithmetic.bitsLength
import com.compscicomputations.number_systems.utils.BinaryArithmetic.fixBits
import com.compscicomputations.number_systems.utils.decimalNumberRegex
import com.compscicomputations.utils.notMatches

object ComplementConverter {

    private const val INVALID_NUM = "Invalid number"
    private const val SIZE_ERROR = "Size error"
    private const val INVALID_DECIMAL = "Invalid decimal number"


    fun ComplementUiState.fromDecimal(decimalStr: String): ComplementUiState {
        if (decimalStr.isBlank()) return ComplementUiState()
        if (decimalStr.endsWith('-')) return copy(decimal = decimalStr)
        val decimals = decimalStr.split(" ")
        if (decimals.any{ it.notMatches(decimalNumberRegex)})
            return copy(
                decimal = decimalStr,
                complement1 = "",
                complement2 = "",
                error = INVALID_DECIMAL
            )
        
        val complement1 = mutableListOf<String>()
        val complement2 = mutableListOf<String>()

        for (dec in decimals) {
            if (dec.isEmpty()) continue
            val long = try {
                dec.toLong()
            } catch (e: NumberFormatException) {
                return copy(
                    decimal = decimalStr,
                    complement1 = "",
                    complement2 = "",
                    error = SIZE_ERROR
                )
            }

            val length = long.bitsLength
            complement1 += java.lang.Long.toBinaryString(if (long < 0) long - 1 else long).fixBits(length)
            complement2 += java.lang.Long.toBinaryString(long).fixBits(length)
        }
        return copy(
            decimal = decimalStr,
            complement1 = complement1.joinToString(" "),
            complement2 = complement2.joinToString(" "),
            error = null
        )
    }

    fun ComplementUiState.fromComplement1(complement1Str: String): ComplementUiState {
        val decimal = mutableListOf<String>()
        val complement2 = mutableListOf<String>()

        for (binary in complement1Str.split(" ")) {
            if (binary.isBlank()) continue
            val long = try {
                java.lang.Long.parseUnsignedLong(binary.fixBits(64, binary.startsWith('1')), 2)
            } catch (e: NumberFormatException) {
                return copy(
                    decimal = "",
                    complement1 = complement1Str,
                    complement2 = "",
                    error = SIZE_ERROR
                )
            }
            val complement1Long = if (long < 0) long + 1 else long
            decimal += complement1Long.toString()
            val length = complement1Long.bitsLength
            complement2 += java.lang.Long.toBinaryString(complement1Long).fixBits(length)
        }
        return copy(
            decimal = decimal.joinToString(" "),
            complement1 = complement1Str,
            complement2 = complement2.joinToString(" "),
            error = null
        )
    }

    fun ComplementUiState.fromComplement2(complement2Str: String): ComplementUiState {
        val decimal = mutableListOf<String>()
        val complement1 = mutableListOf<String>()

        for (binary in complement2Str.split(" ")) {
            if (binary.isBlank()) continue
            val long = try {
                java.lang.Long.parseUnsignedLong(binary.fixBits(64, binary.startsWith('1')), 2)
            } catch (e: NumberFormatException) {
                return copy(
                    decimal = "",
                    complement1 = "",
                    complement2 = complement2Str,
                    error = SIZE_ERROR
                )
            }
            decimal += long.toString()
            val length = long.bitsLength
            complement1 += java.lang.Long.toBinaryString(if (long < 0) long - 1 else long).fixBits(length)
        }
        return copy(
            decimal = decimal.joinToString(" "),
            complement1 = complement1.joinToString(" "),
            complement2 = complement2Str,
            error = null
        )
    }
}