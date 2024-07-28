package com.compscicomputations.number_systems.ui.complement

import com.compscicomputations.number_systems.ui.bases.BaseConverter.toDecimal
import com.compscicomputations.number_systems.utils.BinaryArithmetic
import com.compscicomputations.number_systems.utils.BinaryArithmetic.fillBits
import com.compscicomputations.number_systems.utils.BinaryArithmetic.negateBin
import com.compscicomputations.number_systems.utils.decimalNumberRegex
import com.compscicomputations.utils.notMatches
import kotlin.math.abs

object ComplementConverter {

    private const val INVALID_NUM = "Invalid number"
    private const val SIZE_ERROR = "Size error"
    private const val INVALID_DECIMAL = "Invalid decimal number"


    fun ComplementUiState.fromDecimal(decimal: String): ComplementUiState {
        val decimalStr = decimal.removeSuffix("-")
        if (decimalStr.isEmpty()) return ComplementUiState()
        if (decimalStr.split(" ").any{ it.notMatches(decimalNumberRegex)})
            return copy(error = INVALID_DECIMAL)

        var complement1 = ""
        var complement2 = ""

        for (dec in decimalStr.split(" ")) {
            if (dec.isEmpty()) continue
            val long = try {
                dec.toLong()
            } catch (e: NumberFormatException) {
                return copy(error = SIZE_ERROR)
            }
            val binary = java.lang.Long.toBinaryString(abs(dec.toLong()))

            complement1 += (if (long >= 0) binary.fillBits()
            else binary.fillBits(true).negateBin()) + " "
            complement2 += (if (long >= 0) binary.fillBits()
            else BinaryArithmetic.addBinary(binary.fillBits(true).negateBin(), "1")) + " "
        }

        return copy(
            decimal = decimalStr,
            complement1 = complement1,
            complement2 = complement2,
            error = null
        )
    }

    fun ComplementUiState.fromComplement2(complement2String: String): ComplementUiState {
        return try {
            var decimal = complement2String.toDecimal()
            var decimalString = decimal.toString()

            if (complement2String[0] == '1') {
                decimal = complement2String.negateBin().toDecimal()
                decimalString = "-" + (decimal + 1)
            }
            if (decimal + 1 < 0) {
                copy(error = SIZE_ERROR)
            } else
                fromDecimal(decimalString)
        } catch (unused: Exception) {
            copy(error = SIZE_ERROR)
        }
    }

    fun ComplementUiState.fromComplement1(complement1String: String): ComplementUiState {
        return try {
            var decimal = complement1String.toDecimal()
            var decimalString = decimal.toString()

            if (complement1String[0] == '1') {
                decimal = complement1String.negateBin().toDecimal()
                decimalString = "-$decimal"
            }
            if (decimal + 1 < 0) {
                copy(error = SIZE_ERROR)
            } else
                fromDecimal(decimalString)
        } catch (unused: Exception) {
            copy(error = SIZE_ERROR)
        }
    }

}