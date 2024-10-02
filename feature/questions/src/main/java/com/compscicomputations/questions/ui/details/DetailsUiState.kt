package com.compscicomputations.questions.ui.details

import com.compscicomputations.questions.Question

data class DetailsUiState(
    val question: Question,
    val isStudent: Boolean = false,
)