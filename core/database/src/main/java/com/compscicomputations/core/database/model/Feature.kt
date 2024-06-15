package com.compscicomputations.core.database.model

data class Feature(
    val id: Int,
    val title: String,
    val module: String,
    val className: String,
    val methodName: String?,
)