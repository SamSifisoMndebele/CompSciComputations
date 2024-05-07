package com.ssmnd.karnaughmap.utils

import java.util.Locale
import kotlin.math.pow

val String.binaryToDecimal: String
    get() {
        if (this.isEmpty()) return ""
        var decimalString = ""
        for (number in this.uppercase(Locale.getDefault()).split(" ".toRegex()).toTypedArray()) {
            if (number.isNotEmpty()) {
                var remainder: Long = 0
                var i = 0
                for (digit in number.reversed()) {
                    try {
                        val remainderDouble = remainder.toDouble()
                        val numbDouble = digit.toString().toLong().toDouble()

                        java.lang.Double.isNaN(numbDouble)
                        java.lang.Double.isNaN(remainderDouble)

                        remainder = (remainderDouble + numbDouble * 2.0.pow(i.toDouble())).toLong()

                        i++
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                        return ""
                    }
                }
                decimalString = "$decimalString$remainder "
            }
        }
        return decimalString
    }