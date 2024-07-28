package com.compscicomputations.number_systems.ui.complement

import com.compscicomputations.number_systems.ui.bases.BaseConverter.toDecimal
import com.compscicomputations.number_systems.utils.BinaryArithmetic
import com.compscicomputations.number_systems.utils.BinaryArithmetic.addBinary
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
        if (decimal.isEmpty()) return ComplementUiState()
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
            val binary = java.lang.Long.toBinaryString(abs(dec.toLong())).fillBits()

            complement1 += (if (long >= 0) binary else binary.negateBin()) + " "
            complement2 += (if (long >= 0) binary else addBinary(binary.negateBin(), "1")) + " "
        }

        return copy(
            decimal = decimal,
            complement1 = complement1,
            complement2 = complement2,
            error = null
        )
    }

    fun ComplementUiState.fromComplement1(complement1Str: String): ComplementUiState {
        return try {
            var decimal = complement1Str.toDecimal()
            var decimalString = decimal.toString()

            if (complement1Str[0] == '1') {
                decimal = complement1Str.negateBin().toDecimal()
                decimalString = "-$decimal"
            }
            if (decimal < 0) copy(error = SIZE_ERROR)
            else fromDecimal(decimalString)
        } catch (unused: Exception) {
            copy(error = SIZE_ERROR)
        }
    }

    fun ComplementUiState.fromComplement2(complement2Str: String): ComplementUiState {
        return try {
            var decimal = complement2Str.toDecimal()
            var decimalString = decimal.toString()

            if (complement2Str[0] == '1') {
                decimal = complement2Str.negateBin().toDecimal()
                decimalString = "-${decimal + 1}"
            }
            if (decimal + 1 < 0) copy(error = SIZE_ERROR)
            else fromDecimal(decimalString)
        } catch (unused: Exception) {
            copy(error = SIZE_ERROR)
        }
    }

}