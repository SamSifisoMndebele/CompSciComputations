package com.compscicomputations.karnaugh_maps.logic

import com.compscicomputations.karnaugh_maps.utils.BinaryBits
import com.compscicomputations.karnaugh_maps.utils.ListOfMinterms

class Karnaugh3Variables(iArr: IntArray, iArr2: IntArray?) {
    private val allEssentialMinTerms: ArrayList<Int>
    private val allMinTerms: ListOfMinterms
    private var minTerms0ones: ListOfMinterms
    private var minTerms1ones: ListOfMinterms
    private var minTerms2ones: ListOfMinterms
    private val minTerms3ones: ListOfMinterms
    private val notPrimeImplicates: ListOfMinterms
    private val primeImplicates: ListOfMinterms
    private fun intArrayToArrayList(iArr: IntArray): ArrayList<Int> {
        val arrayList = ArrayList<Int>()
        for (valueOf in iArr) {
            arrayList.add(Integer.valueOf(valueOf))
        }
        return arrayList
    }

    fun executeKarnaugh(): ArrayList<ListOfMinterms> {
        val arrayList = ArrayList<ListOfMinterms>()
        return if (allEssentialMinTerms.size == 0) {
            arrayList.add(ListOfMinterms(3))
            arrayList
        } else if (allMinTerms.size() == 8) {
            val arrayList2: ArrayList<Int> = ArrayList<Int>()
            for (i in 0..7) {
                arrayList2.add(Integer.valueOf(i))
            }
            val listOfMinterms = ListOfMinterms(3)
            listOfMinterms.add("---", arrayList2)
            arrayList.add(listOfMinterms)
            arrayList
        } else {
            splitCubes()
            findPrimeImplicates()
            findPrimePermutations(primeImplicates)
        }
    }

    private fun numberOfOnes(str: String): Int {
        var i = 0
        for (element in str) {
            if (element == '1') {
                i++
            }
        }
        return i
    }

    private fun setDashes(str: String, str2: String): String {
        val charArray = str.toCharArray()
        val charArray2 = str2.toCharArray()
        var i = 0
        var i2 = 0
        for (i3 in str.indices) {
            if (charArray[i3] != charArray2[i3]) {
                i++
                i2 = i3
            }
        }
        if (i != 1) {
            return ""
        }
        charArray[i2] = '-'
        return String(charArray)
    }

    private fun findPrimeImplicates() {
        minTerms0ones = addOneBitDifferences(minTerms0ones, minTerms1ones)
        minTerms1ones = addOneBitDifferences(minTerms1ones, minTerms2ones)
        minTerms2ones = addOneBitDifferences(minTerms2ones, minTerms3ones)
        minTerms0ones = addOneBitDifferences(minTerms0ones, minTerms1ones)
        val addOneBitDifferences = addOneBitDifferences(minTerms1ones, minTerms2ones)
        minTerms1ones = addOneBitDifferences
        minTerms0ones = addOneBitDifferences(minTerms0ones, addOneBitDifferences)
        for (i in 0 until notPrimeImplicates.size()) {
            primeImplicates.removeString(notPrimeImplicates.getString(i))
        }
        primeImplicates.removeDuplicates()
    }

    private fun findPrimePermutations(listOfMinterms: ListOfMinterms): ArrayList<ListOfMinterms> {
        val arrayList: ArrayList<ArrayList<Int>> = ArrayList<ArrayList<Int>>()
        val size = listOfMinterms.size()
        val binaryBits = BinaryBits(size)
        var i = 1000
        for (i2 in 0 until binaryBits.numberOfPermutations()) {
            val arrayList2: ArrayList<Int> = ArrayList<Int>()
            for (i3 in 0 until size) {
                if (binaryBits.bits[i3]) {
                    arrayList2.addAll(listOfMinterms.getIntegers(i3))
                }
            }
            if (arrayList2.containsAll(allEssentialMinTerms)) {
                arrayList.add(binaryBits.arrayIndexOfSetBits())
                if (binaryBits.arrayIndexOfSetBits().size < i) {
                    i = binaryBits.arrayIndexOfSetBits().size
                }
            }
            binaryBits.inc()
        }
        val arrayList3: ArrayList<ArrayList<Int>> = ArrayList<ArrayList<Int>>()
        for (i4 in arrayList.indices) {
            if ((arrayList[i4] as ArrayList<*>).size == i) {
                arrayList3.add(arrayList[i4])
            }
        }
        val arrayList4: ArrayList<ListOfMinterms> = ArrayList<ListOfMinterms>()
        var i5 = 0
        for (i6 in arrayList3.indices) {
            val arrayList5 = arrayList3[i6] as ArrayList<*>
            val listOfMinterms2 = ListOfMinterms(3)
            for (i7 in arrayList5.indices) {
                listOfMinterms2.add(
                    listOfMinterms.getString((arrayList5[i7] as Int).toInt()),
                    listOfMinterms.getIntegers((arrayList5[i7] as Int).toInt())
                )
            }
            val simplicity = listOfMinterms2.simplicity()
            if (simplicity > i5) {
                i5 = simplicity
            }
            arrayList4.add(listOfMinterms2)
        }
        val arrayList6 = ArrayList<ListOfMinterms>()
        for (i8 in arrayList4.indices) {
            val listOfMinterms3 = arrayList4[i8]
            if (listOfMinterms3.simplicity() == i5) {
                arrayList6.add(listOfMinterms3)
            }
        }
        return arrayList6
    }

