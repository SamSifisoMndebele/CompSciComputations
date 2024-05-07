package com.ssmnd.mathslib.matrice

import com.ssmnd.mathslib.utils.Latex


abstract class Matrix<T> {
    abstract val entries: Array<Array<T>>
    val rows: Int get() = entries.size
    val cols: Int get() = entries[0].size
    abstract val isZero: Boolean
    val isOne: Boolean get() = isIdentity
    abstract val isIdentity: Boolean

    override fun toString() : String {
        val sb = StringBuilder()
        entries.forEachIndexed { r, rows ->
            rows.forEach { entry ->
                sb.append("$entry ")
            }
            if (entries.size - 1 != r)  sb.append("\n")
        }
        return sb.toString()
    }
    fun toLatex() : Latex {
        val string = StringBuilder()
        string.append("""\begin{pmatrix}""")
        string.append(latexString())
        string.append("""\end{pmatrix}""")
        return Latex(string.toString())
    }
    private fun latexString() : String {
        val string = StringBuilder()
        for (r in entries.indices) {
            for (c in entries[r].indices) {
                string.append(entries[r][c].toString())
                if (c != rows-1) string.append('&')
            }
            if (r != entries.size-1) string.append("""\\""")
        }
        return string.toString()
    }

    fun determinantLatex() : Latex {
        val string = StringBuilder()
        string.append("""\begin{vmatrix}""")
        string.append(latexString())
        string.append("""\end{vmatrix}""")
        string.append(""" = ${this.determinant().toString()}""")
        return Latex(string.toString())
    }

    abstract fun determinant(): T
    abstract fun rank(): T
    abstract fun nullity(): T

    abstract fun flip(): Matrix<T>
    abstract fun transpose(): Matrix<T>
//    abstract fun invert(): Matrix<T>

    abstract infix fun take(size: Int): Matrix<T>
    abstract infix fun cofactors(element: Any): Matrix<T>
    abstract infix fun combine(matrix: Matrix<T>): Matrix<T>

    operator fun set(row: Int, col: Int, value: T) {
        entries[row][col] = value
    }
    operator fun get(row: Int, col: Int) : T = entries[row][col]
    abstract fun plus(matrix: Matrix<T>, padding : Boolean = false): Matrix<T>
    operator fun plus(matrix: Matrix<T>): Matrix<T> = this.plus(matrix, true)
    abstract operator fun minus(matrix: Matrix<T>): Matrix<T>
    abstract operator fun times(matrix: Matrix<T>): Matrix<T>
}