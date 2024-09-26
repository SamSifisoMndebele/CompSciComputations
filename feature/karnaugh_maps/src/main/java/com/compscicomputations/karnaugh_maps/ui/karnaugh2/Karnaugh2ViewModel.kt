package com.compscicomputations.karnaugh_maps.ui.karnaugh2

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.data.source.local.AiResponse
import com.compscicomputations.data.source.local.AiResponseDao
import com.compscicomputations.karnaugh_maps.data.model.ConvertFrom
import com.compscicomputations.karnaugh_maps.data.model.ConvertFrom.Expression
import com.compscicomputations.karnaugh_maps.data.model.ConvertFrom.Map
import com.compscicomputations.karnaugh_maps.data.model.CurrentTab.FourVars
import com.compscicomputations.karnaugh_maps.data.source.local.datastore.TwoVarsDataStore
import com.compscicomputations.karnaugh_maps.logic.Karnaugh2Variables
import com.compscicomputations.karnaugh_maps.ui.MODULE_NAME
import com.compscicomputations.karnaugh_maps.ui.arrayList
import com.compscicomputations.karnaugh_maps.ui.position
import com.compscicomputations.karnaugh_maps.ui.toDecimalArray
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

class Karnaugh2ViewModel(
    context: Context,
    private val generateContent: GenerateContentUseCase,
    val aiResponseDao: AiResponseDao,
): ViewModel() {
    private val _uiState = MutableStateFlow(Karnaugh2UiState())
    val uiState = _uiState.asStateFlow()

    val textFieldState = mutableStateOf(TextFieldValue())

    init {
        viewModelScope.launch {
            TwoVarsDataStore.lastState(context).first()
                ?.let { state ->
                    if (state.value.isBlank()) return@let
                    try {
                        when(state.convertFrom) {
                            Map -> {
                                val minTerms = state.value.split(";").map { it.toInt() }.toIntArray()
                                onMinTermsChange(minTerms)
                            }
                            Expression -> {
                                onExpressionChange(TextFieldValue(state.value, TextRange(state.value.length)))
                            }
                        }
                    } catch (_: Exception) { }
                }
        }
    }

    fun clear() { _uiState.value = Karnaugh2UiState(convertFrom = _uiState.value.convertFrom) }

    fun setAiState(aiState: AIState) { _uiState.value = _uiState.value.copy(aiState = aiState) }

    fun setConvertFrom(convertFrom: ConvertFrom) { _uiState.value = _uiState.value.copy(convertFrom = convertFrom) }

    fun onExpressionChange(textFieldValue: TextFieldValue) {
        if (textFieldValue.text.isBlank()) {
            _uiState.value = Karnaugh2UiState(convertFrom = _uiState.value.convertFrom)
            return
        }
        textFieldState.value = textFieldValue

        val minTermsSet = mutableSetOf<String>()
        val split = textFieldValue.text.trimEnd('+').split("+")
        for (each in split) {
            var product = each.trim() + Char.MIN_VALUE
            while (product.contains("''"))
                product = product.replace("''", "")

            val charArray = charArrayOf('.','.','.','.')
            var i = 0
            while (i < product.length - 1){
                if (product[i] != '\'' && product[i+1] == '\'') {
                    charArray[product[i++].position()] = '0'
                    ++i
                } else {
                    charArray[product[i++].position()] = '1'
                }
            }
            val regex = charArray.joinToString("").toRegex()

            for (it in arrayList.parallelStream().filter { it.matches(regex) }) minTermsSet.add(it)
        }
        val minTerms = minTermsSet.toDecimalArray()
        Log.d(TAG, "minTerms: ${minTerms.joinToString(";")}")

        val answers = Karnaugh2Variables(minTerms,  IntArray(0)).executeKarnaugh()

        _uiState.value = _uiState.value.copy(
            minTerms = minTerms,
            answers = answers,
            selected = 0,
            convertFrom = Expression
        )
        Log.d(TAG, "_uiState: ${_uiState.value}")
    }

    fun onMinTermsChange(minTerms: IntArray) {
        Log.d(TAG, "minTerms: ${minTerms.joinToString(";")}")

        val answers = Karnaugh2Variables(minTerms,  IntArray(0)).executeKarnaugh()

        val expression = answers[0].toString().split("=")[1].trim()
        textFieldState.value = TextFieldValue(expression, TextRange(expression.length))
        _uiState.value = _uiState.value.copy(
            minTerms = minTerms,
            answers = answers,
            selected = 0,
            convertFrom = Map
        )
        Log.d(TAG, "_uiState: ${_uiState.value}")
    }

    val responsesFlow: Flow<List<AiResponse>?>
        get() = aiResponseDao.selectAll(MODULE_NAME, FourVars.name).map {
            if (it.isNullOrEmpty()) null
            else it
        }

    fun deleteResponse(response: AiResponse) {
        viewModelScope.launch {
            aiResponseDao.delete(response)
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
        FourVars.name,
        Expression.name,
        textFieldState.value.text,
        "Show me steps how to simplify the boolean expression: " + textFieldState.value.text + " using laws, Hint: Make sure you get this correct answer " +
        uiState.value.answers[uiState.value.selected].toString().split("=")[1].replace(" ", "") + ". This answer is correct, never give me something else."
    )

    fun generateSteps() {
        setAiState(AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val aiResponse = aiResponseDao.select(MODULE_NAME, FourVars.name, Expression.name, textFieldState.value.text)
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
                    e is GenerateContentUseCase.AiServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
                    else -> setAiState(AIState.Error(e))
                }
            }
        }
    }

    fun regenerateSteps() {
        val currentResponse = (_uiState.value.aiState as AIState.Success).response
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
        const val TAG = "Karnaugh2ViewModel"
    }
}