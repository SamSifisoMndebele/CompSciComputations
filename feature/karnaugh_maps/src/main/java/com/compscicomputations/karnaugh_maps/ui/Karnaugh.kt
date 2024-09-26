package com.compscicomputations.karnaugh_maps.ui

import android.util.Log
import kotlin.math.pow

const val MODULE_NAME = "karnaugh_maps"

val arrayList = arrayListOf("0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111")

fun Char.position(): Int = when {
    this=='A' -> 0
    this=='B' -> 1
    this=='C' -> 2
    this=='D' -> 3
    else -> -1
}
fun Set<String>.toDecimalArray(): IntArray {
    if (isEmpty()) return intArrayOf()

    val string = joinToString(" ")
    val decimal = arrayListOf<Int>()
    for (number in string.uppercase().split(" ")) {
        if (number.isEmpty()) continue
        decimal.add(java.lang.Long.parseUnsignedLong(number, 2).toInt())
    }
    return decimal.toIntArray()
}
@Deprecated("Deprecated in Java")
fun List<String>.binaryToDecimalList(): List<String> {
    val string = distinct().joinToString(" ")
    if (string.isEmpty()) return listOf()
    var decimalStr = ""
    for (number in string.uppercase().split(" ")) {
        if (number.isEmpty()) continue
        try {
            var decimal: Long = 0
            var i = 0
            for (digit in number.reversed()) {
                val num = digit.toString().toInt()
                decimal += (num * 2.0.pow(i++)).toLong()
            }
            decimalStr = "$decimalStr$decimal "
        } catch (e: Exception) {
            Log.e("KarnaughMaps", "Base Conversion", e)
            return listOf()
        }
    }
    return decimalStr.trim().split(" ")
}

//fun Karnaugh4VariablesBinding.setAnswersSpinner(answers: ArrayList<ListOfMinTerms>) {
//    val arrayList = ArrayList<Any?>()
//    var i = 0
//    while (i < answers.size) {
//        i++
//        arrayList.add("Answer " + i + " of " + answers.size)
//    }
//    val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(root.context, R.layout.spinner_item, arrayList)
//    arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
//    answerSpinner.onItemSelectedListener = null
//    answerSpinner.adapter = arrayAdapter
//    answerSpinner.setSelection(0, false)
//    answerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//        override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, j: Long) {
//            setAnswerView(answers[i])
//        }
//    }
//}