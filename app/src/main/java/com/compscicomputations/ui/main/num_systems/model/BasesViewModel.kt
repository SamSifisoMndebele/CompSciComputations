package com.compscicomputations.ui.main.num_systems.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BasesViewModel : ViewModel() {
    var decimal = mutableStateOf("")
        private set
    var binary = mutableStateOf("")
        private set
    var octal = mutableStateOf("")
        private set
    var hexadecimal = mutableStateOf("")
        private set
    var ascii = mutableStateOf("")
        private set

    var error = mutableStateOf<com.compscicomputations.logic.num_systems.bases.BaseError?>(null)
        private set

    fun fromDecimal(decimalStr: String) {
        val fromDecimal = com.compscicomputations.logic.num_systems.bases.BaseConverter.fromDecimal(decimalStr.removeSuffix("-"))

        decimal.value = decimalStr
        binary.value = fromDecimal.binary
        octal.value = fromDecimal.octal
        hexadecimal.value = fromDecimal.hexadecimal
        ascii.value = fromDecimal.ascii
        error.value = fromDecimal.error
    }

    fun fromBinary(binaryStr: String) {
        val fromBinary = com.compscicomputations.logic.num_systems.bases.BaseConverter.fromBinary(binaryStr)

        decimal.value = fromBinary.decimal
        binary.value = binaryStr
        octal.value = fromBinary.octal
        hexadecimal.value = fromBinary.hexadecimal
        ascii.value = fromBinary.ascii
        error.value = fromBinary.error
    }

    fun fromOctal(octalStr: String) {
        val fromOctal = com.compscicomputations.logic.num_systems.bases.BaseConverter.fromOctal(octalStr)

        decimal.value = fromOctal.decimal
        binary.value = fromOctal.binary
        octal.value = octalStr
        hexadecimal.value = fromOctal.hexadecimal
        ascii.value = fromOctal.ascii
        error.value = fromOctal.error
    }


    fun fromHex(hexadecimalStr: String) {
        val fromHex = com.compscicomputations.logic.num_systems.bases.BaseConverter.fromHex(hexadecimalStr)

        decimal.value = fromHex.decimal
        binary.value = fromHex.binary
        octal.value = fromHex.octal
        hexadecimal.value = hexadecimalStr
        ascii.value = fromHex.ascii
        error.value = fromHex.error
    }
    fun fromAscii(asciiStr: String) {
        val fromAscii = com.compscicomputations.logic.num_systems.bases.BaseConverter.fromCharSeq(asciiStr)

        decimal.value = fromAscii.decimal
        binary.value = fromAscii.binary
        octal.value = fromAscii.octal
        hexadecimal.value = fromAscii.hexadecimal
        ascii.value = asciiStr
        error.value = fromAscii.error
    }
}