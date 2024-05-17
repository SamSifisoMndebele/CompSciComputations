package com.ssmnd.karnaughmaps.logic

import com.ssmnd.karnaughmaps.utils.BinaryBits
import com.ssmnd.karnaughmaps.utils.ListOfMinterms

class Karnaugh2Variables(iArr: IntArray, iArr2: IntArray?) {
    private var allEssencialMinterms = ArrayList<Int>()
    private val allMinTerms: ListOfMinterms
    private var minTerms0ones: ListOfMinterms
    private var minTerms1ones: ListOfMinterms
    private val minTerms2ones: ListOfMinterms
    private val notPrimeImplicants: ListOfMinterms
    private val primeImplicants: ListOfMinterms
    private fun intArrayToArrayList(iArr: IntArray): ArrayList<Int> {
        val arrayList = ArrayList<Int>()
        for (valueOf in iArr) {
            arrayList.add(Integer.valueOf(valueOf))
        }
        return arrayList
    }

    fun executeKarnaugh(): ArrayList<ListOfMinterms> {
        val arrayList = ArrayList<ListOfMinterms>()
        return if (allEssencialMinterms.size == 0) {
            arrayList.add(ListOfMinterms(2))
            arrayList
        } else if (allMinTerms.size() == 4) {
            val arrayList2: ArrayList<Int> = ArrayList<Int>()
            for (i in 0..3) {
                arrayList2.add(Integer.valueOf(i))
            }
            val listOfMinterms = ListOfMinterms(2)
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
            primeImplicants.removeString(notPrimeImplicants.getString(i))
        }
        primeImplicants.removeDuplicates()
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
        val arrayList4: ArrayList<ListOfMinterms> = ArrayList<ListOfMinterms>()
        var i5 = 0
        for (i6 in arrayList3.indices) {
            val arrayList5 = arrayList3[i6] as ArrayList<*>
            val listOfMinterms2 = ListOfMinterms(2)
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
        val listOfMinterms3 = ListOfMinterms(2)
        if (listOfMinterms.size() == 1) {
            primeImplicants.add(listOfMinterms.getString(0), listOfMinterms.getIntegers(0))
        }
        if (listOfMinterms2.size() == 1) {
            primeImplicants.add(listOfMinterms2.getString(0), listOfMinterms2.getIntegers(0))
        }
        for (i in 0 until listOfMinterms.size()) {
            var z = true
            for (i2 in 0 until listOfMinterms2.size()) {
                val dashes = setDashes(listOfMinterms.getString(i), listOfMinterms2.getString(i2))
                if (dashes != "") {
                    notPrimeImplicants.add(
                        listOfMinterms.getString(i),
                        listOfMinterms.getIntegers(i)
                    )
                    notPrimeImplicants.add(
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
                primeImplicants.add(listOfMinterms.getString(i), listOfMinterms.getIntegers(i))
            }
        }
        for (i3 in 0 until listOfMinterms2.size()) {
            var z2 = true
            for (i4 in 0 until listOfMinterms.size()) {
                if (setDashes(listOfMinterms2.getString(i3), listOfMinterms.getString(i4)) != "") {
                    notPrimeImplicants.add(
                        listOfMinterms2.getString(i3),
                        listOfMinterms2.getIntegers(i3)
                    )
                    notPrimeImplicants.add(
                        listOfMinterms.getString(i4),
                        listOfMinterms.getIntegers(i4)
                    )
                    z2 = false
                }
            }
            if (z2) {
                primeImplicants.add(listOfMinterms2.getString(i3), listOfMinterms2.getIntegers(i3))
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
            }
        }
    }

    init {
        allMinTerms = ListOfMinterms(iArr, iArr2, 2)
        allEssencialMinterms = intArrayToArrayList(iArr)
        minTerms0ones = ListOfMinterms(2)
        minTerms1ones = ListOfMinterms(2)
        minTerms2ones = ListOfMinterms(2)
        notPrimeImplicants = ListOfMinterms(2)
        primeImplicants = ListOfMinterms(2)
    }
}