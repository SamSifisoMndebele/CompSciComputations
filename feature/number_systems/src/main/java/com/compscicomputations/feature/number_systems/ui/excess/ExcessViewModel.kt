package com.compscicomputations.feature.number_systems.ui.excess

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
    var error = mutableStateOf<ExcessError?>(null)
        private set

    fun setExcessBits(excessBits: String) {
        try {
            this.excessBits.intValue = excessBits.toInt()
            error.value = null
        } catch (e: Exception) {
            error.value = ExcessError.INVALID_EXCESS_BITS
        }
    }

    fun fromDecimal(decimal: String) {
        val fromDecimal = ExcessConverter.fromDecimal(decimal, excessBits.intValue)

        this.decimal.value = decimal
        excess.value = fromDecimal.excess

        error.value = fromDecimal.error
    }

    fun fromExcess(excess: String) {
        val fromExcess = ExcessConverter.fromExcess(excess, excessBits.intValue)

        this.excess.value = excess
        decimal.value = fromExcess.decimal

        error.value = fromExcess.error
    }
}