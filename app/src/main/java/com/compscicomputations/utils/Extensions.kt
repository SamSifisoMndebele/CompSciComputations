package com.compscicomputations.utils

import com.compscicomputations.theme.phoneNumberRegex

infix fun CharSequence.notMatches(regex: Regex): Boolean = !matches(regex)

fun CharSequence?.isPhoneValid(): Boolean = isNullOrBlank() || matches(phoneNumberRegex)

fun CharSequence?.isBlankText(): Boolean = isNullOrBlank() || notMatches("[_a-zA-Z ]+".toRegex())

fun CharSequence?.isNotBlankText(): Boolean = !isNullOrBlank() && matches("[_a-zA-Z ]+".toRegex())

fun CharSequence.isText(): Boolean = matches("[_a-zA-Z ]*".toRegex())