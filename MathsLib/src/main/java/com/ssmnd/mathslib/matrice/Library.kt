package com.ssmnd.matrice

import com.ssmnd.mathslib.expressions.expression.Expression
import com.ssmnd.mathslib.expressions.expression.ExpressionImpl
import com.ssmnd.mathslib.expressions.term.Term
import com.ssmnd.mathslib.matrice.DoubleMatrix
import com.ssmnd.mathslib.matrice.ExpressionMatrix
import com.ssmnd.mathslib.matrice.IntMatrix
import com.ssmnd.mathslib.matrice.Matrix

fun IntMatrix.toDoubleMatrix() : DoubleMatrix {
    return DoubleMatrix(this.rows, this.cols) { r, c ->
        this.entries[r][c].toDouble()
    }
}

fun DoubleMatrix.toIntMatrix() : IntMatrix {
    return IntMatrix(this.rows, this.cols) { r, c ->
        this.entries[r][c].toInt()
    }
}

///**
// * Returns an equivalent [ExpressionMatrix] of this [DoubleMatrix].
// */
//fun DoubleMatrix.toExpressionMatrix() : ExpressionMatrix {
//    val entries = this.entries.map { row ->
//        row.map { it.toExpression }.toTypedArray()
//    }.toTypedArray()
//    return ExpressionMatrix(entries)
//}

/**
 * Returns a double array containing the specified numbers.
 */
fun rowOf(vararg entry: Number): Array<Double> {
    return entry.map { it.toDouble() }.toTypedArray()
}

/**
 * Returns an expression array containing the specified expressions.
 */
fun rowOf(vararg entry: Expression): Array<Expression> {
    return arrayOf(*entry)
}

/**
 * Returns a [Matrix] containing the specified rows of double numbers.
 * @return [DoubleMatrix]
 * @throws [IllegalArgumentException] If the size of the rows is not equal
 */
@Throws(IllegalArgumentException::class)
fun matrixOf(vararg rows: Array<Double>): DoubleMatrix {
    val rowSize = rows[0].size
    if (rows.any{ it.size != rowSize }) throw IllegalArgumentException("The size of rows is not equal.")
    return DoubleMatrix(arrayOf(*rows))
}

/**
 * Returns a [Matrix] containing the specified rows of expressions.
 * @return [ExpressionMatrix]
 * @throws [IllegalArgumentException] If the size of the rows is not equal
 */
@Throws(IllegalArgumentException::class)
fun matrixOf(vararg rows: Array<Expression>): ExpressionMatrix {
    val rowSize = rows[0].size
    if (rows.any { it.size != rowSize }) throw IllegalArgumentException("The size of rows is not equal.")
    return ExpressionMatrix(arrayOf(*rows))
}

/**
 * Returns a double square [Matrix] of specified size, containing the specified default number.
 * @param size Number of rows and columns in the matrix.
 * @param default Default value for all entries
 * @return [DoubleMatrix]
 */
fun matrixOf(size: Int, default : Number): DoubleMatrix {
    return DoubleMatrix(Array(size) {Array(size) { default.toDouble() } })
}

/**
 * Returns a expression square [Matrix] of specified size, containing the specified default expression.
 * @param size Number of rows and columns in the matrix.
 * @param default Default expression for all entries
 * @return [DoubleMatrix]
 */

fun matrixOf(size: Int, default : Expression): ExpressionMatrix {
    return ExpressionMatrix(Array(size) {Array(size) { default } })
}

/**
 * Returns an expression square [Matrix] of specified size, containing the specified default term.
 * @param size Number of rows and columns in the matrix.
 * @param default Default term for all entries
 * @return [DoubleMatrix]
 */
fun matrixOf(size: Int, default : Term): ExpressionMatrix {
    return ExpressionMatrix(Array(size) {Array(size) { ExpressionImpl(default) } })
}

/**
 * Returns a double square zero [Matrix] of specified size.
 * @param size Number of rows and columns in the matrix.
 * @return [DoubleMatrix]
 */
fun doubleMatrixOf(size: Int): DoubleMatrix {
    return DoubleMatrix(Array(size) {Array(size) { 0.0 } })
}

/**
 * Returns a expression square zero [Matrix] of specified size.
 * @param size Number of rows and columns in the matrix.
 * @return [DoubleMatrix]
 */
fun expressionMatrixOf(size: Int): Matrix<Expression> {
    return ExpressionMatrix(Array(size) {Array(size) { Expression.ZERO } })
}

/**
 * Returns a double [Matrix] of specified size, containing the specified default number.
 * @param rows Number of rows in the matrix.
 * @param cols Number of columns in the matrix.
 * @param default Default value for all entries
 * @return [DoubleMatrix]
 */
fun matrixOf(rows: Int, cols: Int, default : Number): Matrix<Double> {
    return DoubleMatrix(Array(rows) {Array(cols) { default.toDouble() } })
}

/**
 * Returns an expression [Matrix] of specified size, containing the specified default expression.
 * @param rows Number of rows in the matrix.
 * @param cols Number of columns in the matrix.
 * @param default Default value for all entries
 * @return [DoubleMatrix]
 */
fun matrixOf(rows: Int, cols: Int, default : ExpressionImpl): Matrix<Expression> {
    return ExpressionMatrix(Array(rows) {Array(cols) { default } })
}

/**
 * Returns an expression [Matrix] of specified size, containing the specified default expression.
 * @param rows Number of rows in the matrix.
 * @param cols Number of columns in the matrix.
 * @param default Default value for all entries
 * @return [DoubleMatrix]
 */
fun matrixOf(rows: Int, cols: Int, default : Term): Matrix<Expression> {
    return ExpressionMatrix(Array(rows) {Array(cols) { ExpressionImpl(default) } })
}

/**
 * Returns a double square zero [Matrix] specified size.
 * @param rows Number of rows in the matrix.
 * @param cols Number of columns in the matrix.
 * @return [DoubleMatrix]
 */
fun doubleMatrixOf(rows: Int, cols: Int): Matrix<Double> {
    return DoubleMatrix(Array(rows) {Array(cols) { 0.0 } })
}

/**
 * Returns a expression square zero [Matrix] of specified size.
 * @param rows Number of rows in the matrix.
 * @param cols Number of columns in the matrix.
 * @return [DoubleMatrix]
 */
fun expressionMatrixOf(rows: Int, cols: Int): Matrix<Expression> {
    return ExpressionMatrix(Array(rows) {Array(cols) { Expression.ZERO } })
}

/**
 * Returns a double square identity [Matrix] of specified size.
 * @param size Number of rows and columns in the matrix.
 * @return [DoubleMatrix]
 */
fun doubleIdentityMatrix(size: Int) : Matrix<Double> {
    val res = Array(size) { Array(size) {0.0} }
    for (i in 0 until size) res[i][i] = 1.0
    return DoubleMatrix(res)
}

/**
 * Returns an expression square identity [Matrix] of specified size.
 * @param size Number of rows and columns in the matrix.
 * @return [DoubleMatrix]
 */
fun expressionIdentityMatrix(size: Int) : Matrix<Expression> {
    val res = Array(size) { Array<Expression>(size) { Expression.ZERO } }
    for (i in 0 until size) res[i][i] = Expression.ONE
    return ExpressionMatrix(res)
}


