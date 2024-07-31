package com.compscicomputations.number_systems.ui.floating_point

import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.AIState

data class FloatingPointUiState(
    val decimal: String = "",
//    val miniFloat: String = "", // mini float (1,4,3)
    val binary16: String = "", // half IEEE754 (1,5,10)
    val binary32: String = "", // single IEEE754 (1,8,23)
    val binary64: String = "", // double IEEE754 (1,11,52)

    val error: String? = null,

    val convertFrom: ConvertFrom = ConvertFrom.Decimal,

    val aiState: AIState = AIState.Idle
) {
    val isValid: Boolean
        get() = decimal.isNotBlank()
}