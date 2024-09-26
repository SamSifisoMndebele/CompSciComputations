package com.compscicomputations.karnaugh_maps.kMapView

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.compscicomputations.karnaugh_maps.utils.Config

class MinTermTextView : AppCompatTextView {
    private var animation = false
    private var animationPart = 0
    private var sop = true
    fun setSop(z: Boolean) {
        sop = z
    }

    constructor(context: Context?) : super(context!!)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context?, attributeSet: AttributeSet?, i: Int) : super(
        context!!, attributeSet, i
    )

    fun setAnimation(z: Boolean) {
        animation = z
    }

    fun setAnimationPart(i: Int) {
        animationPart = i
    }

    fun setText(str: String) {
        var expression: String
        var beforeEqual: String
        val sumOfProducts = StringBuilder()
        val andOr = if (sop) "+" else "·"
        val split = str.replace("'", "&#772;").split(" = ".toRegex()).toTypedArray()
        try {
            beforeEqual = split[0]
            expression = split[1]
        } catch (unused: Exception) {
            beforeEqual = "S"
            expression = sumOfProducts.toString()
        }

        val products = expression.split(andOr)

        var length = products.size
        if (animation) {
            length = animationPart
        }
        var i = 0
        for (j in 0 until length) {
            if (i == Config.minTermColorsString.size) {
                i = 0
            }
            if (j > 0) {
                sumOfProducts.append(" ")
                    .append(andOr)
                    .append(" ")
            }
            if (products[j] == "0") {
                sumOfProducts.append("0")
            } else {
                sumOfProducts.append("<font color=")
                    .append(Config.minTermColorsString[i])
                    .append(">")
                    .append(products[j])
                    .append("</font>")
            }
            i++
        }
        super.setText(Html.fromHtml("$beforeEqual = $sumOfProducts",Html.FROM_HTML_MODE_LEGACY))
    }
}