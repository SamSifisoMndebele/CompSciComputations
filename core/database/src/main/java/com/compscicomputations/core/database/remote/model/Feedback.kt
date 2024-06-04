package com.compscicomputations.core.database.remote.model

data class Feedback(
    val id: Int,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val uid: String,
)
