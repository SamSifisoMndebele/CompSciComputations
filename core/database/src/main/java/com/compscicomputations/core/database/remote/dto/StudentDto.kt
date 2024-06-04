package com.compscicomputations.core.database.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentDto(
    @SerialName("uid") val uid: String,
    @SerialName("student_course") val studentCourse: String?,
    @SerialName("student_school") val studentSchool: String?,
)
