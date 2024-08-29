package com.compscicomputations.number_systems.ui.excess

import com.compscicomputations.number_systems.utils.BinaryArithmetic.fixBits
import com.compscicomputations.number_systems.utils.binaryRegex
import com.compscicomputations.number_systems.utils.numberRegex
import com.compscicomputations.utils.notMatches

object ExcessConverter {
    private const val SIZE_ERROR = "Size error."
    private const val INVALID_DECIMAL = "Invalid decimal number."
    private const val INVALID_EXCESS = "Invalid excess binary."

    fun ExcessUiState.fromDecimal(decimalStr: String): ExcessUiState {
        if (decimalStr.isBlank()) return ExcessUiState(bits = bits)
        if (decimalStr.endsWith('-') ||
            decimalStr.endsWith('+')) return copy(decimal = decimalStr)
        if (decimalStr.notMatches(numberRegex)) return copy(
            decimal = decimalStr,
            excess = "",
            error = INVALID_DECIMAL
        )

        try {
            val long = decimalStr.toLong()
            if (long < min || long > max) throw NumberFormatException()

            val excessLong = long + bias
            val excess = java.lang.Long.toBinaryString(excessLong).fixBits(bits)

            return copy(
                decimal = decimalStr,
                excess = excess,
                error = null
            )
        } catch (e: NumberFormatException) {
            return copy(
                decimal = decimalStr,
                excess = "",
                error = SIZE_ERROR
            )
        }
    }
    fun ExcessUiState.fromExcess(excessStr: String): ExcessUiState {
        if (excessStr.isBlank()) return ExcessUiState(bits = bits)
        if (excessStr.notMatches(binaryRegex)) return copy(
            decimal = "",
            excess = excessStr,
            error = INVALID_EXCESS
        )

        try {
            val long = java.lang.Long.parseUnsignedLong(excessStr.let {
                if (it.startsWith('0')) it.fixBits(bits)
                else "1"+it.replace("1", "").fixBits(bits - 1)
            }, 2)
            val decimal = long - bias

            return copy(
                decimal = decimal.toString(),
                excess = excessStr,
                error = null
            )
        } catch (_: Exception) {
            return copy(
                decimal = "",
                excess = excessStr,
                error = SIZE_ERROR
            )
        }
    }

}