package com.compscicomputations.number_systems.ui.excess

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.number_systems.data.model.AIState
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.ConvertFrom.Binary
import com.compscicomputations.number_systems.data.model.ConvertFrom.Decimal
import com.compscicomputations.number_systems.data.model.ConvertFrom.Excess
import com.compscicomputations.number_systems.data.model.ConvertFrom.Hexadecimal
import com.compscicomputations.number_systems.data.model.ConvertFrom.Octal
import com.compscicomputations.number_systems.data.model.ConvertFrom.Unicode
import com.compscicomputations.number_systems.data.model.CurrentTab
import com.compscicomputations.number_systems.data.model.CurrentTab.*
import com.compscicomputations.number_systems.data.source.local.AiResponse
import com.compscicomputations.number_systems.data.source.local.AiResponseDao
import com.compscicomputations.number_systems.data.source.local.datastore.ExcessDataStore
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromBinary
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromDecimal
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromHex
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromOctal
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromUnicode
import com.compscicomputations.number_systems.ui.excess.ExcessConverter.fromDecimal
import com.compscicomputations.number_systems.ui.excess.ExcessConverter.fromExcess
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ServerException
import com.google.ai.client.generativeai.type.content
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

class ExcessViewModel(
    private val generativeModel: GenerativeModel,
    private val aiResponseDao: AiResponseDao,
    private val dataStore: ExcessDataStore,
) : ViewModel() {
    init {
        viewModelScope.launch {
            dataStore.bitLength.first()
                ?.let {
                    _uiState.value = _uiState.value.copy(bits = it)
                }
            dataStore.lastState.first()
                ?.let {
                    when(it.convertFrom) {
                        Decimal -> _uiState.value = _uiState.value.fromDecimal(it.fromValue)
                            .copy(convertFrom = Decimal)
                        Excess -> _uiState.value = _uiState.value.fromExcess(it.fromValue)
                            .copy(convertFrom = Excess)
                        else -> {}
                    }
                }
        }
    }
    private val _uiState = MutableStateFlow(ExcessUiState())
    val uiState = _uiState.asStateFlow()

    fun clear() {
        _uiState.value = ExcessUiState(convertFrom = _uiState.value.convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLastState(_uiState.value.convertFrom, "")
        }
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLastState(convertFrom, _uiState.value.fromValue)
        }
    }

    fun setAiState(aiState: AIState) {
        _uiState.value = _uiState.value.copy(aiState = aiState)
    }

    fun setExcessBits(excessBits: Int) {
        when(_uiState.value.convertFrom) {
            Decimal -> _uiState.value = _uiState.value.copy(bits = excessBits, error = null)
                .fromDecimal(_uiState.value.decimal)
            Excess -> _uiState.value = _uiState.value.copy(bits = excessBits, error = null)
                .fromExcess(_uiState.value.decimal)
            else -> {}
        }
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setBits(excessBits)
        }
    }

    fun onDecimalChange(decimalStr: String) {
        _uiState.value = _uiState.value.fromDecimal(decimalStr)
            .copy(convertFrom = Decimal)
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLastState(Decimal, decimalStr)
        }
    }

    fun onExcessChange(excessStr: String) {
        _uiState.value = _uiState.value.fromExcess(excessStr)
            .copy(convertFrom = Excess)
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLastState(Excess, excessStr)
        }
    }


    val responsesFlow: Flow<List<AiResponse>?>
        get() = aiResponseDao.selectAll(CurrentTab.Excess).map {
            if (it.isNullOrEmpty()) null
            else it
        }

    fun deleteResponse(response: AiResponse) {
        viewModelScope.launch {
            aiResponseDao.delete(response)
        }
    }

    fun setFields(response: AiResponse) {
        when(response.convertFrom) {
            Decimal -> _uiState.value = _uiState.value.fromDecimal(response.value)
                .copy(convertFrom = Decimal)
            Excess -> _uiState.value = _uiState.value.fromExcess(response.value)
                .copy(convertFrom = Excess)
            else -> {}
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


    private suspend fun generateContent(): AiResponse {
        val response = generativeModel.generateContent(
            content {
                text(_uiState.value.aiText)
            }
        )
        return response.text?.let { outputContent ->
            Log.d("AI::response", outputContent)
            AiResponse(
                id = 0,
                tab = CurrentTab.Excess,
                convertFrom = _uiState.value.convertFrom,
                value = _uiState.value.fromValue,
                text = outputContent
            )
        } ?: throw Exception("Empty content generated.")
    }

    fun generateSteps() {
        setAiState(AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val aiResponse = aiResponseDao.select(CurrentTab.Excess, _uiState.value.convertFrom, _uiState.value.fromValue)
                if (aiResponse != null) {
                    setAiState(AIState.Success(aiResponse))
                    return@launch
                }
                val newAiResponse = generateContent()
                setAiState(AIState.Success(newAiResponse))
                aiResponseDao.insert(newAiResponse)
            }catch (e: Exception) {
                Log.w("AI::error", e)
                when {
                    e.cause is CancellationException -> setAiState(AIState.Idle)
                    e is ServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
                    else -> setAiState(AIState.Error(e))
                }
            }
        }
    }

    fun regenerateSteps() {
        val currentResponse: AiResponse = (_uiState.value.aiState as AIState.Success).response
        setAiState(AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val newAiResponse = generateContent()
                setAiState(AIState.Success(newAiResponse))
                aiResponseDao.insert(newAiResponse)
            } catch (e: Exception) {
                Log.w("AI::error", e)
                when {
                    e.cause is CancellationException -> setAiState(AIState.Idle)
                    e is ServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
                    else -> setAiState(AIState.Error(e))
                }
                delay(5000)
                setAiState(AIState.Success(currentResponse))
            }
        }

    }

    companion object {

        val ExcessUiState.fromValue: String
            get() = when(convertFrom) {
                Decimal -> decimal
                Excess -> excess
                else -> throw IllegalArgumentException("Invalid option.")
            }    
        
        private val ExcessUiState.aiText: String
            get() = when(convertFrom) {
                Decimal -> "Show me the steps to convert the decimal: $decimal to excess notation, using $bits bits."
                Excess -> "Show me the steps to convert $bits bits excess notation: $decimal to decimal"
                else -> throw IllegalArgumentException("Invalid option.")
            }
    }
}