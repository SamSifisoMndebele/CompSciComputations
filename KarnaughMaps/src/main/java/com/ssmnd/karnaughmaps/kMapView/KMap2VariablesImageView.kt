package com.ssmnd.karnaughmaps.kMapView

import android.content.Context
import android.util.AttributeSet
import com.ssmnd.karnaughmaps.R
import com.ssmnd.karnaughmaps.utils.ListOfMinterms

class KMap2VariablesImageView :
    KMapVariablesImageView {
    private fun setParameters() {
        minTermsString = arrayOf("0", "0", "0", "0")
        allMinTerms = intArrayOf(0, 1, 2, 3)
        alignment.minPositionInsideKMapX = 0.15f
        alignment.minPositionInsideKMapY = 0.2f
        alignment.posX = floatArrayOf(0.21f, 0.41f, 0.81f, 0.61f)
        alignment.posY = floatArrayOf(0.73f, 0.73f, 0.73f, 0.73f)
        alignment.rectPosX = floatArrayOf(0.145f, 0.345f, 0.745f, 0.545f)
        alignment.rectPosY = floatArrayOf(0.36f, 0.36f, 0.36f, 0.36f)
        alignment.rectWidth = 0.2f
        alignment.rectHeight = 0.53f
        alignment.centerX = 0.54f
        alignment.centerY = 0.63f
        listOfMinTermsToDraw = ListOfMinterms(2)
        imageResource = R.drawable.kmap_2_var_ab
    }

    constructor(context: Context?) : super(context) {
        setParameters()
    }

    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet) {
        setParameters()
    }

    constructor(context: Context?, attributeSet: AttributeSet?, i: Int) : super(
        context,
        attributeSet,
        i
    ) {
        setParameters()
    }
}