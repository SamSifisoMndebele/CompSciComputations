package com.compscicomputations.utils

import com.compscicomputations.theme.phoneNumberRegex

infix fun CharSequence.notMatches(regex: Regex): Boolean = !regex.matches(this)

fun CharSequence?.isPhoneValid(): Boolean = isNullOrBlank() || matches(phoneNumberRegex)

fun CharSequence.isNotBlankText(): Boolean = isNotBlank() && matches("[_a-zA-Z ]+".toRegex())

fun CharSequence.isText(): Boolean = matches("[_a-zA-Z ]*".toRegex())