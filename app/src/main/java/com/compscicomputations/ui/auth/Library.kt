package com.compscicomputations.ui.auth

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

enum class UserType { STUDENT, ADMIN, PROFESSIONAL }
enum class FieldType {
    USER_TYPE,
    ADMIN_CODE,
    IMAGE,
    NAMES,
    LASTNAME,
    EMAIL,
    PASSWORD,
    PASSWORD_CONFIRM,
    TERMS
}
data class FieldError(val fieldType: FieldType, val errorMessage: String)

infix fun Set<FieldError>.added(fieldError: FieldError) : Set<FieldError> {
    val set = this.toMutableSet()
    set.add(fieldError)
    return set.toSet()
}
infix fun Set<FieldError>.contain(fieldType: FieldType) : Boolean {
    return this.any { it.fieldType == fieldType }
}
infix fun Set<FieldError>.removed(fieldType: FieldType) : Set<FieldError> {
    return this.filterNot { it.fieldType == fieldType }.toSet()
}
infix fun Set<FieldError>.getMessage(fieldType: FieldType) : @Composable (() ->  Unit)? {
    val error = this.find { it.fieldType == fieldType } ?: return null
    return {
        Text(text = error.errorMessage, color = OutlinedTextFieldDefaults.colors().errorLabelColor)
    }
}