    private fun addOneBitDifferences(
        listOfMinterms: ListOfMinterms,
        listOfMinterms2: ListOfMinterms
    ): ListOfMinterms {
        val listOfMinterms3 = ListOfMinterms(3)
        if (listOfMinterms.size() == 1) {
            primeImplicates.add(listOfMinterms.getString(0), listOfMinterms.getIntegers(0))
        }
        if (listOfMinterms2.size() == 1) {
            primeImplicates.add(listOfMinterms2.getString(0), listOfMinterms2.getIntegers(0))
        }
        for (i in 0 until listOfMinterms.size()) {
            var z = true
            for (i2 in 0 until listOfMinterms2.size()) {
                val dashes = setDashes(listOfMinterms.getString(i), listOfMinterms2.getString(i2))
                if (dashes != "") {
                    notPrimeImplicates.add(
                        listOfMinterms.getString(i),
                        listOfMinterms.getIntegers(i)
                    )
                    notPrimeImplicates.add(
                        listOfMinterms2.getString(i2),
                        listOfMinterms2.getIntegers(i2)
                    )
                    val arrayList: ArrayList<Int> = ArrayList<Int>()
                    arrayList.addAll(listOfMinterms.getIntegers(i))
                    arrayList.addAll(listOfMinterms2.getIntegers(i2))
                    listOfMinterms3.add(dashes, arrayList)
                    z = false
                }
            }
            if (z) {
                primeImplicates.add(listOfMinterms.getString(i), listOfMinterms.getIntegers(i))
            }
        }
        for (i3 in 0 until listOfMinterms2.size()) {
            var z2 = true
            for (i4 in 0 until listOfMinterms.size()) {
                if (setDashes(listOfMinterms2.getString(i3), listOfMinterms.getString(i4)) != "") {
                    notPrimeImplicates.add(
                        listOfMinterms2.getString(i3),
                        listOfMinterms2.getIntegers(i3)
                    )
                    notPrimeImplicates.add(
                        listOfMinterms.getString(i4),
                        listOfMinterms.getIntegers(i4)
                    )
                    z2 = false
                }
            }
            if (z2) {
                primeImplicates.add(listOfMinterms2.getString(i3), listOfMinterms2.getIntegers(i3))
            }
        }
        listOfMinterms3.removeDuplicates()
        return listOfMinterms3
    }

    private fun splitCubes() {
        for (i in 0 until allMinTerms.size()) {
            when (numberOfOnes(allMinTerms.getString(i))) {
                0 -> {
                    minTerms0ones.add(allMinTerms.getString(i), allMinTerms.getIntegers(i))
                }
                1 -> {
                    minTerms1ones.add(allMinTerms.getString(i), allMinTerms.getIntegers(i))
                }
                2 -> {
                    minTerms2ones.add(allMinTerms.getString(i), allMinTerms.getIntegers(i))
                }
                3 -> {
                    minTerms3ones.add(allMinTerms.getString(i), allMinTerms.getIntegers(i))
                }
            }
        }
    }

    init {
        allMinTerms = ListOfMinterms(iArr, iArr2, 3)
        allEssentialMinTerms = intArrayToArrayList(iArr)
        minTerms0ones = ListOfMinterms(3)
        minTerms1ones = ListOfMinterms(3)
        minTerms2ones = ListOfMinterms(3)
        minTerms3ones = ListOfMinterms(3)
        notPrimeImplicates = ListOfMinterms(3)
        primeImplicates = ListOfMinterms(3)
    }
}