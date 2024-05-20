package com.compscicomputations.number_systems.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.compscicomputations.number_systems.logic.bases.BaseConverter
import com.compscicomputations.number_systems.logic.bases.BaseError

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

    var error = mutableStateOf<BaseError?>(null)
        private set

    fun fromDecimal(decimalStr: String) {
        val fromDecimal = BaseConverter.fromDecimal(decimalStr.removeSuffix("-"))

        decimal.value = decimalStr
        binary.value = fromDecimal.binary
        octal.value = fromDecimal.octal
        hexadecimal.value = fromDecimal.hexadecimal
        ascii.value = fromDecimal.ascii
        error.value = fromDecimal.error
    }

    fun fromBinary(binaryStr: String) {
        val fromBinary = BaseConverter.fromBinary(binaryStr)

        decimal.value = fromBinary.decimal
        binary.value = binaryStr
        octal.value = fromBinary.octal
        hexadecimal.value = fromBinary.hexadecimal
        ascii.value = fromBinary.ascii
        error.value = fromBinary.error
    }

    fun fromOctal(octalStr: String) {
        val fromOctal = BaseConverter.fromOctal(octalStr)

        decimal.value = fromOctal.decimal
        binary.value = fromOctal.binary
        octal.value = octalStr
        hexadecimal.value = fromOctal.hexadecimal
        ascii.value = fromOctal.ascii
        error.value = fromOctal.error
    }


    fun fromHex(hexadecimalStr: String) {
        val fromHex = BaseConverter.fromHex(hexadecimalStr)

        decimal.value = fromHex.decimal
        binary.value = fromHex.binary
        octal.value = fromHex.octal
        hexadecimal.value = hexadecimalStr
        ascii.value = fromHex.ascii
        error.value = fromHex.error
    }
    fun fromAscii(asciiStr: String) {
        val fromAscii = BaseConverter.fromCharSeq(asciiStr)

        decimal.value = fromAscii.decimal
        binary.value = fromAscii.binary
        octal.value = fromAscii.octal
        hexadecimal.value = fromAscii.hexadecimal
        ascii.value = asciiStr
        error.value = fromAscii.error
    }
}