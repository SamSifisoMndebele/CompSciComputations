package com.compscicomputations.number_systems.data.model

import androidx.compose.runtime.Stable

@Stable
enum class ConvertFrom(val text: String) {
    Decimal("Decimal"),

    Binary("Binary"),
    Octal("Octal"),
    Hexadecimal("Hexadecimal"),
    Unicode("Unicode Character(s)"),

    Complement1("Complement 1"),
    Complement2("Complement 2"),

    Excess("Excess notation"),

    MiniFloat("Mini float"),
    Binary16("Half precision"),
    Binary32("Single precision"),
    Binary64("Double precision");

    companion object {
        val baseN = arrayOf(Decimal, Binary, Octal, Hexadecimal, Unicode)
        val complementNotation = arrayOf(Decimal, Complement1, Complement2)
        val excessNotation = arrayOf(Decimal, Excess)
        val floatingPointNotation = arrayOf(Decimal, /*MiniFloat, Binary16,*/ Binary32, Binary64)
    }

    val decimal: Boolean
        get() = this == Decimal
    val binary: Boolean
        get() = this == Binary
    val octal: Boolean
        get() = this == Octal
    val hexadecimal: Boolean
        get() = this == Hexadecimal
    val unicode: Boolean
        get() = this == Unicode
    val complement1: Boolean
        get() = this == Complement1
    val complement2: Boolean
        get() = this == Complement2
    val excess: Boolean
        get() = this == Excess
    val miniFloat: Boolean
        get() = this == MiniFloat
    val binary16: Boolean
        get() = this == Binary16
    val binary32: Boolean
        get() = this == Binary32
    val binary64: Boolean
        get() = this == Binary64
}