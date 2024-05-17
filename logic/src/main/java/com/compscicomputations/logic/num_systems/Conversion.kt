package com.compscicomputations.logic.num_systems

class Conversion {

    var decimal = ""
        private set
    var ascii = ""
        private set
    var binary = ""
        private set
    var hex = ""
        private set
    var octal = ""
        private set
    var iEEE754 = ""
        private set
    var doubleIEEE754 = ""
        private set
    var complement1 = ""
        private set
    var complement2 = ""
        private set
    var excess = ""
        private set
    var excessIdentifier = "128"
        private set
    var excessBits = 8
        private set
    var error = false
    var errorMessage = "Size Error"

    internal constructor()

    internal constructor(decimalString: String, excessString: String, excessIdentifier: String, excessBits : Int) {
        decimal = decimalString
        excess = excessString
        this.excessIdentifier = excessIdentifier
        this.excessBits = excessBits
    }

    internal constructor(decimalString: String, floatString: String, doubleString: String) {
        decimal = decimalString
        iEEE754 = floatString
        doubleIEEE754 = doubleString
    }

    internal constructor(decimalString: String, complement1String: String, complement2String: String, noinline : String?) {
        decimal = decimalString
        complement1 = complement1String
        complement2 = complement2String
    }

    internal constructor(decimalString: String, binaryString: String, octalString: String, hexString: String, asciiString: String) {
        decimal = decimalString
        binary = binaryString
        octal = octalString
        hex = hexString
        ascii = asciiString
    }
}