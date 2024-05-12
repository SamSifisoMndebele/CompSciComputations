package com.ssmnd.karnaughmap

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.ssmnd.karnaughmap.utils.ListOfMinterms

abstract class KarnaughViewModel : ViewModel() {

    var booleanExp = mutableStateOf(TextFieldValue())
        private set
    var simplifiedBooleanExp = mutableStateOf("Text Field Value")
        private set

    var answers = mutableStateListOf<ListOfMinterms?>()
    var selectedAnswer = mutableStateOf<ListOfMinterms?>(null)

    fun setBooleanExp(value: TextFieldValue) {
        booleanExp.value = value
    }

    fun setAnswers(answers: List<ListOfMinterms>) {
        this.answers = answers.toMutableStateList()
    }

    fun setSimplifiedBooleanExp(value: String) {
        simplifiedBooleanExp.value = value
    }
}

class Karnaugh2ViewModel : KarnaughViewModel()
class Karnaugh3ViewModel : KarnaughViewModel()
class Karnaugh4ViewModel : KarnaughViewModel()