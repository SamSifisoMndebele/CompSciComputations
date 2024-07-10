package com.compscicomputations.feature.karnaugh_maps.utils

import android.util.Log
import kotlin.math.pow

fun List<String>.binaryToDecimalList(): List<String> {
    val string = distinct().joinToString(" ")
    if (string.isEmpty()) return listOf()
    var decimalStr = ""
    for (number in string.uppercase().split(" ")) {
        if (number.isEmpty()) continue
        try {
            var decimal: Long = 0
            var i = 0
            for (digit in number.reversed()) {
                val num = digit.toString().toInt()
                decimal += (num * 2.0.pow(i++)).toLong()
            }
            decimalStr = "$decimalStr$decimal "
        } catch (e: Exception) {
            Log.e("KarnaughMaps", "Base Conversion", e)
            return listOf()
        }
    }
    return decimalStr.trim().split(" ")
}