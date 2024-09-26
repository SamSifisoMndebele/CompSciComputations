package com.compscicomputations.karnaugh_maps.logic

import com.compscicomputations.karnaugh_maps.utils.BinaryBits
import com.compscicomputations.karnaugh_maps.utils.ListOfMinTerms

class Karnaugh2Variables(iArr: IntArray, iArr2: IntArray?) {
    private var allEssencialMinterms = ArrayList<Int>()
    private val allMinTerms: ListOfMinTerms = ListOfMinTerms(iArr, iArr2, 2)
    private var minTerms0ones: ListOfMinTerms
    private var minTerms1ones: ListOfMinTerms
    private val minTerms2ones: ListOfMinTerms
    private val notPrimeImplicants: ListOfMinTerms
    private val primeImplicants: ListOfMinTerms
    private fun intArrayToArrayList(iArr: IntArray): ArrayList<Int> {
        val arrayList = ArrayList<Int>()
        for (valueOf in iArr) {
            arrayList.add(Integer.valueOf(valueOf))
        }
        return arrayList
    }

    fun executeKarnaugh(): ArrayList<ListOfMinTerms> {
        val arrayList = ArrayList<ListOfMinTerms>()
        return if (allEssencialMinterms.size == 0) {
            arrayList.add(ListOfMinTerms(2))
            arrayList
        } else if (allMinTerms.size() == 4) {
            val arrayList2: ArrayList<Int> = ArrayList<Int>()
            for (i in 0..3) {
                arrayList2.add(Integer.valueOf(i))
            }
            val listOfMinterms = ListOfMinTerms(2)
            listOfMinterms.add("---", arrayList2)
            arrayList.add(listOfMinterms)
            arrayList
        } else {
            splitCubes()
            findPrimeImplicants()
            findPrimePermutations(primeImplicants)
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

    private fun findPrimeImplicants() {
        minTerms0ones = addOneBitDifferences(minTerms0ones, minTerms1ones)
        val addOneBitDifferences = addOneBitDifferences(minTerms1ones, minTerms2ones)
        minTerms1ones = addOneBitDifferences
        minTerms0ones = addOneBitDifferences(minTerms0ones, addOneBitDifferences)
        for (i in 0 until notPrimeImplicants.size()) {
            notPrimeImplicants.getString(i)?.let { primeImplicants.removeString(it) }
        }
        primeImplicants.removeDuplicates()
    }

    private fun findPrimePermutations(listOfMinterms: ListOfMinTerms): ArrayList<ListOfMinTerms> {
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
            if (arrayList2.containsAll(allEssencialMinterms)) {
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
        val arrayList4: ArrayList<ListOfMinTerms> = ArrayList<ListOfMinTerms>()
        var i5 = 0
        for (i6 in arrayList3.indices) {
            val arrayList5 = arrayList3[i6] as ArrayList<*>
            val listOfMinTerms2 = ListOfMinTerms(2)
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
        val listOfMinTerms3 = ListOfMinTerms(2)
        if (listOfMinterms.size() == 1) {
            primeImplicants.add(listOfMinterms.getString(0), listOfMinterms.getIntegers(0))
        }
        if (listOfMinTerms2.size() == 1) {
            primeImplicants.add(listOfMinTerms2.getString(0), listOfMinTerms2.getIntegers(0))
        }
        for (i in 0 until listOfMinterms.size()) {
            var z = true
            for (i2 in 0 until listOfMinTerms2.size()) {
                val dashes = listOfMinterms.getString(i)
                    ?.let { listOfMinTerms2.getString(i2)?.let { it1 -> setDashes(it, it1) } }
                if (dashes != "") {
                    notPrimeImplicants.add(
                        listOfMinterms.getString(i),
                        listOfMinterms.getIntegers(i)
                    )
                    notPrimeImplicants.add(
                        listOfMinTerms2.getString(i2),
                        listOfMinTerms2.getIntegers(i2)
                    )
                    val arrayList: ArrayList<Int> = ArrayList()
                    arrayList.addAll(listOfMinterms.getIntegers(i))
                    arrayList.addAll(listOfMinTerms2.getIntegers(i2))
                    listOfMinTerms3.add(dashes, arrayList)
                    z = false
                }
            }
            if (z) {
                primeImplicants.add(listOfMinterms.getString(i), listOfMinterms.getIntegers(i))
            }
        }
        for (i3 in 0 until listOfMinTerms2.size()) {
            var z2 = true
            for (i4 in 0 until listOfMinterms.size()) {
                if (listOfMinTerms2.getString(i3)
                        ?.let { listOfMinterms.getString(i4)?.let { it1 -> setDashes(it, it1) } } != "") {
                    notPrimeImplicants.add(
                        listOfMinTerms2.getString(i3),
                        listOfMinTerms2.getIntegers(i3)
                    )
                    notPrimeImplicants.add(
                        listOfMinterms.getString(i4),
                        listOfMinterms.getIntegers(i4)
                    )
                    z2 = false
                }
            }
            if (z2) {
                primeImplicants.add(listOfMinTerms2.getString(i3), listOfMinTerms2.getIntegers(i3))
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
            }
        }
    }

    init {
        allEssencialMinterms = intArrayToArrayList(iArr)
        minTerms0ones = ListOfMinTerms(2)
        minTerms1ones = ListOfMinTerms(2)
        minTerms2ones = ListOfMinTerms(2)
        notPrimeImplicants = ListOfMinTerms(2)
        primeImplicants = ListOfMinTerms(2)
    }
}