package com.compscicomputations.number_systems.ui.floating_point

import com.compscicomputations.ui.utils.ProgressState

data class FloatingPointUiState(
    val decimal: String = "",
    val float8: String = "", // semi IEEE754
    val float16: String = "", // half IEEE754
    val float32: String = "", // single IEEE754
    val float64: String = "", // double IEEE754
    val convertFrom: ConvertFrom = ConvertFrom.Decimal,

    val error: String? = null,

    val progressState: ProgressState = ProgressState.Idle,

    val stepsContent: String = "",
)

enum class ConvertFrom(val text: String) {
    Decimal("Decimal"),
    Float8("Mini precedence"),
    Float16("Half precedence"),
    Float32("Single precedence"),
    Float64("Double precedence");

    val decimal: Boolean
        get() = this == Decimal
    val float8: Boolean
        get() = this == Float8
    val float16: Boolean
        get() = this == Float16
    val float32: Boolean
        get() = this == Float32
    val float64: Boolean
        get() = this == Float64
}