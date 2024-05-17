package com.ssmnd.karnaughmaps.utils

class MinTerm {
    @JvmField
    var minTermIntegers = ArrayList<Int>()
    @JvmField
    var minTermString: String? = null
    private val variableName = arrayOf("A", "B", "C", "D", "E")
    fun onlyOneVariableNumber(): Int {
        val replace = toString().replace("'", "")
        if (replace.length > 1) {
            return -1
        }
        var i = 0
        while (true) {
            val strArr = variableName
            if (i >= strArr.size) {
                return -1
            }
            if (replace == strArr[i]) {
                return i
            }
            i++
        }
    }

    override fun toString(): String {

        val charArray = minTermString!!.toCharArray()
        val str = StringBuilder()
        for (i in charArray.indices) {
            if (charArray[i] == '0') {
                str.append(variableName[i]).append("'")
            }
            if (charArray[i] == '1') {
                str.append(variableName[i])
            }
        }
        return str.toString()
    }

    fun toStringPos(): String {
        val charArray = minTermString!!.toCharArray()
        val str = StringBuilder()
        for (i in charArray.indices) {
            if (charArray[i] == '1') {
                if (str.isNotEmpty()) {
                    str.append("+")
                }
                str.append(variableName[i]).append("'")
            }
            if (charArray[i] == '0') {
                if (str.isNotEmpty()) {
                    str.append("+")
                }
                str.append(variableName[i])
            }
        }
        return str.toString()
    }

    fun numberOfVariables(): Int {
        return toString().replace("'", "").length
    }
}