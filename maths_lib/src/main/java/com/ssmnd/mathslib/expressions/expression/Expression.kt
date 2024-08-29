package com.ssmnd.mathslib.expressions.expression

import com.ssmnd.mathslib.expressions.term.Term


interface Expression : Comparable<Expression> {
    /**
     * The sorted set of [Term] in the expression.
     * @return The sorted set of terms
     */
    val terms: Array<Term>


    val comparableString : String
    override fun compareTo(other: Expression): Int = other.comparableString.compareTo(comparableString)

    /**
     * String representation of the term.
     * @return [String]
     */
    override fun toString(): String

    /**
     * Returns the value of the expression.
     * @param variables variables values set, 0.0 by default.
     * @return [Double] value of the variable.
     */
    fun value(vararg variables: Pair<Char, Number>): Double {
        var value = 0.0
        terms.forEach {term ->
            value += term.value(*variables)
        }
        return value
    }

    /**
     * Checks if the expression is equals to One.
     * @return [Boolean]
     */
    val isOne: Boolean
        get() = terms.size == 1 && terms.first().isOne

    /**
     * Checks if the expression is equals to Zero.
     * @return [Boolean]
     */
    val isZero: Boolean
        get() = terms.all{ it.isZero }

    /**
     * Checks if the expression is a Number.
     * @return [Boolean]
     */
    val isNumber: Boolean
        get() = terms.all { it.isNumber }

    /**
     * Checks if the expression is Not a Number.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isNaN(vararg variables: Pair<Char, Number>): Boolean = terms.any { it.isNaN(*variables) }

    /**
     * Checks if the expression is Negative.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isNegative(vararg variables: Pair<Char, Number>): Boolean = terms.all { it.isNegative(*variables) }

    /**
     * Checks if the expression is Positive.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isPositive(vararg variables: Pair<Char, Number>): Boolean = terms.all { it.isPositive(*variables) }

    /**
     * Checks if the expression is Finite.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isFinite(vararg variables: Pair<Char, Number>): Boolean = terms.all { it.isFinite(*variables) }

    /**
     * Checks if the expression is Infinite.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isInfinite(vararg variables: Pair<Char, Number>): Boolean = terms.any { it.isInfinite(*variables) }


    /**
     * Returns the partial derivative of this term.
     * @param x the variable to derive with respect to.
     * @param n the degree if the derivative. `e.g n = 1 for first derivative, n = 2 for second derivative.`
     * @return [Expression]
     */
    fun partialDerivative(x: Char, n: Int): Expression

    /**
     * Derives this term to the nth derivative
     * @param n the degree of the derivative. `e.g n = 1 for first derivative, n = 2 for second derivative.`
     * @return [Expression]
     */
    fun derivative(n: Int): Expression

    @Suppress("unused")
    val firstDerivative get() = derivative(1)
    @Suppress("unused")
    val secondDerivative get() = derivative(2)

    /**
     * Returns the positive of this value.
     * @return [Expression]
     */
    operator fun unaryPlus(): Expression {
        return this
    }

    /**
     * Returns the negative of this value.
     * @return [Expression]
     */
    operator fun unaryMinus() : Expression

    /**
     * Returns this value incremented by one.
     * @return [Expression]
     */
    operator fun inc(): Expression

    /**
     * Returns this value decremented by one.
     * @return [Expression]
     */
    operator fun dec(): Expression

    /**
     * Adds the number value to this value.
     * @param number A [Number]
     * @return [Expression]
     */
    operator fun plus(number: Number): Expression

    /**
     * Adds the number value to this value.
     * @param term A [Term]
     * @return [Expression]
     */
    operator fun plus(term: Term): Expression

    /**
     * Adds the number value to this value.
     * @param expression A [Expression]
     * @return [Expression]
     */
    operator fun plus(expression: Expression): Expression

    /**
     * Subtracts the number value from this value.
     * @param number A [Number]
     * @return [Expression]
     */
    operator fun minus(number: Number): Expression

    /**
     * Subtracts the term value from this value.
     * @param term A [Term]
     * @return [Expression]
     */
    operator fun minus(term: Term): Expression

    /**
     * Subtracts the expression value from this value.
     * @param expression A [Expression]
     * @return [Expression]
     */
    operator fun minus(expression: Expression): Expression

    /**
     * Multiplies this value by the number value.
     * @param number A [Number]
     * @return [Expression]
     */
    operator fun times(number: Number): Expression

    /**
     * Multiplies this value by the term value.
     * @param term A [Term]
     * @return [Expression]
     */
    operator fun times(term: Term): Expression

    /**
     * Multiplies this value by the expression value.
     * @param expression A [Expression]
     * @return [Expression]
     */
    //operator fun times(expression: Expression): Expression

    /**
     * Divides this value by the number value.
     * @param number A [Number]
     * @return [Expression]
     */
    operator fun div(number: Number): Expression

    /**
     * Divides this value by the term value.
     * @param term A [Term]
     * @return [Expression]
     */
    operator fun div(term: Term): Expression

    /**
     * Divides this value by the expression value.
     * @param expression A [Expression]
     * @return [RationalExpression]
     */
    //operator fun div(expression: Expression): Expression

    /**
     * Calculates the remainder of truncating division of this value (dividend) by the number value (divisor).
     * The result is either zero or has the same sign as the dividend and has the absolute value less than the absolute value of the divisor.
     * @param number A [Number]
     * @return [Expression]
     */
    //operator fun rem(number: Number): ExpressionInterface

    /**
     * Calculates the remainder of truncating division of this value (dividend) by the term value (divisor).
     * The result is either zero or has the same sign as the dividend and has the absolute value less than the absolute value of the divisor.
     * @param term A [Term]
     * @return [Expression]
     */
    //operator fun rem(term: Term): ExpressionInterface

    /**
     * Calculates the remainder of truncating division of this value (dividend) by the expression value (divisor).
     * The result is either zero or has the same sign as the dividend and has the absolute value less than the absolute value of the divisor.
     * @param expression A [Expression]
     * @return [Expression]
     */
    //operator fun rem(expression: ExpressionInterface): ExpressionInterface

    //operator fun Expression.contains(expression: Expression) : Boolean
    //operator fun Expression.contains(number: Number) : Boolean
    //operator fun Expression.contains(term: Term) : Boolean

    //infix fun pow(n: Term) : ExpressionInterface
    //infix fun pow(n: Int) : ExpressionInterface
    //fun squared() : ExpressionInterface
    //fun cubed() : ExpressionInterface
    //fun sqrt() : ExpressionInterface
    //fun cbrt() : ExpressionInterface

//    override fun equals(other: Any?): Boolean
//    override fun hashCode(): Int

    companion object {
        /**
         * A constant holding the "not a number" value of Term.
         * */
        val NaN: Expression = ExpressionImpl(arrayOf(Term.NaN))

        /**
         * A constant holding the negative infinity value of Expression.
         */
        val NEGATIVE_INFINITY: Expression = ExpressionImpl(arrayOf(Term.NEGATIVE_INFINITY))

        /**
         * A constant holding the positive infinity value of Expression.
         */
        val POSITIVE_INFINITY: Expression = ExpressionImpl(arrayOf(Term.POSITIVE_INFINITY))

        /**
         * A constant holding the zero value of Expression.
         */
        val ZERO: Expression = ExpressionImpl(arrayOf(Term.ZERO))

        /**
         * A constant holding the one value of Expression.
         */
        val ONE: Expression = ExpressionImpl(arrayOf(Term.ONE))
    }
}