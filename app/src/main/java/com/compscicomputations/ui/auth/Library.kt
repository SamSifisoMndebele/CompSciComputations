package com.compscicomputations.ui.auth

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

enum class UserType { STUDENT, ADMIN, PROFESSIONAL }

fun String?.showMessage(): @Composable (() -> Unit)? {
    if (this == null) return null
    return {
        Text(text = this, color = OutlinedTextFieldDefaults.colors().errorLabelColor)
    }
}
val String?.isError
    get() = this != null


@Deprecated("Remove")
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
@Deprecated("Remove")
data class FieldError(val fieldType: FieldType, val errorMessage: String)

@Deprecated("Remove")
infix fun Set<FieldError>.added(fieldError: FieldError) : Set<FieldError> {
    val set = this.toMutableSet()
    set.add(fieldError)
    return set.toSet()
}
@Deprecated("Remove")
infix fun Set<FieldError>.contain(fieldType: FieldType) : Boolean {
    return this.any { it.fieldType == fieldType }
}
@Deprecated("Remove")
infix fun Set<FieldError>.removed(fieldType: FieldType) : Set<FieldError> {
    return this.filterNot { it.fieldType == fieldType }.toSet()
}
@Deprecated("Remove")
infix fun Set<FieldError>.getMessage(fieldType: FieldType) : @Composable (() ->  Unit)? {
    val error = this.find { it.fieldType == fieldType } ?: return null
    return {
        Text(text = error.errorMessage, color = OutlinedTextFieldDefaults.colors().errorLabelColor)
    }
}