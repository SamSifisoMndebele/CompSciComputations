package com.ssmnd.mathslib.utils

import java.text.NumberFormat
import kotlin.math.abs
import kotlin.math.floor

fun Number.fixTo(decimals: Int = 2): String {
    val numberFormat = NumberFormat.getInstance()
    numberFormat.maximumFractionDigits = decimals
    numberFormat.minimumFractionDigits = decimals
    return numberFormat.format(this).replace(',', '.')
}

fun Number.roundTo(decimals: Int = 2): String {
    val numberFormat = NumberFormat.getInstance()
    numberFormat.maximumFractionDigits = decimals
    return numberFormat.format(this).replace(',', '.')
}

class Fraction(numerator: Long, denominator: Long, val decimalNumber: Double) {
    val numerator: Long
    val denominator: Long
    init {
        if (denominator < 0) {
            this.numerator = -numerator
            this.denominator = -denominator
        } else{
            this.numerator = numerator
            this.denominator = denominator
        }
    }
    val negate
        get() = if (denominator > 0) Fraction(-numerator, denominator, -decimalNumber)
        else Fraction(numerator, -denominator, -decimalNumber)

    val isNegative
        get() = (numerator < 0 && denominator > 0 || numerator > 0 && denominator < 0)
    val isPositive
        get() = numerator > 0 && denominator > 0
    val isZero: Boolean
        get() = numerator == 0L
    val isNaN: Boolean
        get() = numerator == 0L && denominator == 0L || java.lang.Double.isNaN(decimalNumber)


    companion object {
        fun fractionOf(numerator: Long, denominator: Long) =
            Fraction(numerator, denominator, numerator.toDouble() / denominator)

        fun fractionOf(numerator: Int, denominator: Int) =
            simplyFraction(Fraction(numerator.toLong(), denominator.toLong(), numerator.toDouble() / denominator))

        fun fractionOf(decimalNumber: Double) = decimalNumber.toFraction()
        private fun simplyFraction(fraction: Fraction): Fraction {
            val numerator = fraction.numerator
            val denominator = fraction.denominator
            var hcf: Long = 0

            var i: Long = 1
            while (i <= numerator || i <= denominator) {
                if (numerator % i == 0L && denominator % i == 0L) hcf = i
                i++
            }

            return Fraction(numerator / hcf, denominator / hcf, fraction.decimalNumber)
        }
    }
}

fun Number.toFraction(): Fraction {
    val tolerance = 5.0E-13
    val doubleNumber = this.toDouble()

    val isNegative = doubleNumber < 0

    val decimalNumber = if (isNegative) -doubleNumber else doubleNumber

    var numerator = 1.0
    var num = 0.0
    var denominator = 0.0
    var den = 1.0
    var number = decimalNumber
    do {
        val numberFloor = floor(number)

        var temp = numerator
        numerator = numberFloor * numerator + num
        num = temp

        temp = denominator
        denominator = numberFloor * denominator + den
        den = temp
        number = 1 / (number - numberFloor)
    } while (abs(decimalNumber - numerator / denominator) > decimalNumber * tolerance)

    return Fraction((if (isNegative) -numerator else numerator).toLong(), denominator.toLong(), doubleNumber)
}
fun Number.toFracString(): String {
    val fraction = this.toFraction()

    val numerator = fraction.numerator.toString()
    if (fraction.denominator.toDouble() == 1.0) {
        return numerator
    } else {
        val denominator = fraction.denominator.toString()
        if (numerator.length > 4 || denominator.length > 4) {
            return fraction.decimalNumber.toString()
        }
        return fraction.numerator.toString() + "/" + fraction.denominator
    }
}
