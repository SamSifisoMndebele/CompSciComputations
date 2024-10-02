package com.compscicomputations.questions.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.compscicomputations.questions.Question
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuestionsViewModel(
    userId : String,
    val userName : String,
): ViewModel() {
    private val _uiState = MutableStateFlow(QuestionsUiState())
    val uiState = _uiState.asStateFlow()
    init {
        val database = Firebase.database.reference
        val questionListener = object : ValueEventListener {
            val list = arrayListOf<Question>()
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val question = postSnapshot.getValue<Question>() ?: continue
                    list.add(question)
                }
                _uiState.value = _uiState.value.copy(questions = list)
                Log.d(TAG, "loadPost:onDataChange ${list.size}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.child("questions").addValueEventListener(questionListener)
    }

    override fun onCleared() {
        super.onCleared()
//        listener.remove()
    }

    companion object {
        private const val TAG = "QuestionsViewModel"
    }
}