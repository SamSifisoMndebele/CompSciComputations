package com.compscicomputations.feature.karnaugh_maps.kMapView

import android.content.Context
import android.util.AttributeSet
import com.compscicomputations.feature.karnaugh_maps.R
import com.compscicomputations.feature.karnaugh_maps.utils.ListOfMinterms

class KMap4VariablesImageView : KMapVariablesImageView {
    private fun setParameters() {
        minTermsString =
            arrayOf("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0")
        allMinTerms = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
        alignment.minPositionInsideKMapX = 0.15f
        alignment.minPositionInsideKMapY = 0.2f
        alignment.posX = floatArrayOf(
            0.21f,
            0.41f,
            0.81f,
            0.61f,
            0.21f,
            0.41f,
            0.81f,
            0.61f,
            0.21f,
            0.41f,
            0.81f,
            0.61f,
            0.21f,
            0.41f,
            0.81f,
            0.61f
        )
        alignment.posY = floatArrayOf(
            0.28f,
            0.28f,
            0.28f,
            0.28f,
            0.48f,
            0.48f,
            0.48f,
            0.48f,
            0.88f,
            0.88f,
            0.88f,
            0.88f,
            0.68f,
            0.68f,
            0.68f,
            0.68f
        )
        alignment.rectPosX = floatArrayOf(
            0.145f,
            0.345f,
            0.745f,
            0.545f,
            0.145f,
            0.345f,
            0.745f,
            0.545f,
            0.145f,
            0.345f,
            0.745f,
            0.545f,
            0.145f,
            0.345f,
            0.745f,
            0.545f
        )
        alignment.rectPosY = floatArrayOf(
            0.145f,
            0.145f,
            0.145f,
            0.145f,
            0.345f,
            0.345f,
            0.345f,
            0.345f,
            0.745f,
            0.745f,
            0.745f,
            0.745f,
            0.545f,
            0.545f,
            0.545f,
            0.545f
        )
        alignment.rectWidth = 0.2f
        alignment.rectHeight = 0.2f
        listOfMinTermsToDraw = ListOfMinterms(4)
        alignment.inversionConversion =
            intArrayOf(0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15)
        alignment.drawInversion = intArrayOf(0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15)
        alignment.invertBtnposition = 0.11f
        alignment.centerX = 0.54f
        alignment.centerY = 0.54f
        imageResource = R.drawable.kmap_4_var_abcd
        imageResourceMapInverted = R.drawable.kmap_4_var_cdab
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