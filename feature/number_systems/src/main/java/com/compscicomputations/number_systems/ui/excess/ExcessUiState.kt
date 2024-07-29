package com.compscicomputations.number_systems.ui.excess

import com.compscicomputations.ui.utils.ProgressState

data class ExcessUiState(
    val decimal: String = "",
    val excess: String = "",
    val excessIdentifier: String = "",
    val excessBits: Int = 8,
    val error: String? = null,

    val convertFrom: ConvertFrom = ConvertFrom.Decimal,

    val progressState: ProgressState = ProgressState.Idle,

    val stepsContent: String = "",
)

enum class ConvertFrom(val text: String) {
    Decimal("Decimal"),
    Excess("Excess notation");

    val decimal: Boolean
        get() = this == Decimal
    val excess: Boolean
        get() = this == Excess
}