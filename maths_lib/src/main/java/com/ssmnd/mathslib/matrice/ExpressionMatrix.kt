package com.ssmnd.mathslib.matrice

import com.ssmnd.mathslib.expressions.expression.Expression

class ExpressionMatrix : Matrix<Expression> {
    override val entries: Array<Array<Expression>>
    override val isZero: Boolean
        get() = TODO("Not yet implemented")
    override val isIdentity: Boolean
        get() = TODO("Not yet implemented")

    constructor(entries: Array<Array<Expression>>) {
        this.entries = entries
    }
    constructor(size: Int) : this(Array(size) {Array(size){ Expression.ZERO } })
    constructor(rows: Int, cols: Int) : this(Array(rows) {Array(cols){ Expression.ZERO } })
    constructor(size: Int, init: (Int, Int) -> Expression) : this(size, size, init)
    constructor(rows: Int, cols: Int, init: (Int, Int) -> Expression) {
        entries = Array(rows) {Array(cols) { Expression.ZERO } }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                entries[r][c] = init.invoke(r, c)
            }
        }
    }

    override fun determinant(): Expression {
        TODO("Not yet implemented")
    }

    override fun rank(): Expression {
        TODO("Not yet implemented")
    }

    override fun nullity(): Expression {
        TODO("Not yet implemented")
    }

    override fun flip(): Matrix<Expression> {
        TODO("Not yet implemented")
    }

    override fun transpose(): Matrix<Expression> {
        TODO("Not yet implemented")
    }

    override fun take(size: Int): Matrix<Expression> {
        TODO("Not yet implemented")
    }

    override fun cofactors(element: Any): Matrix<Expression> {
        TODO("Not yet implemented")
    }

    override fun times(matrix: Matrix<Expression>): Matrix<Expression> {
        TODO("Not yet implemented")
    }

    override fun minus(matrix: Matrix<Expression>): Matrix<Expression> {
        TODO("Not yet implemented")
    }

    override fun plus(matrix: Matrix<Expression>, padding: Boolean): Matrix<Expression> {
        TODO("Not yet implemented")
    }

    override fun combine(matrix: Matrix<Expression>): Matrix<Expression> {
        TODO("Not yet implemented")
    }

}