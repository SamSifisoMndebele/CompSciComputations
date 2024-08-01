package com.compscicomputations.number_systems.ui.bases

import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.AIState

data class BasesUiState(
    val decimal: String = "",
    val binary: String = "",
    val octal: String = "",
    val hexadecimal: String = "",
    val unicode: String = "",

    val error: String? = null,
    val convertFrom: ConvertFrom = ConvertFrom.Decimal,
    val aiState: AIState = AIState.Idle
) {
    val isValid: Boolean
        get() = decimal.isNotBlank()
//                && binary.isNotBlank()
//                && octal.isNotBlank()
//                && hexadecimal.isNotBlank()
//                && unicode.isNotBlank()
}

