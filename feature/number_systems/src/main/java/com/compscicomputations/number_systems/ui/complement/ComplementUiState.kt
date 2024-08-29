package com.compscicomputations.number_systems.ui.complement

import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.ui.main.dynamic_feature.AIState

data class ComplementUiState(
    val decimal: String = "",
    val complement1: String = "",
    val complement2: String = "",

    val error: String? = null,
    val convertFrom: ConvertFrom = ConvertFrom.Decimal,
    val aiState: AIState = AIState.Idle,
) {
    val isValid: Boolean
        get() = decimal.isNotBlank()
}
