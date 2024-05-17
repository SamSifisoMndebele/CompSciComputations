package com.ssmnd.karnaughmaps

import android.graphics.Color

object Config {
    @JvmField
    var animation = 0
    @JvmField
    val minTermColors = intArrayOf(
        Color.rgb(255, 0, 0),
        Color.rgb(0, 0, 255),
        Color.rgb(255, 0, 255),
        Color.rgb(0, 255, 0),
        Color.rgb(220, 220, 0),
        Color.rgb(0, 255, 255),
        Color.rgb(136, 136, 136),
        Color.rgb(255, 128, 0)
    )
    @JvmField
    val minTermColorsString = arrayOf(
        "\"#FF0000\"",
        "\"#0000FF\"",
        "\"#FF00FF\"",
        "\"#00FF00\"",
        "\"#DCDC00\"",
        "\"#00FFFF\"",
        "\"#888888\"",
        "\"#FF8000\""
    )
}