package com.ssmnd.karnaughmap.kMapView

class Alignment {
    @JvmField
    var centerX = 0.5f
    @JvmField
    var centerY = 0.5f
    lateinit var coordinatesToMinermMap: IntArray
    lateinit var drawInversion: IntArray
    lateinit var inversionConversion: IntArray
    @JvmField
    var invertBtnposition = 0f
    @JvmField
    var mapAngle = 0.0f
    @JvmField
    var maxPositionInsideKMapX = 1.0f
    @JvmField
    var maxPositionInsideKMapY = 1.0f
    @JvmField
    var minPositionInsideKMapX = 0.0f
    @JvmField
    var minPositionInsideKMapY = 0.0f
    lateinit var posX: FloatArray
    lateinit var posY: FloatArray
    @JvmField
    var rectHeight = 0f
    lateinit var rectPosX: FloatArray
    lateinit var rectPosY: FloatArray
    @JvmField
    var rectWidth = 0f
}