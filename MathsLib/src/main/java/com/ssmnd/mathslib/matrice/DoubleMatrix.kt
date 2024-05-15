package com.ssmnd.mathslib.matrice

import kotlin.math.max
import kotlin.math.min

class DoubleMatrix : Matrix<Double> {
    override val entries: Array<Array<Double>>
    override val isZero: Boolean
        get() = entries.all { it.all { v -> v == 0.0 } }
    override val isIdentity: Boolean
        get() {
            for ((i, row) in entries.withIndex()) {
                for ((j, entry) in row.withIndex()) {
                    if (i == j && entry != 1.0 || i != j && entry != 0.0) return false
                }
            }
            return true
        }

    constructor(entries: Array<Array<Double>>) {
        this.entries = entries
    }
    constructor(size: Int) : this(Array(size) {Array(size){0.0} })
    constructor(rows: Int, cols: Int) : this(Array(rows) {Array(cols){0.0} })
    constructor(size: Int, init: (Int, Int) -> Double) : this(size, size, init)
    constructor(rows: Int, cols: Int, init: (Int, Int) -> Double) {
        entries = Array(rows) {Array(cols) {0.0} }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                entries[r][c] = init.invoke(r, c)
            }
        }
    }

    override fun determinant(): Double {
        TODO("Not yet implemented")
    }

    override fun rank(): Double {
        TODO("Not yet implemented")
    }

    override fun nullity(): Double {
        TODO("Not yet implemented")
    }

    override fun flip(): Matrix<Double> {
        TODO("Not yet implemented")
    }

    override fun transpose(): Matrix<Double> {
        TODO("Not yet implemented")
    }

    override fun take(size: Int): Matrix<Double> {
        val rows = min(size, rows)
        val cols = min(size, cols)
        val result = Array(rows) { Array(cols) {0.0} }
        for (r in 0 until rows)
            for (c in 0 until cols)
                result[r][c] = entries[r][c]
        return DoubleMatrix(result)
    }

    override fun cofactors(element: Any): Matrix<Double> {
        TODO("Not yet implemented")
    }

    override fun times(matrix: Matrix<Double>): Matrix<Double> {
        TODO("Not yet implemented")
    }

    override fun minus(matrix: Matrix<Double>): Matrix<Double> {
        TODO("Not yet implemented")
    }

    override fun plus(matrix: Matrix<Double>, padding : Boolean): Matrix<Double> {
        return if (padding) {
            val array = arrayOf<Double>()
            val result = Array(max(this.rows, matrix.rows)) { Array(max(this.cols, matrix.cols)) {0.0} }
            for ((r, row) in result.withIndex())
                for (c in row.indices) {
                    val entryA = entries.getOrElse(r){ array }.getOrElse(c) { 0.0 }
                    val entryB = matrix.entries.getOrElse(r){ array }.getOrElse(c) { 0.0 }
                    result[r][c] = entryA + entryB
                }
            DoubleMatrix(result)
        } else {
            val result = this.entries.zip(matrix.entries) { A, B ->
                A.zip(B) {a, b -> a + b}.toTypedArray()
            }.toTypedArray()
            DoubleMatrix(result)
        }
//        assert(rows == matrix.rows && cols == matrix.cols)
//        val result = this.entries.zip(matrix.entries) { A, B ->
//            A.zip(B) {a, b -> a + b}.toTypedArray()
//        }.toTypedArray()

//        for (r in 0 until rows)
//            for (c in 0 until cols)
//                result[r][c] = entries[r][c] + matrix.entries[r][c]
//        return DoubleMatrix(result)
    }

    override fun combine(matrix: Matrix<Double>): Matrix<Double> {
        TODO("Not yet implemented")
    }
}