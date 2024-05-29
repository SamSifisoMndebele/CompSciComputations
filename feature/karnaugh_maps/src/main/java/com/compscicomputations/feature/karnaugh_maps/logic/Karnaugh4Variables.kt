package com.compscicomputations.feature.karnaugh_maps.logic

import com.compscicomputations.feature.karnaugh_maps.utils.BinaryBits
import com.compscicomputations.feature.karnaugh_maps.utils.ListOfMinterms

class Karnaugh4Variables(iArr: IntArray, iArr2: IntArray?) {
    private val allEssentialMinTerms: ArrayList<Int>
    private val allMinTerms: ListOfMinterms
    private var minTerms0ones: ListOfMinterms
    private var minTerms1ones: ListOfMinterms
    private var minTerms2ones: ListOfMinterms
    private var minTerms3ones: ListOfMinterms
    private val minTerms4ones: ListOfMinterms
    private val notPrimeImplicates: ListOfMinterms
    private val primeImplicates: ListOfMinterms
    private fun intArrayToArrayList(iArr: IntArray): ArrayList<Int> {
        val arrayList = ArrayList<Int>()
        for (valueOf in iArr) {
            arrayList.add(valueOf)
        }
        return arrayList
    }

    fun executeKarnaugh(): ArrayList<ListOfMinterms> {
        val arrayList = ArrayList<ListOfMinterms>()
        return if (allEssentialMinTerms.size == 0) {
            arrayList.add(ListOfMinterms(4))
            arrayList
        } else if (allMinTerms.size() == 16) {
            val arrayList2 = ArrayList<Int>()
            for (i in 0..15) {
                arrayList2.add(i)
            }
            val listOfMinterms = ListOfMinterms(4)
            listOfMinterms.add("----", arrayList2)
            arrayList.add(listOfMinterms)
            arrayList
        } else {
            splitCubes()
            findPrimeImplicates()
            findPrimePermutations(primeImplicates)
        }
    }

    private fun numberOfOnes(str: String): Int {
        return str.count { it == '1' }
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
        minTerms3ones = addOneBitDifferences(minTerms3ones, minTerms4ones)
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
        val arrayList = ArrayList<ArrayList<Int>>()
        val size = listOfMinterms.size()
        val binaryBits = BinaryBits(size)
        var i = 1000
        for (i2 in 0 until binaryBits.numberOfPermutations()) {
            val arrayList2 = ArrayList<Int>()
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
        val arrayList3 = ArrayList<ArrayList<Int>>()
        for (i4 in arrayList.indices) {
            if (arrayList[i4].size == i) {
                arrayList3.add(arrayList[i4])
            }
        }
        val arrayList4 = ArrayList<ListOfMinterms>()
        var i5 = 0
        for (i6 in arrayList3.indices) {
            val arrayList5 = arrayList3[i6]
            val listOfMinTerms2 = ListOfMinterms(4)
            for (i7 in arrayList5.indices) {
                listOfMinTerms2.add(
                    listOfMinterms.getString(arrayList5[i7]), listOfMinterms.getIntegers(
                        arrayList5[i7]
                    )
                )
            }
            val simplicity = listOfMinTerms2.simplicity()
            if (simplicity > i5) {
                i5 = simplicity
            }
            arrayList4.add(listOfMinTerms2)
        }
        val arrayList6 = ArrayList<ListOfMinterms>()
        for (i8 in arrayList4.indices) {
            val listOfMinTerms3 = arrayList4[i8]
            if (listOfMinTerms3.simplicity() == i5) {
                arrayList6.add(listOfMinTerms3)
            }
        }
        return arrayList6
    }

    private fun addOneBitDifferences(
        listOfMinterms: ListOfMinterms,
        listOfMinTerms2: ListOfMinterms
    ): ListOfMinterms {
        val listOfMinTerms3 = ListOfMinterms(4)
        if (listOfMinterms.size() == 1) {
            primeImplicates.add(listOfMinterms.getString(0), listOfMinterms.getIntegers(0))
        }
        if (listOfMinTerms2.size() == 1) {
            primeImplicates.add(listOfMinTerms2.getString(0), listOfMinTerms2.getIntegers(0))
        }
        for (i in 0 until listOfMinterms.size()) {
            var z = true
            for (i2 in 0 until listOfMinTerms2.size()) {
                val dashes = setDashes(listOfMinterms.getString(i), listOfMinTerms2.getString(i2))
                if (dashes != "") {
                    notPrimeImplicates.add(
                        listOfMinterms.getString(i),
                        listOfMinterms.getIntegers(i)
                    )
                    notPrimeImplicates.add(
                        listOfMinTerms2.getString(i2),
                        listOfMinTerms2.getIntegers(i2)
                    )
                    val arrayList = ArrayList<Int>()
                    arrayList.addAll(listOfMinterms.getIntegers(i))
                    arrayList.addAll(listOfMinTerms2.getIntegers(i2))
                    listOfMinTerms3.add(dashes, arrayList)
                    z = false
                }
            }
            if (z) {
                primeImplicates.add(listOfMinterms.getString(i), listOfMinterms.getIntegers(i))
            }
        }
        for (i3 in 0 until listOfMinTerms2.size()) {
            var z2 = true
            for (i4 in 0 until listOfMinterms.size()) {
                if (setDashes(listOfMinTerms2.getString(i3), listOfMinterms.getString(i4)) != "") {
                    notPrimeImplicates.add(
                        listOfMinTerms2.getString(i3),
                        listOfMinTerms2.getIntegers(i3)
                    )
                    notPrimeImplicates.add(
                        listOfMinterms.getString(i4),
                        listOfMinterms.getIntegers(i4)
                    )
                    z2 = false
                }
            }
            if (z2) {
                primeImplicates.add(listOfMinTerms2.getString(i3), listOfMinTerms2.getIntegers(i3))
            }
        }
        listOfMinTerms3.removeDuplicates()
        return listOfMinTerms3
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
                4 -> {
                    minTerms4ones.add(allMinTerms.getString(i), allMinTerms.getIntegers(i))
                }
            }
        }
    }

    init {
        allMinTerms = ListOfMinterms(iArr, iArr2, 4)
        allEssentialMinTerms = intArrayToArrayList(iArr)
        minTerms0ones = ListOfMinterms(4)
        minTerms1ones = ListOfMinterms(4)
        minTerms2ones = ListOfMinterms(4)
        minTerms3ones = ListOfMinterms(4)
        minTerms4ones = ListOfMinterms(4)
        notPrimeImplicates = ListOfMinterms(4)
        primeImplicates = ListOfMinterms(4)
    }
}