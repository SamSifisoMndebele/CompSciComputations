package com.ssmnd.karnaughmap.kMapView

import android.content.Context
import android.util.AttributeSet
import com.ssmnd.karnaughmap.R
import com.ssmnd.karnaughmap.utils.ListOfMinterms

class KMap3VariablesImageView :
    KMapVariablesImageView {
    private fun setParameters() {
        minTermsString = arrayOf("0", "0", "0", "0", "0", "0", "0", "0")
        allMinTerms = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
        alignment.minPositionInsideKMapX = 0.15f
        alignment.minPositionInsideKMapY = 0.2f
        alignment.posX = floatArrayOf(0.21f, 0.41f, 0.81f, 0.61f, 0.21f, 0.41f, 0.81f, 0.61f)
        alignment.posY = floatArrayOf(0.46f, 0.46f, 0.46f, 0.46f, 0.8f, 0.8f, 0.8f, 0.8f)
        alignment.rectPosX = floatArrayOf(0.145f, 0.345f, 0.745f, 0.545f, 0.145f, 0.345f, 0.745f, 0.545f)
        alignment.rectPosY = floatArrayOf(0.25f, 0.25f, 0.25f, 0.25f, 0.58f, 0.58f, 0.58f, 0.58f)
        alignment.rectWidth = 0.2f
        alignment.rectHeight = 0.33f
        listOfMinTermsToDraw = ListOfMinterms(3)
        alignment.inversionConversion = intArrayOf(0, 2, 4, 6, 1, 3, 5, 7)
        alignment.drawInversion = intArrayOf(0, 4, 1, 5, 2, 6, 3, 7)
        alignment.invertBtnposition = 0.15f
        alignment.centerX = 0.54f
        alignment.centerY = 0.58f
        imageResource = R.drawable.kmap_3_var_abc
        imageResourceMapInverted = R.drawable.kmap_3_var_cab
        mapInverted = true
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