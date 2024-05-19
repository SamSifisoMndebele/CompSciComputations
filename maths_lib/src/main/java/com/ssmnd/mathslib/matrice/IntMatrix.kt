package com.ssmnd.mathslib.matrice

class IntMatrix : Matrix<Int> {
    override val entries: Array<Array<Int>>
    override val isZero: Boolean
        get() = entries.all { it.all { v -> v == 0 } }
    override val isIdentity: Boolean
        get() {
            for ((i, row) in entries.withIndex()) {
                for ((j, entry) in row.withIndex()) {
                    if (i == j && entry != 1 || i != j && entry != 0) return false
                }
            }
            return true
        }

    constructor(entries: Array<Array<Int>>) {
        this.entries = entries
    }
    constructor(size: Int) : this(Array(size) {Array(size){0} })
    constructor(rows: Int, cols: Int) : this(Array(rows) {Array(cols){0} })
    constructor(size: Int, init: (Int, Int) -> Int) : this(size, size, init)
    constructor(rows: Int, cols: Int, init: (Int, Int) -> Int): super() {
        entries = Array(rows) {Array(cols) {0} }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                entries[r][c] = init.invoke(r, c)
            }
        }
    }

    override fun determinant(): Int {
        TODO("Not yet implemented")
    }

    override fun rank(): Int {
        TODO("Not yet implemented")
    }

    override fun nullity(): Int {
        TODO("Not yet implemented")
    }

    override fun flip(): Matrix<Int> {
        TODO("Not yet implemented")
    }

    override fun transpose(): Matrix<Int> {
        TODO("Not yet implemented")
    }

    override fun take(size: Int): Matrix<Int> {
        TODO("Not yet implemented")
    }

    override fun cofactors(element: Any): Matrix<Int> {
        TODO("Not yet implemented")
    }

    override fun times(matrix: Matrix<Int>): Matrix<Int> {
        TODO("Not yet implemented")
    }

    override fun minus(matrix: Matrix<Int>): Matrix<Int> {
        TODO("Not yet implemented")
    }

    override fun plus(matrix: Matrix<Int>, padding: Boolean): Matrix<Int> {
        TODO("Not yet implemented")
    }


    override fun combine(matrix: Matrix<Int>): Matrix<Int> {
        TODO("Not yet implemented")
    }
}