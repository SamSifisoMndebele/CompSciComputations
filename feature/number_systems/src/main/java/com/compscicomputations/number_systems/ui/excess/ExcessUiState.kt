package com.compscicomputations.number_systems.ui.excess

import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.AIState

data class ExcessUiState(
    val decimal: String = "",
    val excess: String = "",
    val excessIdentifier: String = "",
    val excessBits: Int = 8,

    val error: String? = null,

    val convertFrom: ConvertFrom = ConvertFrom.Decimal,

    val aiState: AIState = AIState.Idle,
) {
    val isValid: Boolean
        get() = decimal.isNotBlank()
}