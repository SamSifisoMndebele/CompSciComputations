package com.compscicomputations.number_systems.utils

import com.compscicomputations.number_systems.ui.bases.BaseConverter.toDecimal
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.pow

object BinaryArithmetic {
    /**
     * Fill binary strings to nearest 4 bits
     */
    fun String.fillBits(): String {
        val bits = 2.0.pow(ceil(ln(trim().length.toDouble()) / ln(2.0))).toInt()
        return padStart(bits, '0')
    }

    /**
     * Binary string with [bits] bits. Filled `0` bit on start.
     */
    fun String.fillBits(bits: Int): String = padStart(bits, '0')

    /**
     * Compliment of the binary string
     */
    fun String.negateBin(): String = buildString {
        this@negateBin.forEach { char ->
            if (char == '0') append('1')
            else append('0')
        }
    }


    /**
     * Adds two binary strings
     * @throws NumberFormatException
     */
    fun addBinary(a: String, b: String): String =
        java.lang.Long.toBinaryString(a.toDecimal() + b.toDecimal())









    /*----------- Convert a binary string to 2s compliment ------------------------------*/
    /*fun Any.negativeBin(bits : Int = 16) : String {
        val bin = this.toString().padStart(bits, '0')
        val complimentString = buildString {
            for (char in bin) {
                if (char == '0') {
                    append('1')
                }
                else if (char == '1') {
                    append('0')
                }
            }
        }
        return addBinary(complimentString, "1")
    }*/

    /*----------- Round to nearest any number -------------------------------------------*/
    /*fun Number.round(nearest: Int = 10): Int {
        return ((this.toDouble() / nearest).roundToInt() * nearest)
    }*/

    /*----------- Round up to nearest any number   --------------------------------------*/
    /*fun Number.roundUp(nearest: Int = 10): Int {
        return (((this.toDouble() + nearest/2)/ nearest - 0.01).roundToInt() * nearest)
    }*/

    /*----------- Convert Decimal string to Binary --------------------------------------*/
    /*fun String.decimalToBinary(): String {
        val binaryString = this.split(".", ",")

        val isNegative: Boolean
        var number : ULong = if (binaryString[0].startsWith('-')){
            isNegative = true
            binaryString[0].drop(1).toULong()
        } else {
            isNegative = false
            binaryString[0].toULong()
        }

        val binaryNum = StringBuilder()
        while (number > 0u){
            val remainder = number % 2u
            binaryNum.append(remainder)
            number /= 2u
        }
        var binary = binaryNum.toString().reversed()

        if ((this.contains('.') || this.contains(',')) && !isNegative){
            var roundNum = 64

            var fractionNum = "0.${binaryString[1]}".toDouble()
            val decimalNum = StringBuilder()

            while (roundNum > 0 && fractionNum != 1.0){
                fractionNum *= 2.0
                println(fractionNum)
                if (fractionNum < 1.0) {
                    decimalNum.append(0)
                } else {
                    if (fractionNum != 1.0)
                        fractionNum--
                    decimalNum.append(1)
                }

                roundNum--
            }

            binary = "${binaryNum.reversed()}.$decimalNum"
        }

        val answer = if (isNegative){
            val bits = binary.length.roundUp(4)
            binary.negativeBin(bits)
        } else {
            binary
        }

        return answer
    }*/

    /*----------- Convert Binary string to Decimal --------------------------------------*/
   /* fun String.binaryToDecimal2(bit: Int = 16): String {
        val binaryString = this.split(".", ",")

        val string = if (binaryString[0].startsWith('-')){
            val binary = binaryString[0].drop(1)
            val bits = binary.length.roundUp(bit)
            binary.negativeBin(bits)
        } else {
            binaryString[0]
        }
        val numberString = string.padStart(string.length.roundUp(bit) - string.length, '0')
        numberString.padStart(string.length.roundUp(bit) - string.length, '0')

        println(string)

        if (numberString.startsWith("1")){
            var numb : Long = 0
            var i = 0
            for (digit in numberString.negativeBin().reversed()){
                if (digit.isDigit()){
                    numb += digit.toString().toInt() * 2.0.pow(i).toLong()
                }
                i++
            }
            return (-1*numb).toString()
        }


        var number : Long = 0
        var i = 0
        for (digit in numberString.reversed()){
            if (digit.isDigit()){
                number += digit.toString().toByte() * 2.0.pow(i).toLong()
            }
            i++
        }

        var answer : String = number.toString()
        var fraction = 0.0
        if (this.contains('.') || this.contains(',')){
            val decimal = binaryString[1]
            var j = -1
            for (digit in decimal){
                fraction += digit.toString().toInt() * 2.0.pow(j)
                j--
            }
            answer = (number+fraction).toString()
        }
        return answer
    }*/

    /*----------- Convert a Floating Point number to Decimal -----------------------------*/
    /*fun String.floatingPointToDecimal(bits: Int = 8): String {
        val binary = this
        val exp: Int
        val mantissa: Double

        val signBit: Int = binary[0].toString().toInt()

        val expString = "${binary[1]}${binary[2]}${binary[3]}"
        exp = expString.toDecimal().toInt() - 4

        val mantissaString = "${binary[4]}${binary[5]}${binary[6]}${binary[7]}"
        mantissa = "0.$mantissaString".toDouble()

        // val newBin = String.format("%.10f", mantissa * 10.0.pow(exp))
        val newBin =  ((-1.0).pow(signBit) * mantissaString.toDecimal().toDouble() * 2.0.pow(exp)).toString()

        val answer = StringBuilder()
        var ans = newBin
        if (signBit == 1){
            answer.append("Sign bit : 1 -> Negative\n")
            ans = "-$ans"
        } else {
            answer.append("Sign bit : 0 -> Positive\n")
        }
        answer.append("Exponent : $expString -> $exp\n")
        answer.append("Mantissa : $mantissaString\n\n")
        answer.append("Then : ${newBin.take(10)} = $ans")


        //val num3 = decimalNum.toString().padStart(8, '0')
        *//*var before : String  = buildString {
            for (i in num2.indices) {
                if (i % 4 == 0 && i > 0)
                    append(' ')
                append(num2[i])
            }
        }.reversed()*//*

        return answer.toString()
    }*/
}