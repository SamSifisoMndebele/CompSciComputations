package com.ssmnd.karnaughmap.keyboard

import androidx.annotation.DrawableRes
import com.ssmnd.karnaughmap.R

sealed class Key(
    val value: Char,
    @DrawableRes val iconId: Int,
) {
    data object A : Key('A', R.drawable.ic_a)
    data object B : Key('B', R.drawable.ic_b)
    data object C : Key('C', R.drawable.ic_c)
    data object D : Key('D', R.drawable.ic_d)
    data object Or : Key('+', R.drawable.ic_plus)
    data object Not : Key('\'', R.drawable.ic_not)
    data object Left : Key('l', R.drawable.ic_angle_left)
    data object Right : Key('r', R.drawable.ic_angle_right)
    data object Backspace : Key('b', R.drawable.ic_backspace)
}