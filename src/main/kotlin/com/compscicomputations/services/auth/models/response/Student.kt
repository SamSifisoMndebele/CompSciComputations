package com.compscicomputations.services.auth.models.response

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String,
    val university: String,
    val school: String,
    val course: String,
)