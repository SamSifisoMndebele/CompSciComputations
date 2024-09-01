package com.compscicomputations.number_systems.ui.bases

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.data.source.local.AiResponse
import com.compscicomputations.data.source.local.AiResponseDao
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.ConvertFrom.Binary
import com.compscicomputations.number_systems.data.model.ConvertFrom.Decimal
import com.compscicomputations.number_systems.data.model.ConvertFrom.Hexadecimal
import com.compscicomputations.number_systems.data.model.ConvertFrom.Octal
import com.compscicomputations.number_systems.data.model.ConvertFrom.Unicode
import com.compscicomputations.number_systems.data.model.CurrentTab.BaseN
import com.compscicomputations.number_systems.data.source.local.datastore.BaseNDataStore
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromBinary
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromDecimal
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromHex
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromOctal
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromUnicode
import com.compscicomputations.number_systems.utils.BinaryArithmetic.padBits
import com.compscicomputations.number_systems.utils.MODULE_NAME
import com.compscicomputations.ui.main.dynamic_feature.AIState
import com.compscicomputations.ui.main.dynamic_feature.GenerateContentUseCase
import com.compscicomputations.ui.main.dynamic_feature.GenerateContentUseCase.AiServerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class BasesViewModel(
    private val context: Context,
    private val generateContent: GenerateContentUseCase,
    val aiResponseDao: AiResponseDao,
) : ViewModel() {
    init {
        viewModelScope.launch {
            BaseNDataStore.lastState(context).first()
                ?.let {
                    when(it.convertFrom) {
                        Decimal -> _uiState.value = _uiState.value.fromDecimal(it.fromValue)
                            .copy(convertFrom = Decimal)
                        Binary -> _uiState.value = _uiState.value.fromBinary(it.fromValue)
                            .copy(convertFrom = Binary)
                        Octal -> _uiState.value = _uiState.value.fromOctal(it.fromValue)
                            .copy(convertFrom = Octal)
                        Hexadecimal -> _uiState.value = _uiState.value.fromHex(it.fromValue)
                            .copy(convertFrom = Hexadecimal)
                        Unicode -> _uiState.value = _uiState.value.fromUnicode(it.fromValue)
                            .copy(convertFrom = Unicode)
                        else -> {}
                    }
                }
        }

    }

    private val _uiState = MutableStateFlow(BasesUiState())
    val uiState = _uiState.asStateFlow()

    fun setAiState(aiState: AIState) {
        _uiState.value = _uiState.value.copy(aiState = aiState)
    }

    fun clear() {
        _uiState.value = BasesUiState(convertFrom = _uiState.value.convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            BaseNDataStore.setLastState(context, _uiState.value.convertFrom, "")
        }
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            BaseNDataStore.setLastState(context, convertFrom, _uiState.value.fromValue)
        }
    }
    fun onDecimalChange(decimalStr: String) {
        _uiState.value = _uiState.value.fromDecimal(decimalStr)
            .copy(convertFrom = Decimal)
        viewModelScope.launch(Dispatchers.IO) {
            BaseNDataStore.setLastState(context, Decimal, decimalStr)
        }
    }

    fun onBinaryChange(binaryStr: String) {
        _uiState.value = _uiState.value.fromBinary(binaryStr)
            .copy(convertFrom = Binary)
        viewModelScope.launch(Dispatchers.IO) {
            BaseNDataStore.setLastState(context, Binary, binaryStr)
        }
    }

    fun onOctalChange(octalStr: String) {
        _uiState.value = _uiState.value.fromOctal(octalStr)
            .copy(convertFrom = Octal)
        viewModelScope.launch(Dispatchers.IO) {
            BaseNDataStore.setLastState(context, Octal, octalStr)
        }
    }

    fun onHexadecimalChange(hexadecimalStr: String) {
        _uiState.value = _uiState.value.fromHex(hexadecimalStr)
            .copy(convertFrom = Hexadecimal)
        viewModelScope.launch(Dispatchers.IO) {
            BaseNDataStore.setLastState(context, Hexadecimal, hexadecimalStr)
        }
    }

    fun onUnicodeChange(unicodeStr: String) {
        _uiState.value = _uiState.value.fromUnicode(unicodeStr)
            .copy(convertFrom = Unicode)
        viewModelScope.launch(Dispatchers.IO) {
            BaseNDataStore.setLastState(context, Unicode, unicodeStr)
        }
    }

    private var job: Job? = null
    @OptIn(InternalCoroutinesApi::class)
    fun cancelJob(handler: () -> Unit = {}) {
        job?.cancel()
        job?.invokeOnCompletion(true) {
            job = null
            setAiState(AIState.Idle)
            handler()
        }
    }

    val responsesFlow: Flow<List<AiResponse>?>
        get() = aiResponseDao.selectAll(MODULE_NAME, BaseN.name).map {
            if (it.isNullOrEmpty()) null
            else it
        }

    fun deleteResponse(response: AiResponse) {
        viewModelScope.launch {
            aiResponseDao.delete(response)
        }
    }

    fun setFields(response: AiResponse) {
        when(ConvertFrom.valueOf(response.convertFrom)) {
            Decimal -> _uiState.value = _uiState.value.fromDecimal(response.value)
                .copy(convertFrom = Decimal)
            Binary -> _uiState.value = _uiState.value.fromBinary(response.value)
                .copy(convertFrom = Binary)
            Octal -> _uiState.value = _uiState.value.fromOctal(response.value)
                .copy(convertFrom = Octal)
            Hexadecimal -> _uiState.value = _uiState.value.fromHex(response.value)
                .copy(convertFrom = Hexadecimal)
            Unicode -> _uiState.value = _uiState.value.fromUnicode(response.value)
                .copy(convertFrom = Unicode)
            else -> {}
        }
    }

    private suspend fun generateContent(): AiResponse = generateContent(
        MODULE_NAME,
        BaseN.name,
        _uiState.value.convertFrom.name,
        _uiState.value.fromValue,
        _uiState.value.aiText + "\n\nUse UTF-16 for unicode characters encoding.\n" +
                "Do Not group binary digits because you are not grouping correct."
    )

    fun generateSteps() {
        setAiState(AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val aiResponse = aiResponseDao.select(MODULE_NAME, BaseN.name, _uiState.value.convertFrom.name, _uiState.value.fromValue)
                if (aiResponse != null) {
                    setAiState(AIState.Success(aiResponse))
                    return@launch
                }
                val newAiResponse = generateContent()
                setAiState(AIState.Success(newAiResponse))
                aiResponseDao.insert(newAiResponse)
            } catch (e: Exception) {
                Log.w("AI::error", e)
                when {
                    e.cause is CancellationException -> setAiState(AIState.Idle)
                    e is AiServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
                    else -> setAiState(AIState.Error(e))
                }
            }
        }
    }

    fun regenerateSteps() {
        val currentResponse: AiResponse = (_uiState.value.aiState as AIState.Success).response
        setAiState(AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch {
            try {
                val newAiResponse = generateContent()
                setAiState(AIState.Success(newAiResponse))
                aiResponseDao.insert(newAiResponse)
            } catch (e: Exception) {
                Log.w("AI::error", e)
                when {
                    e.cause is CancellationException -> setAiState(AIState.Idle)
                    e is AiServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
                    else -> setAiState(AIState.Error(e))
                }
                delay(5000)
                setAiState(AIState.Success(currentResponse))
            }
        }
    }

    companion object {
        val BasesUiState.fromValue: String
            get() = when(convertFrom) {
                Decimal -> decimal.trim()
                Binary -> binary.trim()
                Octal -> octal.trim()
                Hexadecimal -> hexadecimal.trim()
                Unicode -> unicode.trim()
                else -> throw IllegalArgumentException("Invalid option.")
            }

        private val BasesUiState.aiText: String
            get() = when(convertFrom) {
                Decimal -> "Show me steps how to convert the decimal: $decimal to binary, octal, hexadecimal, and unicode character."
                Binary -> "Show me steps how to convert the binary: ${binary.padBits()} to decimal, octal, hexadecimal, and unicode character."
                Octal -> "Show me steps how to convert the octal: $octal to decimal, binary, hexadecimal, and unicode character."
                Hexadecimal -> "Show me steps how to convert the hexadecimal: $hexadecimal to decimal, binary, octal, and unicode character."
                Unicode -> "Show me steps how to convert the unicode character: $unicode to decimal, binary, octal, and hexadecimal."
                else -> throw IllegalArgumentException("Invalid option.")
            }
    }
}