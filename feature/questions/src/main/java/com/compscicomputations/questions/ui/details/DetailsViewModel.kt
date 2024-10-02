package com.compscicomputations.questions.ui.details

import androidx.lifecycle.ViewModel
import com.compscicomputations.questions.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    question: Question
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailsUiState(question))
    val uiState = _uiState.asStateFlow()

    companion object {
        private const val TAG = "DetailsViewModel"
    }
}