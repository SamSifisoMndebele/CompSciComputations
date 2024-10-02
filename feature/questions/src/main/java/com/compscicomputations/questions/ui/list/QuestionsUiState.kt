package com.compscicomputations.questions.ui.list

import com.compscicomputations.questions.Question

data class QuestionsUiState(
    val isStudent: Boolean = false,
    val questions: List<Question> = listOf(
        Question(
//            id = 1,
            question = "kbjvsndxk",
            userName = "Sam"
        ),
        Question(
//            id = 2,
            question = "sdfnsndfcv fsbvad ijbkavs ija svcijkn acsiuzxjk vaiusjzb vaiusjkn acsiuzxjk vaiusjkn acsiuzxjk",
            answer = "dmfcvredhnfg",
            userName = "Sam"
        ),
    ),
)