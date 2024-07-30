package com.compscicomputations.number_systems.ui.complement

import com.compscicomputations.number_systems.utils.ProgressState

data class ComplementUiState(
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

    val decimal: Boolean
        get() = this == Decimal
    val complement1: Boolean
        get() = this == Complement1
    val complement2: Boolean
        get() = this == Complement2
}