package com.compscicomputations.data.model

data class Feedback(
    val id: Int,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val uid: String,
)
