package com.compscicomputations.number_systems.ui.bases

import com.compscicomputations.number_systems.utils.binaryRegex
import com.compscicomputations.number_systems.utils.hexadecimalRegex
import com.compscicomputations.number_systems.utils.numberRegex
import com.compscicomputations.number_systems.utils.octalRegex
import com.compscicomputations.utils.notMatches

object BaseConverter {
    private const val SIZE_ERROR = "Size error"
    private const val INVALID_DECIMAL = "Invalid decimal number"
    private const val INVALID_BINARY = "Invalid binary number"
    private const val INVALID_OCT = "Invalid octal number"
    private const val INVALID_HEX = "Invalid hexadecimal number"

    /**
     * @throws NumberFormatException
     */
    private fun BasesUiState.fromDecimal(decimal: List<String>): BasesUiState {
        val binary = mutableListOf<String>()
        val octal = mutableListOf<String>()
        val hexadecimal = mutableListOf<String>()
        val unicode = mutableListOf<Char>()

        for (dec in decimal) {
            if (dec.isEmpty()) continue
            val long = dec.toLong()

            binary.add(java.lang.Long.toBinaryString(long))
            octal.add(java.lang.Long.toOctalString(long))
            hexadecimal.add(java.lang.Long.toHexString(long).uppercase())
            unicode.add(long.toInt().toChar())
        }
        return copy(
            decimal = decimal.joinToString(" "),
            binary = binary.joinToString(" "),
            octal = octal.joinToString(" "),
            hexadecimal = hexadecimal.joinToString(" "),
            unicode = unicode.joinToString(""),
            error = null
        )
    }

    /**
     * @throws NumberFormatException
     */
    private fun BasesUiState.fromRadix(string: String, base: Int): BasesUiState {
        val decimal = mutableListOf<String>()
        for (number in string.uppercase().split(" ")) {
            if (number.isBlank()) continue
            decimal.add(java.lang.Long.parseUnsignedLong(number, base).toString())
        }
        return fromDecimal(decimal)
    }

    fun BasesUiState.fromDecimal(decimalStr: String): BasesUiState {
        if (decimalStr.isBlank()) return BasesUiState()
        if (decimalStr.endsWith('-') ||
            decimalStr.endsWith('+')) return copy(decimal = decimalStr)
        val decimals = decimalStr.split(" ")
        if (decimals.any{ it.notMatches(numberRegex)})
            return copy(
                decimal = decimalStr,
                binary = "",
                octal = "",
                hexadecimal = "",
                unicode = "",
                error = INVALID_DECIMAL
            )

        return try {
            fromDecimal(decimals)
        } catch (e: NumberFormatException) {
            copy(
                decimal = decimalStr,
                binary = "",
                octal = "",
                hexadecimal = "",
                unicode = "",
                error = SIZE_ERROR
            )
        }
    }
    fun BasesUiState.fromBinary(binaryStr: String): BasesUiState {
        if (binaryStr.isBlank()) return BasesUiState()
        if (binaryStr.notMatches(binaryRegex)) {
            return copy(
                decimal = "",
                binary = binaryStr,
                octal = "",
                hexadecimal = "",
                unicode = "",
                error = INVALID_BINARY
            )
        }
        return try {
            fromRadix(binaryStr, 2).copy(binary = binaryStr)
        } catch (e: NumberFormatException) {
            copy(
                decimal = "",
                binary = binaryStr,
                octal = "",
                hexadecimal = "",
                unicode = "",
                error = SIZE_ERROR
            )
        }
    }
    fun BasesUiState.fromOctal(octalStr: String): BasesUiState {
        if (octalStr.isBlank()) return BasesUiState()
        if (octalStr.notMatches(octalRegex)) {
            return copy(
                decimal = "",
                binary = "",
                octal = octalStr,
                hexadecimal = "",
                unicode = "",
                error = INVALID_OCT
            )
        }
        return try {
            fromRadix(octalStr, 8).copy(octal = octalStr)
        } catch (e: NumberFormatException) {
            copy(
                decimal = "",
                binary = "",
                octal = octalStr,
                hexadecimal = "",
                unicode = "",
                error = SIZE_ERROR
            )
        }
    }
    fun BasesUiState.fromHex(hexadecimalStr: String): BasesUiState {
        if (hexadecimalStr.isBlank()) return BasesUiState()
        if (hexadecimalStr.notMatches(hexadecimalRegex)) {
            return copy(
                decimal = "",
                binary = "",
                octal = "",
                hexadecimal = hexadecimalStr,
                unicode = "",
                error = INVALID_HEX
            )
        }
        return try {
            fromRadix(hexadecimalStr, 16).copy(hexadecimal = hexadecimalStr)
        } catch (e: NumberFormatException) {
            copy(
                decimal = "",
                binary = "",
                octal = "",
                hexadecimal = hexadecimalStr,
                unicode = "",
                error = SIZE_ERROR
            )
        }
    }

    fun BasesUiState.fromUnicode(charArray: CharSequence): BasesUiState {
        if (charArray.isBlank()) return BasesUiState()
        val decimal = mutableListOf<String>()
        decimal.addAll(charArray.map { it.code.toString() })
        return fromDecimal(decimal)
    }
}