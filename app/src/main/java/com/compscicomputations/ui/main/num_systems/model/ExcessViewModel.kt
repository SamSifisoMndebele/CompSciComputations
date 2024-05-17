package com.compscicomputations.ui.main.num_systems.model

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ExcessViewModel : ViewModel() {
    var decimal = mutableStateOf("")
        private set
    var excess = mutableStateOf("")
        private set
    var excessIdentifier = mutableStateOf("")
        private set
    var excessBits = mutableIntStateOf(8)
        private set
    var error = mutableStateOf<com.compscicomputations.logic.num_systems.excess.ExcessError?>(null)
        private set

    fun setExcessBits(excessBits: String) {
        try {
            this.excessBits.intValue = excessBits.toInt()
            error.value = null
        } catch (e: Exception) {
            error.value = com.compscicomputations.logic.num_systems.excess.ExcessError.INVALID_EXCESS_BITS
        }
    }

    fun fromDecimal(decimal: String) {
        val fromDecimal = com.compscicomputations.logic.num_systems.excess.ExcessConverter.fromDecimal(decimal, excessBits.intValue)

        this.decimal.value = decimal
        excess.value = fromDecimal.excess

        error.value = fromDecimal.error
    }

    fun fromExcess(excess: String) {
        val fromExcess = com.compscicomputations.logic.num_systems.excess.ExcessConverter.fromExcess(excess, excessBits.intValue)

        this.excess.value = excess
        decimal.value = fromExcess.decimal

        error.value = fromExcess.error
    }
}