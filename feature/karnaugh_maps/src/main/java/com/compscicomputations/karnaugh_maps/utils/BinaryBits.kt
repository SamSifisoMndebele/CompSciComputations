package com.compscicomputations.karnaugh_maps.utils

import kotlin.math.pow

data class BinaryBits(private val numberOfBits: Int) {
    @JvmField
    var bits: BooleanArray
    private var intValue = 0

    init {
        this.bits = BooleanArray(numberOfBits)
        var i = 0
        while (true) {
            if (i < bits.size) {
                bits[i++] = false
            } else {
                intValue = 0
                break
            }
        }
    }

    fun numberOfPermutations(): Int {
        return 2.0.pow(numberOfBits.toDouble()).toInt()
    }

    override fun toString(): String {
        val str = StringBuilder()
        for (i in 0 until this.numberOfBits) {
            if (bits[i]) {
                str.append("1")
            } else {
                str.append("0")
            }
        }
        return str.toString()
    }

    fun setBits(zArr: BooleanArray) {
        this.bits = zArr
        this.intValue = toString().toInt(2)
    }

    fun arrayIndexOfSetBits(): ArrayList<Int> {
        val arrayList = ArrayList<Int>()
        for (i in 0 until this.numberOfBits) {
            if (bits[i]) {
                arrayList.add(i)
            }
        }
        return arrayList
    }

    private fun setBits(i: Int) {
        val binaryString = StringBuilder(Integer.toBinaryString(i))
        while (binaryString.length < this.numberOfBits) {
            binaryString.insert(0, "0")
        }
        val charArray = binaryString.toString().toCharArray()
        for (i2 in 0 until this.numberOfBits) {
            bits[i2] = charArray[i2] != '0'
        }
    }

    fun inc() {
        val i = this.intValue + 1
        this.intValue = i
        setBits(i)
    }

    fun dec() {
        val i = this.intValue - 1
        this.intValue = i
        setBits(i)
    }
}
