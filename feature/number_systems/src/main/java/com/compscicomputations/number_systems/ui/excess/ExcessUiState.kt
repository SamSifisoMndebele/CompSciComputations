package com.compscicomputations.number_systems.ui.excess

import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.ui.main.dynamic_feature.AIState
import kotlin.math.pow

data class ExcessUiState(
    val bits: Int = 8,
    val decimal: String = "",
    val excess: String = "",

    val error: String? = null,
    val convertFrom: ConvertFrom = ConvertFrom.Decimal,
    val aiState: AIState = AIState.Idle,
) {
    val bias: Long
        get() = 2.0.pow(bits - 1).toLong()
    val min: Long
        get() = -bias
    val max: Long
        get() = bias - 1


    val isValid: Boolean
        get() = decimal.isNotBlank()
}