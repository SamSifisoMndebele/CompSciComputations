package com.compscicomputations.logic.num_systems

import com.compscicomputations.logic.num_systems.bases.BaseConverter.toDecimal


object ExcessConvert {



    //////
    fun String.identifierToBits(): String {
        return try {
            val excessIdentifier = this.toLong()
            val excessBits = java.lang.Long.toBinaryString(excessIdentifier)
            excessBits.length.toString()
        } catch (e: Exception) {
            ""
        }
    }

    fun String.bitsToIdentifier(): String {
        return try {
            val excessBits = this.toInt()
            val excessIdentifier = "1".padEnd(excessBits, '0')
            excessIdentifier.toDecimal().toString()
        } catch (e: Exception) {
            ""
        }
    }

    fun Long.isValidIdentifier() : Boolean {
        var b = true
        var i = this
        while (i > 0){
            if ((i % 2) != 0.toLong()){
                b = false
                break
            }
            i/=2
        }
        return b
    }


}