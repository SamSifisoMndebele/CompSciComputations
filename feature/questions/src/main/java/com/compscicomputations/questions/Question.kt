package com.compscicomputations.questions

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val question : String = "",
    val imageUrl : String? = null, //Todo: Add image support

    val answer : String = "",
    val answerImage : String? = null, //Todo: Add image support

    val userId : String = "0",
    val userName : String = "",

//    val path : String = "",
//    val timestamp : Timestamp = Timestamp.now(),
)

@Serializable
object QuestionList