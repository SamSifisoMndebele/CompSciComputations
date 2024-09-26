package com.compscicomputations.karnaugh_maps.data.model

import androidx.compose.runtime.Stable

@Stable
enum class ConvertFrom(val text: String) {
    Map("Map Selection"),
    Expression("Expression"),
    ;

    val isMap: Boolean
        get() = this == Map
    val isExpression: Boolean
        get() = this == Expression
}