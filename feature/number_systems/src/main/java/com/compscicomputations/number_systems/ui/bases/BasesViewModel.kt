package com.compscicomputations.number_systems.ui.bases

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromAscii
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromBinary
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromDecimal
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromHex
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromOctal
import com.compscicomputations.ui.utils.ProgressState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.mlkit.vision.text.TextRecognizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BasesViewModel(
    private val generativeModel: GenerativeModel,
    private val textRecognizer: TextRecognizer
) : ViewModel() {
    private val _uiState = MutableStateFlow(BasesUiState())
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

    fun onBinaryChange(binaryStr: String) {
        _uiState.value = _uiState.value.fromBinary(binaryStr)
            .copy(convertFrom = ConvertFrom.Binary)
    }

    fun onOctalChange(octalStr: String) {
        _uiState.value = _uiState.value.fromOctal(octalStr)
            .copy(convertFrom = ConvertFrom.Octal)
    }

    fun onHexadecimalChange(hexadecimalStr: String) {
        _uiState.value = _uiState.value.fromHex(hexadecimalStr)
            .copy(convertFrom = ConvertFrom.Hexadecimal)
    }

    fun fromAscii(asciiStr: String) {
        _uiState.value = _uiState.value.fromAscii(asciiStr)
            .copy(convertFrom = ConvertFrom.ASCII)
    }

    fun sendPrompt() {
        _uiState.value = _uiState.value.copy(
            progressState = ProgressState.Loading("Loading Steps...")
        )
        val text: String = when(_uiState.value.convertFrom) {
            ConvertFrom.Decimal -> "Show me steps how to convert decimal number: ${_uiState.value.decimal} to binary, octal, hexadecimal, and ascii"
            ConvertFrom.Binary -> "Show me steps how to convert binary number: ${_uiState.value.decimal} to decimal, octal, hexadecimal, and ascii"
            ConvertFrom.Octal -> "Show me steps how to convert octal number: ${_uiState.value.decimal} to decimal, binary, hexadecimal, and ascii"
            ConvertFrom.Hexadecimal -> "Show me steps how to convert hexadecimal number: ${_uiState.value.decimal} to decimal, binary, octal, and ascii"
            ConvertFrom.ASCII -> "Show me steps how to convert ascii character: ${_uiState.value.decimal} to decimal, binary, octal, and hexadecimal"
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
//                        image(bitmap)
                        text(text)
                    }
                )
                response.text?.let { outputContent ->
                    Log.d("sendPrompt", outputContent)
                    _uiState.value = _uiState.value.copy(
                        progressState = ProgressState.Success,
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

}