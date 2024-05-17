package com.compscicomputations.logic.num_systems

import com.compscicomputations.logic.num_systems.BinaryArithmetic.complimentBin
import com.compscicomputations.logic.num_systems.BinaryArithmetic.fillBits
import com.compscicomputations.logic.num_systems.bases.BaseConverter.toDecimal
import kotlin.math.abs

object ComplementConvert {
    fun fromDecimal(decimalString: String): Conversion {
        return try {
            val decimalLong = decimalString.toLong()
            if (decimalLong > Long.MAX_VALUE || decimalLong < Long.MIN_VALUE) {
                Conversion().apply { error = true }
            }
            Conversion(decimalString,
                decimalToComplement1(decimalString),
                decimalToComplement2(decimalString), null )
        } catch (e: Exception) {
            e.printStackTrace()
            Conversion().apply { error = (decimalString.isNotEmpty() && decimalString != "-") }
        }
    }

    fun fromComplement2(complement2String: String): Conversion {
        return try {
            var decimal = complement2String.toDecimal()
            var decimalString = decimal.toString()

            if (complement2String[0] == '1') {
                decimal = complement2String.complimentBin().toDecimal()
                decimalString = "-" + (decimal + 1)
            }
            if (decimal + 1 < 0) {
                Conversion().apply { error = true }
            } else
                fromDecimal(decimalString)
        } catch (unused: Exception) {
            Conversion().apply { error = complement2String.isNotEmpty() }
        }
    }

    fun fromComplement1(complement1String: String): Conversion {
        return try {
            var decimal = complement1String.toDecimal()
            var decimalString = decimal.toString()

            if (complement1String[0] == '1') {
                decimal = complement1String.complimentBin().toDecimal()
                decimalString = "-$decimal"
            }
            if (decimal + 1 < 0) {
                Conversion().apply { error = true }
            } else
                fromDecimal(decimalString)
        } catch (unused: Exception) {
            Conversion().apply { error = complement1String.isNotEmpty() }
        }
    }
    
    @Throws(NumberFormatException::class)
    fun decimalToComplement2(decimalString: String): String {
        val binary = java.lang.Long.toBinaryString(abs(decimalString.toLong()))

        return if (decimalString.toLong() < 0) {
            BinaryArithmetic.addBinary(binary.fillBits(true).complimentBin(), "1")
        } else
            binary.fillBits()
    }

    @Throws(NumberFormatException::class)
    private fun decimalToComplement1(decimalString: String): String {
        val binary = java.lang.Long.toBinaryString(abs(decimalString.toLong()))

        return if (decimalString.toLong() < 0) {
            binary.fillBits(true).complimentBin()
        } else
            binary.fillBits()
    }
}