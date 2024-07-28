package com.compscicomputations.number_systems.ui.bases

import com.compscicomputations.number_systems.utils.binaryNumbersRegex
import com.compscicomputations.number_systems.utils.decimalNumberRegex
import com.compscicomputations.number_systems.utils.hexNumbersRegex
import com.compscicomputations.number_systems.utils.octalNumbersRegex
import com.compscicomputations.utils.notMatches
import kotlin.math.pow

object BaseConverter {

    private val hexLetters = mapOf(
        'A' to 10,
        'B' to 11,
        'C' to 12,
        'D' to 13,
        'E' to 14,
        'F' to 15
    )
    private const val INVALID_NUM = "Invalid number"
    private const val SIZE_ERROR = "Size error"
    private const val INVALID_DECIMAL = "Invalid decimal number"
    private const val INVALID_BINARY = "Invalid binary number"
    private const val INVALID_OCT = "Invalid octal number"
    private const val INVALID_HEX = "Invalid hexadecimal number"
    private const val INVALID_ASCII = "Invalid ascii character"

    fun BasesUiState.fromDecimal(decimal: String): BasesUiState {
        val decimalStr = decimal.removeSuffix("-")
        if (decimal.isEmpty()) return BasesUiState()
        if (decimalStr.split(" ").any{ it.notMatches(decimalNumberRegex)})
            return copy(error = INVALID_DECIMAL)

        var binaryStr = ""
        var octalStr = ""
        var hexStr = ""
        var asciiStr = ""

        for (dec in decimalStr.split(" ")) {
            if (dec.isEmpty()) continue
            val long = try {
                dec.toLong()
            } catch (e: NumberFormatException) {
                return copy(error = SIZE_ERROR)
            }

            binaryStr += java.lang.Long.toBinaryString(long) + " "
            octalStr += java.lang.Long.toOctalString(long) + " "
            hexStr += java.lang.Long.toHexString(long).uppercase() + " "
            asciiStr += long.toInt().toChar()
        }
        return copy(
            decimal = decimal,
            binary = binaryStr,
            octal = octalStr,
            hexadecimal = hexStr,
            ascii = asciiStr,
            convertFrom = ConvertFrom.Decimal,
            error = null
        )
    }

    /**
     * @throws NumberFormatException
     */
    private fun BasesUiState.fromRadix(string: String, base: Int): BasesUiState {
        if (string.isEmpty()) return BasesUiState()
        var decimalStr = ""
        for (number in string.uppercase().split(" ")) {
            if (number.isEmpty()) continue
            try {
                val decimal = number.toDecimal(base)
                decimalStr = "$decimalStr$decimal "
            } catch (e: SizeException) {
                return copy(error =  SIZE_ERROR)
            }
        }
        return fromDecimal(decimalStr)
    }

    fun BasesUiState.fromBinary(binaryStr: String): BasesUiState {
        if (binaryStr.isEmpty()) return BasesUiState()
        if (binaryStr.notMatches(binaryNumbersRegex)) {
            return copy(error = INVALID_BINARY)
        }
        return try {
            fromRadix(binaryStr, 2)
        } catch (e: NumberFormatException) {
            copy(error = INVALID_BINARY)
        }
    }
    fun BasesUiState.fromOctal(octalStr: String): BasesUiState {
        if (octalStr.isEmpty()) return BasesUiState()
        if (octalStr.notMatches(octalNumbersRegex)) {
            return copy(error = INVALID_OCT)
        }
        return try {
            fromRadix(octalStr, 8)
        } catch (e: NumberFormatException) {
            copy(error = INVALID_OCT)
        }
    }
    fun BasesUiState.fromHex(hexadecimalStr: String): BasesUiState {
        if (hexadecimalStr.isEmpty()) return BasesUiState()
        if (hexadecimalStr.notMatches(hexNumbersRegex)) {
            return copy(error = INVALID_HEX)
        }
        return try {
            fromRadix(hexadecimalStr, 16)
        } catch (e: NumberFormatException) {
            copy(error = INVALID_HEX)
        }
    }

    fun BasesUiState.fromAscii(charArray: CharSequence): BasesUiState {
        var decimalStr = ""
        for (char in charArray) {
            decimalStr += char.code.toString() + " "
        }
        return fromDecimal(decimalStr)
    }

    class SizeException : Exception("Size Error")

    /*----------- Convert Any base number string to Decimal --------------------------------------*/
    /**
     * @throws NumberFormatException
     * @throws SizeException
     */
    fun String.toDecimal(base: Int = 2): Long {
        val b = base.toDouble()
        var decimal: Long = 0
        var i = 0
        for (digit in this.reversed()) {
            val num = hexLetters[digit] ?: digit.toString().toInt()
            if (num >= base) throw NumberFormatException()

            decimal += (num * b.pow(i++)).toLong()
            if (java.lang.Double.isNaN(decimal.toDouble()))
                throw SizeException()
        }

        return decimal
    }
}