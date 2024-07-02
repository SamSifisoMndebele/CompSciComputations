package com.compscicomputations.core.ktor_client.model

enum class Usertype(val sqlName: String) {
    ADMIN("Admin"), STUDENT("Student"), OTHER("Other");
    override fun toString(): String = sqlName
    companion object {
        val String.asUsertype: Usertype
            get() = when(this) {
                "Admin", "ADMIN" -> ADMIN
                "Student", "STUDENT" -> STUDENT
                "Other", "OTHER" -> OTHER
                else -> OTHER
            }
    }
}