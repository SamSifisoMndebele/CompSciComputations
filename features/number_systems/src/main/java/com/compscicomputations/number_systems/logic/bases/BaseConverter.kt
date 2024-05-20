package com.compscicomputations.number_systems.logic.bases

import com.compscicomputations.number_systems.logic.binaryNumbersRegex
import com.compscicomputations.number_systems.logic.decimalNumberRegex
import com.compscicomputations.number_systems.logic.hexNumbersRegex
import com.compscicomputations.number_systems.logic.octalNumbersRegex
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

    fun fromCharSeq(charArray: CharSequence): BaseConversion {
        var decimalStr = ""
        for (char in charArray) {
            decimalStr += char.code.toString() + " "
        }
        return fromDecimal(decimalStr)
    }

    fun fromDecimal(decimalStr: String): BaseConversion {
        if (decimalStr.isEmpty()) return BaseConversion()
        if (decimalStr.split(" ").any{ it.notMatches(decimalNumberRegex)}) {
            return BaseConversion().apply {
                error = BaseError.INVALID_DECIMAL
            }
        }

        var binaryStr = ""
        var octalStr = ""
        var hexStr = ""
        var asciiStr = ""

        for (decimal in decimalStr.split(" ")) {
            if (decimal.isEmpty()) continue
            val long = try {
                decimal.toLong()
            } catch (e: NumberFormatException) {
                return BaseConversion().apply { error = BaseError.SIZE_ERROR }
            }

            binaryStr += java.lang.Long.toBinaryString(long) + " "
            octalStr += java.lang.Long.toOctalString(long) + " "
            hexStr += java.lang.Long.toHexString(long).uppercase() + " "
            asciiStr += long.toInt().toChar()
        }
        return BaseConversion(decimalStr, binaryStr, octalStr, hexStr, asciiStr)
    }

    private fun fromRadix(string: String, base: Int): BaseConversion {
        if (string.isEmpty()) return BaseConversion()
        var decimalStr = ""
        for (number in string.uppercase().split(" ")) {
            if (number.isEmpty()) continue
            try {
                val decimal = number.toDecimal(base)
                decimalStr = "$decimalStr$decimal "
            } catch (e: NumberFormatException) {
                return BaseConversion().apply {
                    error = BaseError errorOf base
                }
            } catch (e: SizeException) {
                return BaseConversion().apply {
                    error = BaseError.SIZE_ERROR
                }
            }
        }
        return fromDecimal(decimalStr)
    }

    fun fromBinary(binaryStr: String): BaseConversion {
        if (binaryStr.isEmpty()) return BaseConversion()
        if (binaryStr.notMatches(binaryNumbersRegex)) {
            return BaseConversion().apply {
                error = BaseError.INVALID_BINARY
            }
        }
        return fromRadix(binaryStr, 2)
    }
    fun fromOctal(octalStr: String): BaseConversion {
        if (octalStr.isEmpty()) return BaseConversion()
        if (octalStr.notMatches(octalNumbersRegex)) {
            return BaseConversion().apply {
                error = BaseError.INVALID_OCT
            }
        }
        return fromRadix(octalStr, 8)
    }
    fun fromHex(hexadecimalStr: String): BaseConversion {
        if (hexadecimalStr.isEmpty()) return BaseConversion()
        if (hexadecimalStr.notMatches(hexNumbersRegex)) {
            return BaseConversion().apply {
                error = BaseError.INVALID_HEX
            }
        }
        return fromRadix(hexadecimalStr, 16)
    }


    class SizeException : Exception("Size Error")

    /*----------- Convert Any base number string to Decimal --------------------------------------*/
    @Throws(NumberFormatException::class, SizeException::class)
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