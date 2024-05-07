package com.ssmnd.util

import com.ssmnd.utils.toFraction
import kotlin.math.min
import kotlin.math.pow

@Deprecated("Dont use this.", level = DeprecationLevel.ERROR)
object Utils {
    fun toScientific(number: Double, decimals: Int): SciAns {
        val isNegative = number < 0

        var d = if (isNegative) -number else number
        var e = 0
        var m = d.toInt()

        if (m < 1) {
            while (m < 1 || m > 9) {
                d *= 10.0
                e--
                m = d.toInt()
            }
        } else {
            while (m < 1 || m > 9) {
                d /= 10.0
                e++
                m = d.toInt()
            }
        }

        val fixed = min(decimals.toDouble(), 15.0)
        val dec: Double = 10.0.pow(fixed)

        val numUp = dec * (if (isNegative) -d else d)
        var num = numUp.toLong()
        if (numUp > 0) {
            if (numUp - num >= 0.49999) {
                num++
            }
        } else {
            if (num - numUp >= 0.49999) {
                num--
            }
        }
        val string1 = (num / dec).toString() + "<small>×10<sup>" + e + "</sup></small>"
        val string2 = (num / dec).toString() + "E" + e

        return SciAns(string1, string2, string2.toDouble())
    }

    @JvmStatic

    fun toFractionLatex(decimalNumber: Double): String {
        var fraction = decimalNumber.toFraction()
        val isNegative = fraction.isNegative
        if (isNegative) fraction = fraction.negate

        val latex = StringBuilder()
        if (isNegative) latex.append('-')
        if (fraction.denominator.toDouble() == 1.0) {
            latex.append(fraction.numerator)
        } else {
            latex.append("\\frac{")
                .append(fraction.numerator)
                .append("}{")
                .append(fraction.denominator)
                .append("}")
        }

        return latex.toString()
    }

    data class SciAns(val text: String, val textE: String, val number: Double)


    private fun String.removePlusOnStart() : String {
        var equationString = this
        if (equationString.startsWith("+")){
            equationString = equationString.replaceFirst("+","")
        } else if (equationString.contains("{+")){
            equationString = equationString.replace("{+","{")
        }
        return equationString
    }
    /*private fun Array<Term>.hcf() : Term {
        var hcfCoefficient = if (this[0].coefficient > 0) this[0].coefficient else -this[0].coefficient
        for (i in 1 until this.size) {
            var hcf = hcfCoefficient
            var hcf2 = if (this[i].coefficient > 0) this[i].coefficient else -this[i].coefficient
            while (hcf != hcf2) {
                if (hcf > hcf2) hcf -= hcf2
                else hcf2 -= hcf
            }
            hcfCoefficient = hcf
        }

        var hcf = this[0].variables.toMutableMap()
        val negative = mutableMapOf<Char, Double>()
        this[0].variables.forEach{
            if(it.value < 0){
                negative[it.key] = it.value
            }
        }
        for(i in 1 until this.size){
            val temp = mutableMapOf<Char, Double>()
            this[i].variables.forEach{
                val variable = it.key
                val exponent = it.value

                val index = hcf.keys.indexOf(variable)

                if (index != -1){
                    val value = hcf.values.toList()[index]
                    if(value <= exponent) temp[variable] = value
                    else temp[variable] = exponent
                } else if (exponent < 0) {
                    negative[variable] = exponent
                }
            }
            hcf = temp
        }
        hcf.putAll(negative)

        return Term(hcfCoefficient,hcf.toMap())
    }
    private fun Array<Term>.lcm() : Term {
        var lcmCoefficient = if (this[0].coefficient > 0) this[0].coefficient else -this[0].coefficient
        for (i in 1 until this.size){
            var hcf = lcmCoefficient
            var hcf2 = if (this[i].coefficient > 0) this[i].coefficient else -this[i].coefficient
            val n1 = hcf
            val n2 = hcf2
            while (hcf != hcf2) {
                if (hcf > hcf2)
                    hcf -= hcf2
                else
                    hcf2 -= hcf
            }
            lcmCoefficient = (n1 * n2)/hcf
        }

        var lcm = mutableMapOf<Char, Double>()
        this[0].variables.forEach{
            if(it.value > 0){
                lcm[it.key] = it.value
            }
        }
        for(i in 1 until this.size) {
            val temp = mutableMapOf<Char, Double>()
            this[i].variables.forEach{
                val variable = it.key
                val exponent = it.value

                val index = lcm.keys.indexOf(variable)

                if (index != -1){
                    val value = lcm.values.toList()[index]
                    if(exponent >= value && exponent > 0) temp[variable] = exponent
                    else if(value > 0) temp[variable] = value
                } else if(exponent > 0) {
                    temp[variable] = exponent
                }
            }
            lcm.forEach{
                if(!temp.keys.contains(it.key)){
                    temp[it.key] = it.value
                }
            }

            lcm = temp
        }

        return Term(lcmCoefficient,lcm)
    }*/
}
