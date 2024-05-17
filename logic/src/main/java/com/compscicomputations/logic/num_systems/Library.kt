package com.compscicomputations.logic.num_systems

val namesRegex = Regex("^[A-Za-z ]{2,}$")
val emailRegex = Regex("^[A-Za-z0-9._+\\-\']+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")
val phoneNumberRegex = Regex("^((\\+27|27)|0)[- ]?(\\d{2})[- ]?(\\d{3})[- ]?(\\d{4})$")
val strongPasswordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*_]).{6,}$")
val moduleCodeRegex = Regex("^[A-Z]{4,5}[0-9]{3}$")

val decimalNumberRegex = Regex("^-[0-9]+|[0-9]*$")
val decimalFieldRegex = Regex("^[0-9-+ ]*$")
val binaryNumbersRegex = Regex("^[01 ]*$")
val octalNumbersRegex = Regex("^[0-7 ]*$")
val hexNumbersRegex = Regex("^[0-9A-Fa-f ]*$")


infix fun CharSequence.notMatches(regex: Regex): Boolean = !regex.matches(this)