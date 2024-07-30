package com.compscicomputations.number_systems.ui.complement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.number_systems.ui.bases.BasesUiState
import com.compscicomputations.number_systems.ui.bases.ConvertFrom.Binary
import com.compscicomputations.number_systems.ui.bases.ConvertFrom.Decimal
import com.compscicomputations.number_systems.ui.bases.ConvertFrom.Hexadecimal
import com.compscicomputations.number_systems.ui.bases.ConvertFrom.Octal
import com.compscicomputations.number_systems.ui.bases.ConvertFrom.Unicode
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromComplement1
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromComplement2
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromDecimal
import com.compscicomputations.number_systems.utils.ProgressState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ComplementViewModel(
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

    fun onComplement1Change(complement1Str: String) {
        _uiState.value = _uiState.value.fromComplement1(complement1Str)
            .copy(convertFrom = ConvertFrom.Complement1)
    }

    fun onComplement2Change(complement2Str: String) {
        _uiState.value = _uiState.value.fromComplement2(complement2Str)
            .copy(convertFrom = ConvertFrom.Complement2)
    }

    fun sendPrompt() {
        _uiState.value = _uiState.value.copy(
            progressState = ProgressState.Loading("Loading Steps...")
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
//                        image(bitmap)
                        text(_uiState.value.aiText)
                    }
                )
                response.text?.let { outputContent ->
                    Log.d("sendPrompt", outputContent)
                    _uiState.value = _uiState.value.copy(
                        progressState = ProgressState.Success(outputContent),
                        stepsContent = outputContent
                    )
                } ?: throw Exception("Empty content generated.")
            } catch (e: Exception) {
                Log.w("sendPrompt", e)
                _uiState.value = _uiState.value.copy(
                    progressState = ProgressState.Error(e.localizedMessage)
                )
            }
        }
    }

    private val ComplementUiState.aiText: String
        get() = when(convertFrom) {
            ConvertFrom.Decimal -> TODO()
            ConvertFrom.Complement1 -> TODO()
            ConvertFrom.Complement2 -> TODO()
        }

}