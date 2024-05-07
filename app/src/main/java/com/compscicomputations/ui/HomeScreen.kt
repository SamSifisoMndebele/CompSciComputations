package com.compscicomputations.ui

import androidx.compose.runtime.Composable

/*private val term1 = TermImpl.ONE
private val term2 = TermImpl(2)
private val termN2 = TermImpl(-2)
private val termZero = TermImpl(0)
private val termNaN = TermImpl(Double.NaN)
private val termPosInf = TermImpl(Double.POSITIVE_INFINITY)
private val termNegInf = TermImpl(Double.NEGATIVE_INFINITY)
private val termX1 = TermImpl(-4,'x', 1)
private val termX3 = TermImpl(15,'x', 3)
private val termXN2 = TermImpl('x', -2)
private val term3XN2 = TermImpl(3,'x', -2)
private val termY2 = TermImpl(15,'y', 2)*/

@Composable
fun HomeScreen() {
    /*val matrix0 = matrixOf(rowOf(9,5,3,5), rowOf(0,1,7,8))
    val matrix1 = NumMatrix(arrayOf(arrayOf(termX1)))
    val matrix2 = NumMatrix(arrayOf(
        arrayOf(1.1,2,3),
        arrayOf(4,5,6),
        arrayOf(7,8,9)
    ))
    val matrix3 = matrixOf(10, 0)
    val matrix30 = matrixOf(10, 5, 0)
    val matrix4 = identityMatrix(4)

    val matrix5 = matrixOf(3, termX3)*/


    /*var expression = termX3 + term1 * 2
    val expression1 = termX1 + termX3 * term3XN2
    val expression2 = termY2 + termXN2 * termY2+ termX1 + 1

    expression *= 5

    Column {
        *//*MathText(text = matrix0.toString())
        MathText(text = matrix1.toString())
        MathText(text = matrix2.toString())
        MathText(text = matrix3.toString())
        MathText(text = matrix4.toString())*//*

        MathText(text = { "5($termX3 + $term1 * 2) = $expression" })
        MathText(text = { "$termX1 + $termX3 * $term3XN2 = $expression1" })
        MathText(text = { "$termY2 + $termXN2 * $termY2 + $termX1 + 1 = $expression2" })
        HorizontalDivider()
        DetMatrix(matrix = matrixOf(rowOf(1, 2), rowOf(1, 2)))
        //MathText(text = matrix5.toString())
    }*/
}