package com.compscicomputations.number_systems.ui.excess

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.data.source.local.AiResponse
import com.compscicomputations.data.source.local.AiResponseDao
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.ConvertFrom.Decimal
import com.compscicomputations.number_systems.data.model.ConvertFrom.Excess
import com.compscicomputations.number_systems.data.model.CurrentTab
import com.compscicomputations.number_systems.data.source.local.datastore.ExcessDataStore
import com.compscicomputations.number_systems.ui.excess.ExcessConverter.fromDecimal
import com.compscicomputations.number_systems.ui.excess.ExcessConverter.fromExcess
import com.compscicomputations.number_systems.utils.MODULE_NAME
import com.compscicomputations.ui.main.dynamic_feature.AIState
import com.compscicomputations.ui.main.dynamic_feature.GenerateContentUseCase
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
    private val context: Context,
    private val generateContent: GenerateContentUseCase,
    private val aiResponseDao: AiResponseDao,
) : ViewModel() {
    init {
        viewModelScope.launch {
            ExcessDataStore.bitLength(context).first()
                ?.let {
                    _uiState.value = _uiState.value.copy(bits = it)
                }
            ExcessDataStore.lastState(context).first()
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
            ExcessDataStore.setLastState(context, _uiState.value.convertFrom, "")
        }
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            ExcessDataStore.setLastState(context, convertFrom, _uiState.value.fromValue)
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
            ExcessDataStore.setBits(context, excessBits)
        }
    }

    fun onDecimalChange(decimalStr: String) {
        _uiState.value = _uiState.value.fromDecimal(decimalStr)
            .copy(convertFrom = Decimal)
        viewModelScope.launch(Dispatchers.IO) {
            ExcessDataStore.setLastState(context, Decimal, decimalStr)
        }
    }

    fun onExcessChange(excessStr: String) {
        _uiState.value = _uiState.value.fromExcess(excessStr)
            .copy(convertFrom = Excess)
        viewModelScope.launch(Dispatchers.IO) {
            ExcessDataStore.setLastState(context, Excess, excessStr)
        }
    }


    val responsesFlow: Flow<List<AiResponse>?>
        get() = aiResponseDao.selectAll(MODULE_NAME, CurrentTab.Excess.name).map {
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

    private suspend fun generateContent(): AiResponse = generateContent(
        MODULE_NAME,
        CurrentTab.Excess.name,
        _uiState.value.convertFrom.name,
        _uiState.value.fromValue,
        _uiState.value.aiText
    )

    fun generateSteps() {
        setAiState(AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val aiResponse = aiResponseDao.select(MODULE_NAME, CurrentTab.Excess.name, _uiState.value.convertFrom.name, _uiState.value.fromValue)
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
                    e is GenerateContentUseCase.AiServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
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
                    e is GenerateContentUseCase.AiServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
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