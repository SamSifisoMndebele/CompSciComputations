package com.compscicomputations.karnaugh_maps.logic

import com.compscicomputations.karnaugh_maps.utils.BinaryBits
import com.compscicomputations.karnaugh_maps.utils.ListOfMinTerms

class Karnaugh3Variables(iArr: IntArray, iArr2: IntArray?) {
    private val allEssentialMinTerms: ArrayList<Int>
    private val allMinTerms: ListOfMinTerms = ListOfMinTerms(iArr, iArr2, 3)
    private var minTerms0ones: ListOfMinTerms
    private var minTerms1ones: ListOfMinTerms
    private var minTerms2ones: ListOfMinTerms
    private val minTerms3ones: ListOfMinTerms
    private val notPrimeImplicates: ListOfMinTerms
    private val primeImplicates: ListOfMinTerms
    private fun intArrayToArrayList(iArr: IntArray): ArrayList<Int> {
        val arrayList = ArrayList<Int>()
        for (valueOf in iArr) {
            arrayList.add(Integer.valueOf(valueOf))
        }
        return arrayList
    }

    fun executeKarnaugh(): ArrayList<ListOfMinTerms> {
        val arrayList = ArrayList<ListOfMinTerms>()
        return if (allEssentialMinTerms.size == 0) {
            arrayList.add(ListOfMinTerms(3))
            arrayList
        } else if (allMinTerms.size() == 8) {
            val arrayList2: ArrayList<Int> = ArrayList<Int>()
            for (i in 0..7) {
                arrayList2.add(Integer.valueOf(i))
            }
            val listOfMinterms = ListOfMinTerms(3)
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
            notPrimeImplicates.getString(i)?.let { primeImplicates.removeString(it) }
        }
        primeImplicates.removeDuplicates()
    }

    private fun findPrimePermutations(listOfMinterms: ListOfMinTerms): ArrayList<ListOfMinTerms> {
        val arrayList: ArrayList<ArrayList<Int>> = ArrayList()
        val size = listOfMinterms.size()
        val binaryBits = BinaryBits(size)
        var i = 1000
        for (i2 in 0 until binaryBits.numberOfPermutations()) {
            val arrayList2: ArrayList<Int> = ArrayList()
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
        val arrayList3: ArrayList<ArrayList<Int>> = ArrayList()
        for (i4 in arrayList.indices) {
            if ((arrayList[i4] as ArrayList<*>).size == i) {
                arrayList3.add(arrayList[i4])
            }
        }
        val arrayList4: ArrayList<ListOfMinTerms> = ArrayList()
        var i5 = 0
        for (i6 in arrayList3.indices) {
            val arrayList5 = arrayList3[i6] as ArrayList<*>
            val listOfMinTerms2 = ListOfMinTerms(3)
            for (i7 in arrayList5.indices) {
                listOfMinTerms2.add(
                    listOfMinterms.getString((arrayList5[i7] as Int).toInt()),
                    listOfMinterms.getIntegers((arrayList5[i7] as Int).toInt())
                )
            }
            val simplicity = listOfMinTerms2.simplicity()
            if (simplicity > i5) {
                i5 = simplicity
            }
            arrayList4.add(listOfMinTerms2)
        }
        val arrayList6 = ArrayList<ListOfMinTerms>()
        for (i8 in arrayList4.indices) {
            val listOfMinterms3 = arrayList4[i8]
            if (listOfMinterms3.simplicity() == i5) {
                arrayList6.add(listOfMinterms3)
            }
        }
        return arrayList6
    }

    private fun addOneBitDifferences(
        listOfMinterms: ListOfMinTerms,
        listOfMinTerms2: ListOfMinTerms
    ): ListOfMinTerms {
        val listOfMinTerms3 = ListOfMinTerms(3)
        if (listOfMinterms.size() == 1) {
            primeImplicates.add(listOfMinterms.getString(0), listOfMinterms.getIntegers(0))
        }
        if (listOfMinTerms2.size() == 1) {
            primeImplicates.add(listOfMinTerms2.getString(0), listOfMinTerms2.getIntegers(0))
        }
        for (i in 0 until listOfMinterms.size()) {
            var z = true
            for (i2 in 0 until listOfMinTerms2.size()) {
                val dashes = listOfMinterms.getString(i)
                    ?.let { listOfMinTerms2.getString(i2)?.let { it1 -> setDashes(it, it1) } }
                if (dashes != "") {
                    notPrimeImplicates.add(
                        listOfMinterms.getString(i),
                        listOfMinterms.getIntegers(i)
                    )
                    notPrimeImplicates.add(
                        listOfMinTerms2.getString(i2),
                        listOfMinTerms2.getIntegers(i2)
                    )
                    val arrayList: ArrayList<Int> = ArrayList<Int>()
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
                if (listOfMinTerms2.getString(i3)
                        ?.let { listOfMinterms.getString(i4)?.let { it1 -> setDashes(it, it1) } } != "") {
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
            when (allMinTerms.getString(i)?.let { numberOfOnes(it) }) {
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
        allEssentialMinTerms = intArrayToArrayList(iArr)
        minTerms0ones = ListOfMinTerms(3)
        minTerms1ones = ListOfMinTerms(3)
        minTerms2ones = ListOfMinTerms(3)
        minTerms3ones = ListOfMinTerms(3)
        notPrimeImplicates = ListOfMinTerms(3)
        primeImplicates = ListOfMinTerms(3)
    }
}