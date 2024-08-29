package com.ssmnd.mathslib.expressions.expression

import com.ssmnd.mathslib.expressions.term.Term

class ExpressionImpl(override val terms: Array<Term>) : Expression {
    constructor(term: Term) : this(arrayOf(term))
    /*override val terms: Array<Term> = terms.ifEmpty { arrayOf(ExpTerm.ZERO) }

    override fun toString(): String {
        val string = StringBuilder()
        string.append(terms[0].toString())
        for (i in 1 until terms.size) {
            val term = terms[i]
            if (term.coefficient > 0) string.append(" + ")
                .append(term.toString())
            else string.append(" - ")
                .append(term.toString().replaceFirst("-",""))
        }
        return string.toString()
    }

    override fun value(vararg vars: Pair<Char, Number>): Double {
        var value = 0.0
        for (term in terms) {
            value += term.value(*vars)
        }
        return value
    }*/

    override val comparableString: String
        get() = TODO("Not yet implemented")

    override fun toString(): String {
        TODO("Not yet implemented")
    }

    override fun partialDerivative(x: Char, n: Int): Expression {
        TODO("Not yet implemented")
    }

    override fun derivative(n: Int): Expression {
        TODO("Not yet implemented")
    }

    override fun unaryMinus(): Expression {
        TODO("Not yet implemented")
    }

    override fun inc(): Expression {
        TODO("Not yet implemented")
    }

    override fun dec(): Expression {
        TODO("Not yet implemented")
    }

    override fun plus(number: Number): Expression {
        TODO("Not yet implemented")
    }

    override fun plus(term: Term): Expression {
        TODO("Not yet implemented")
    }

    override fun plus(expression: Expression): Expression {
        TODO("Not yet implemented")
    }

    override fun minus(number: Number): Expression {
        TODO("Not yet implemented")
    }

    override fun minus(term: Term): Expression {
        TODO("Not yet implemented")
    }

    override fun minus(expression: Expression): Expression {
        TODO("Not yet implemented")
    }

    override fun times(number: Number): Expression {
        TODO("Not yet implemented")
    }

    override fun times(term: Term): Expression {
        TODO("Not yet implemented")
    }

    override fun div(number: Number): Expression {
        TODO("Not yet implemented")
    }

    override fun div(term: Term): Expression {
        TODO("Not yet implemented")
    }

}