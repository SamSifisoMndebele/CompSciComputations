package com.compscicomputations.number_systems.logic

import com.compscicomputations.number_systems.logic.BinaryArithmetic.fillBits
import com.compscicomputations.number_systems.logic.bases.BaseConverter.toDecimal

object FloatingBinaryConvert {

    fun fromDecimal(decimalString: String): Conversion {
        var conversion = Conversion()
        val decimal = decimalString.replace(',', '.')
        try {
            val parseDouble = decimal.toDouble()
            val parseFloat = decimal.toFloat()

            val floatStr = if (parseFloat > 0.0f) "0" else ""
            val float = floatStr + Integer.toBinaryString(java.lang.Float.floatToRawIntBits(parseFloat))
            val doubleIEEE754 = floatStr + java.lang.Long.toBinaryString(java.lang.Double.doubleToLongBits(parseDouble))

            conversion = Conversion(decimal, float, doubleIEEE754)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return conversion
    }

    fun fromIEEE754(floatString: String): Conversion {
        var conversion = Conversion()
        try {
            if (floatString.length > 9) {
                val intBitsToFloat = java.lang.Float.intBitsToFloat(floatString.fillBits(32).toDecimal().toInt())
                if (1.0f + intBitsToFloat < 0.0f) {
                    conversion.error = true
                }
                val doubleIEEE754 = (if (intBitsToFloat < 0.0f) "1" else "0") + java.lang.Long.toBinaryString(
                        java.lang.Double.doubleToLongBits(intBitsToFloat.toDouble())
                    )

                conversion = Conversion(intBitsToFloat.toString(), floatString, doubleIEEE754)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return conversion
    }

    fun fromDoubleIEEE754(doubleString: String): Conversion {
        var conversion = Conversion()
        try {
            if (doubleString.length > 12) {
                val longBitsToDouble = java.lang.Double.longBitsToDouble(doubleString.fillBits(64).toDecimal())
                if (1.0 + longBitsToDouble < 0.0) {
                    conversion.error = true
                }

                val iEEE754 = (if (longBitsToDouble < 0.0) "1" else "0") +
                        java.lang.Long.toBinaryString(java.lang.Float.floatToIntBits(longBitsToDouble.toFloat()).toLong())

                conversion = Conversion(longBitsToDouble.toString(), iEEE754, doubleString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return conversion
    }
}