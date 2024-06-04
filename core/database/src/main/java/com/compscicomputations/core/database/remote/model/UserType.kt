package com.compscicomputations.core.database.remote.model

enum class UserType(val sqlName: String) {
    ADMIN("Admin"), STUDENT("Student"), OTHER("Other");
    override fun toString(): String = sqlName
    companion object {
        val String.asUserType: UserType
            get() = when(this) {
                "Admin", "ADMIN" -> ADMIN
                "Student", "STUDENT" -> STUDENT
                "Other", "OTHER" -> OTHER
                else -> OTHER
            }
    }
}