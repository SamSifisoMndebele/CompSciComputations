package com.compscicomputations.number_systems.ui.excess

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromDecimal
import com.compscicomputations.number_systems.ui.complement.ComplementUiState
import com.compscicomputations.number_systems.ui.complement.ConvertFrom
import com.compscicomputations.ui.utils.ProgressState
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExcessViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComplementUiState())
    val uiState = _uiState.asStateFlow()

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
    }

    fun setProgressState(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }

    fun onDecimalChange(decimalStr: String) {
        _uiState.value = _uiState.value.fromDecimal(decimalStr)
            .copy(convertFrom = ConvertFrom.Decimal)
    }

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