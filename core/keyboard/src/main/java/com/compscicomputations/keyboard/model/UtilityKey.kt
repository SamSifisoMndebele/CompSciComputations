package com.compscicomputations.keyboard.model

import androidx.annotation.DrawableRes
import com.compscicomputations.keyboard.R

sealed class UtilityKey(
    @DrawableRes val iconId: Int,
    override val value: String,
    override val span: Int = 2
) : Key {
    data object Or : UtilityKey(R.drawable.ic_plus, "OR")
    data object Not : UtilityKey(R.drawable.ic_not, "NOT")
    data object Backspace : UtilityKey(R.drawable.ic_backspace, "BACKSPACE_KEY")
    data object ArrowLeft : UtilityKey(R.drawable.ic_angle_left, "ARROW_LEFT_KEY", 1)
    data object ArrowRight : UtilityKey(R.drawable.ic_angle_right, "ARROW_RIGHT_KEY", 1)
    data object Action : UtilityKey(R.drawable.ic_angle_right, "ACTION")
}