package com.ssmnd.mathslib.expressions.fraction

import com.ssmnd.mathslib.expressions.expression.Expression
import com.ssmnd.mathslib.expressions.term.Term


interface Fraction : Comparable<Fraction> {
    val numerator: Expression
    val denominator: Expression


    val comparableString : String
    override fun compareTo(other: Fraction): Int = other.comparableString.compareTo(comparableString)

    /**
     * Checks if the expression is equals to One.
     * @return [Boolean]
     */
    val isOne: Boolean
        get() = numerator == denominator && !numerator.isZero

    /**
     * Checks if the expression is equals to Zero.
     * @return [Boolean]
     */
    val isZero: Boolean
        get() = numerator.isZero && !denominator.isZero

    /**
     * Checks if the expression is a Number.
     * @return [Boolean]
     */
    val isNumber: Boolean
        get() = numerator.isNumber && denominator.isNumber && !denominator.isZero

    /**
     * Checks if the expression is Not a Number.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isNaN(vararg variables: Pair<Char, Number>): Boolean =
        (numerator.isNaN(*variables) || denominator.isNaN()) ||
                numerator.isZero && denominator.isZero

    /**
     * Checks if the expression is Negative.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isNegative(vararg variables: Pair<Char, Number>): Boolean =
        numerator.isPositive(*variables) && denominator.isNegative(*variables) ||
                numerator.isNegative(*variables) && denominator.isPositive(*variables)

    /**
     * Checks if the expression is Positive.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isPositive(vararg variables: Pair<Char, Number>): Boolean =
        numerator.isPositive(*variables) && denominator.isPositive(*variables) ||
                numerator.isNegative(*variables) && denominator.isNegative(*variables)

    /**
     * Checks if the expression is Finite.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isFinite(vararg variables: Pair<Char, Number>): Boolean =
        numerator.isFinite(*variables) && denominator.isFinite(*variables) && !denominator.isZero

    /**
     * Checks if the expression is Infinite.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isInfinite(vararg variables: Pair<Char, Number>): Boolean =
        numerator.isInfinite(*variables) && denominator.isFinite(*variables) ||
                numerator.isFinite(*variables) && denominator.isZero


    /**
     * The value of the term given all the values of it variables.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Double]: The value of the term.
     */
    fun value(vararg variables: Pair<Char, Number>) : Double {
        val num = numerator.value(*variables)
        val den = denominator.value(*variables)
        return num / den
    }

    /**
     * Latex string representation of the term.
     * @return [String]
     */
    override fun toString(): String



    /**
     * Returns the positive of this value.
     * @return [Fraction]
     */
    operator fun unaryPlus(): Fraction {
        return this
    }

    /**
     * Returns the negative of this value.
     * @return [Fraction]
     */
    operator fun unaryMinus() : Fraction



    /**
     * Returns this value incremented by one.
     * @return [Fraction]
     */
    operator fun inc(): Fraction

    /**
     * Returns this value decremented by one.
     * @return [Fraction]
     */
    operator fun dec(): Fraction

    /**
     * Adds the number value to this value.
     * @param number A [Number]
     * @return [Fraction]
     */
    operator fun plus(number: Number): Fraction

    /**
     * Adds the number value to this value.
     * @param term A [Term]
     * @return [Fraction]
     */
    operator fun plus(term: Term): Fraction

    /**
     * Adds the number value to this value.
     * @param expression A [Fraction]
     * @return [Fraction]
     */
    operator fun plus(expression: Fraction): Fraction

    /*operator fun plusAssign(number: Number)
    operator fun plusAssign(term: Term)
    operator fun plusAssign(expression: Fraction)*/

    /**
     * Subtracts the number value from this value.
     * @param number A [Number]
     * @return [Fraction]
     */
    operator fun minus(number: Number): Fraction

    /**
     * Subtracts the term value from this value.
     * @param term A [Term]
     * @return [Fraction]
     */
    operator fun minus(term: Term): Fraction

    /**
     * Subtracts the expression value from this value.
     * @param expression A [Fraction]
     * @return [Fraction]
     */
    operator fun minus(expression: Fraction): Fraction

    /**
     * Multiplies this value by the number value.
     * @param number A [Number]
     * @return [Fraction]
     */
    operator fun times(number: Number): Fraction

    /**
     * Multiplies this value by the term value.
     * @param term A [Term]
     * @return [Fraction]
     */
    operator fun times(term: Term): Fraction

    /**
     * Multiplies this value by the expression value.
     * @param expression A [Fraction]
     * @return [Fraction]
     */
    operator fun times(expression: Fraction): Fraction

    /**
     * Adds the number value to this value.
     * @param expression A [Expression]
     * @return [Expression]
     */
    operator fun plus(expression: Expression): Fraction

    /**
     * Subtracts the expression value from this value.
     * @param expression A [Expression]
     * @return [Expression]
     */
    operator fun minus(expression: Expression): Fraction

    /**
     * Multiplies this value by the expression value.
     * @param expression A [Expression]
     * @return [Expression]
     */
    operator fun times(expression: Expression): Fraction



    infix fun derivative(n : Int) {

    }

    val firstDerivative
        get() = derivative(1)
    val secondDerivative
        get() = derivative(2)
}