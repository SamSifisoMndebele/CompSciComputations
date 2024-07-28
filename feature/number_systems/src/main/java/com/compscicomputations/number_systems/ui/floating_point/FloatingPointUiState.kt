package com.compscicomputations.number_systems.ui.floating_point

import com.compscicomputations.ui.utils.ProgressState

data class FloatingPointUiState(
    val decimal: String = "",
    val complement1: String = "",
    val complement2: String = "",
    val convertFrom: ConvertFrom = ConvertFrom.Decimal,

    val error: String? = null,

    val progressState: ProgressState = ProgressState.Idle,

    val stepsContent: String = "",
)

enum class ConvertFrom(val text: String) {
    Decimal("Decimal"),
    Complement1("Complement 1"),
    Complement2("Complement 2");

    val isDecimal: Boolean
        get() = this == Decimal
    val isComplement1: Boolean
        get() = this == Complement1
    val isComplement2: Boolean
        get() = this == Complement2
}