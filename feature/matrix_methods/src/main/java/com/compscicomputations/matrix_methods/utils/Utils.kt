package com.compscicomputations.matrix_methods.utils

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.annotation.ColorRes
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow


object Utils {
    val Double.zeroIfTooSmall : Double
        get() {
            return if (this in -1E-15 .. 1E-15) 0.0 else this
        }
    
    data class FractionNumber(val latex: String, val frac: Frac)
    data class Frac(val numerator: Long = 0, val denominator: Long = 1)
    fun Double.toFractionNumber(tolerance : Double = 1.0E-6): FractionNumber {
        val isNegative = this < 0
        val decimalNumber = if (isNegative) -this else this

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


        val fractionString : String  = if (this.toLong().toString().length > 6){
            toSci(1).textE
        } else if (denominator.toLong().toString().length > 6 ){
            toSci(1).textE
        } else {
            when {
                denominator == 1.0 && isNegative -> (-numerator).toLong().toString() //Negative Integer
                denominator == 1.0 && !isNegative -> numerator.toLong().toString()  //Positive Integer
                isNegative -> """-\frac{${numerator.toLong()}}{${denominator.toLong()}}"""  //Negative Fraction
                else ->"""\frac{${numerator.toLong()}}{${denominator.toLong()}}""" //Positive Fraction
            }
        }

        val fractionNumber: Frac = when {
            isNegative -> Frac(-numerator.toLong(),denominator.toLong()) //Negative Fraction
            else -> Frac(numerator.toLong(),denominator.toLong()) //Positive Fraction
        }
        return FractionNumber(fractionString, fractionNumber)
    }


    data class SciAns(val text: String, val textE: String, val double: Double)
    fun Double.toSci(decimals : Int) : SciAns {
        val isNegative = this<0

        var d = if (isNegative) -this else this
        var e = 0
        var m = d.toInt()

        if (m < 1) {
            while (m !in 1..9){
                d *= 10
                e--
                m = d.toInt()
            }
        } else {
            while (m !in 1..9){
                d /= 10
                e++
                m = d.toInt()
            }
        }

        val fixed = if (decimals >= 15) 15 else decimals
        val dec = 10.0.pow(fixed)

        val numUp = dec * if (isNegative) -d else d
        var num = numUp.toLong()
        if (numUp>0){
            if (numUp-num >= 0.49999){
                num++
            }
        } else{
            if (num-numUp >= 0.49999){
                num--
            }
        }

        return SciAns("${num/dec}<small>×10<sup>$e</sup></small>","${num/dec}E$e", "${num/dec}E$e".toDouble())
    }
    fun Double.roundTo(decimals : Int = 15) : Double{
        val fixed = if (decimals >= 15) 15 else decimals
        val dec = 10.0.pow(fixed)
        val numUp = dec * this
        var num = numUp.toLong()
        if (numUp>0){
            if (numUp-num >= 0.49999){
                num++
            }
        } else{
            if (num-numUp >= 0.49999){
                num--
            }
        }
        println("roundTo")
        println("dec = $dec")
        println("num = $num")
        println("num/dec = ${num/dec}")
        return num/dec
    }
    fun Double.roundToString(decimals : Int) : String {
        val fixed = if (decimals >= 16) 16 else decimals
        val num = (this * 10.0.pow(fixed)).toLong()
        val number = num.toString().dropLast(fixed)
        val decimal = num.toString().takeLast(fixed)
        return if (fixed == 0) number
        else "$number.$decimal"
    }

    val Int.toDp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
    val Int.toPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
    fun Int.pxToDp() : Int = (this / Resources.getSystem().displayMetrics.density).toInt()
    fun Int.dpToPx() : Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    fun LinearLayout.printLatex(latex: String, size: Int = 10) {
//        val mathView = MathView(this.context)
        /*mathView.isClickable = true
        mathView.isFocusable = true*/
//        mathView.setTextSize(size.toPx)
//        mathView.setDisplayText(latex)
//        mathView.setTextColor(this.context.resources.getColor(R.color.primary_color))
//        mathView.setViewBackgroundColor(this.context.resources.getColor(R.color.background))
//        mathView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        addView(mathView)
    }
    fun LinearLayout.printText(text: String, size: Int = 14, paddingVertical : Int = 16) {
        val textView = TextView(context)
        textView.textSize = size.toPx.toFloat()
        textView.text = text
        textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        textView.setPadding(4, paddingVertical.toPx, 4, paddingVertical.toPx)
        addView(textView)
    }

    fun LinearLayout.space(width: Int = 20, height : Int = 20){
        val space = Space(this.context)
        space.layoutParams = ViewGroup.MarginLayoutParams(width,height)
        addView(space)
    }
    fun LinearLayout.line(){
        val line = View(this.context)
        line.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 2)
//        line.setBackgroundColor(getColor(R.color.primary_color))
        addView(line)
        space(height=8)
    }

    fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }
    fun LinearLayout.getColor(@ColorRes id: Int ): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(id, context.theme)
        } else {
            resources.getColor(id)
        }
    }
}