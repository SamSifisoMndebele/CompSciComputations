package com.compscicomputations.karnaugh_maps.utils

import kotlin.math.pow

class ListOfMinTerms {
    private var minTermsList = ArrayList<MinTerm>()
    private val numberOfVariables: Int

    constructor(iArr: IntArray, iArr2: IntArray?, varNo: Int) {
        this.numberOfVariables = varNo
        val strArr = arrayOf(
            "00000",
            "00001",
            "00010",
            "00011",
            "00100",
            "00101",
            "00110",
            "00111",
            "01000",
            "01001",
            "01010",
            "01011",
            "01100",
            "01101",
            "01110",
            "01111",
            "10000",
            "10001",
            "10010",
            "10011",
            "10100",
            "10101",
            "10110",
            "10111",
            "11000",
            "11001",
            "11010",
            "11011",
            "11100",
            "11101",
            "11110",
            "11111"
        )
        for (i2 in iArr.indices) {
            val minterm = MinTerm()
            minterm.minTermString = strArr[iArr[i2]]
            minterm.minTermString = minterm.minTermString!!.substring(5 - varNo, 5)
            minterm.minTermIntegers.add(iArr[i2])
            minTermsList.add(i2, minterm)
        }
        if (iArr2 != null) {
            for (i3 in iArr2.indices) {
                val minTerm2 = MinTerm()
                minTerm2.minTermString = strArr[iArr2[i3]]
                minTerm2.minTermString = minTerm2.minTermString!!.substring(5 - varNo, 5)
                minTerm2.minTermIntegers.add(iArr2[i3])
                minTermsList.add(i3, minTerm2)
            }
        }
    }

    constructor(i: Int) {
        this.numberOfVariables = i
    }

    override fun toString(): String {
        if (minTermsList.size == 1) {
            if (minTermsList[0].minTermIntegers.size == 0) {
                return "S = 0"
            }
            if ((minTermsList[0].minTermIntegers.size.toDouble()) == 2.0.pow(
                    numberOfVariables.toDouble()
                )
            ) {
                return "S = 1"
            }
        }
        if (minTermsList.size == 0) {
            return "S = 0"
        }
        val str = StringBuilder("S = " + minTermsList[0].toString())
        for (i in 1 until minTermsList.size) {
            str.append(" + ").append(minTermsList[i].toString())
        }
        return str.toString()
    }

    fun toStringPos(): String {
        if (minTermsList.size == 1) {
            if (minTermsList[0].minTermIntegers.size == 0) {
                return "S = 1"
            }
            if ((minTermsList[0].minTermIntegers.size.toDouble()) == 2.0.pow(
                    numberOfVariables.toDouble()
                )
            ) {
                return "S = 0"
            }
        }
        if (minTermsList.size == 0) {
            return "S = 1"
        }
        val str = StringBuilder("S = (" + minTermsList[0].toStringPos() + ")")
        for (i in 1 until minTermsList.size) {
            str.append(" · (").append(minTermsList[i].toStringPos()).append(")")
        }
        return str.toString()
    }

    fun simplicity(): Int {
        var i = 0
        for (i2 in minTermsList.indices) {
            i += minTermsList[i2].minTermIntegers.size
        }
        return i
    }

    fun size(): Int {
        return minTermsList.size
    }

    fun getMinterm(i: Int): MinTerm {
        return minTermsList[i]
    }

    fun getString(i: Int): String? {
        return minTermsList[i].minTermString
    }

    fun getIntegers(i: Int): ArrayList<Int> {
        return minTermsList[i].minTermIntegers
    }

    fun setString(i: Int, str: String?) {
        minTermsList[i].minTermString = str
    }

    fun setInteger(i: Int, arrayList: ArrayList<Int>) {
        minTermsList[i].minTermIntegers = arrayList
    }

    val mintermsInteger: ArrayList<Int>
        get() {
            val arrayList = ArrayList<Int>()
            for (i in minTermsList.indices) {
                arrayList.addAll(minTermsList[i].minTermIntegers)
            }
            return arrayList
        }

    fun add(str: String?, arrayList: ArrayList<Int>) {
        val minterm = MinTerm()
        minterm.minTermString = str
        minterm.minTermIntegers = arrayList
        minTermsList.add(minterm)
    }

    private fun findString(arrayList: ArrayList<MinTerm>, str: String?): Boolean {
        for (i in arrayList.indices) {
            if (arrayList[i].minTermString == str) {
                return true
            }
        }
        return false
    }

    fun removeDuplicates() {
        val arrayList = ArrayList<MinTerm>()
        for (i in minTermsList.indices) {
            val minterm = MinTerm()
            val str = minTermsList[i].minTermString
            val arrayList2 = minTermsList[i].minTermIntegers
            minterm.minTermString = str
            minterm.minTermIntegers = arrayList2
            if (!findString(arrayList, str)) {
                arrayList.add(minterm)
            }
        }
        this.minTermsList = arrayList
    }

    fun removeString(str: String) {
        for (i in minTermsList.indices) {
            if (minTermsList[i].minTermString == str) {
                minTermsList.removeAt(i)
                return
            }
        }
    }
}
