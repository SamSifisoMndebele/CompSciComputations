package com.ssmnd.matrice

import com.ssmnd.mathslib.matrice.DoubleMatrix

fun main() {
    val m1 = DoubleMatrix(4, 3)
    m1[0,0] = 10.0
    m1[1,2] = 30.0
    m1[3,1] = 25.0

    val m2 = DoubleMatrix(arrayOf(
        arrayOf(1.0, 0.0, 0.0, 1.0),
        arrayOf(0.0, 1.0, 0.0, 2.3),
        arrayOf(0.0, 2.0, 1.0, 25.2)
    ))

    println(m1)
    println(m2)
    println(m2 + m1)
}