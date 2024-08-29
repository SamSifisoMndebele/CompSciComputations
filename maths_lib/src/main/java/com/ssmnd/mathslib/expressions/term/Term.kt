package com.ssmnd.mathslib.expressions.term

import com.ssmnd.mathslib.expressions.expression.Expression
import com.ssmnd.mathslib.expressions.variable.Variable
import com.ssmnd.mathslib.expressions.variable.Variable.Companion.variableOf
import com.ssmnd.mathslib.utils.Latex
import java.util.*
import kotlin.math.pow

interface Term : Comparable<Term> {
    /**
     * The coefficient of the term.
     */
    val coefficient: Double

    /**
     * The set of variables product in the term.
     */
    val variables: TreeSet<Variable>

    /**
     * String representation of the term.
     * @return [String]
     */
    override fun toString(): String

    val comparableString : String
    override fun compareTo(other: Term): Int = other.comparableString.compareTo(comparableString)

    /**
     * Latex representation of the term.
     * @return [Latex]
     */
    fun toLatex(): Latex

    /**
     * Returns the value of the term.
     * @param variables variables values set, 0.0 by default.
     * @return [Double] value of the variable.
     */
    fun value(vararg variables: Pair<Char, Number>): Double

    /**
     * Checks if the term is a Number without variables.
     * @return [Boolean]
     */
    val isNumber: Boolean

    /**
     * Checks if the term is equals to One.
     * @return [Boolean]
     */
    val isOne : Boolean

    /**
     * Checks if the term is equals to Zero.
     * @return [Boolean]
     */
    val isZero : Boolean

    /**
     * Checks if the term is Not a Number.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isNaN(vararg variables: Pair<Char, Number>) = value(*variables).isNaN()

    /**
     * Checks if the term is Negative.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isNegative(vararg variables: Pair<Char, Number>) = value(*variables) < 0.0

    /**
     * Checks if the term is Positive.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isPositive(vararg variables: Pair<Char, Number>) = value(*variables) >= 0.0

    /**
     * Checks if the term is Finite.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isFinite(vararg variables: Pair<Char, Number>) = value(*variables).isFinite()

    /**
     * Checks if the term is Infinite.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    fun isInfinite(vararg variables: Pair<Char, Number>) = value(*variables).isInfinite()


    /**
     * Checks if the term is a like term with other term.
     * @param other The term to check if is like term with this term
     * @return [Boolean]
     */
    fun isLikeTerm(other: Any) : Boolean

    /**
     * Returns the partial derivative of this term.
     * @param x the variable to derive with respect to.
     * @param n the degree if the derivative. `e.g n = 1 for first derivative, n = 2 for second derivative.`
     * @return [Term]
     */
    fun partialDerivative(x: Char, n: Int): Term /*{
        var coefficient = this.coefficient
        var removeVar : Variable
        for (variable in variables) {
            removeVar = variable

        }


        //return PolynomialTerm(coefficient * exp, variables.also { it[base_convert] = exp - 1 })
        TODO()
    }*/

    /**
     * Returns the derivative of this term.
     * @param /n the degree if the derivative. `e.g n = 1 for first derivative, n = 2 for second derivative.`
     * @return [Expression]
     */
    //fun derivative(n: Int): Expression

    /**
     * Returns the positive of this value.
     * @return [Term]
     */
    operator fun unaryPlus(): Term = this

    /**
     * Returns the negative of this value.
     * @return [Term]
     */
    operator fun unaryMinus(): Term = TermImpl(-coefficient, variables)

    /**
     * Adds the term value to this value.
     * @param term A [Term]
     * @return [Expression]
     * @throws ArithmeticException if the term is not a like term
     */
    @Throws(ArithmeticException::class)
    operator fun plus(term: Term): Term

    /**
     * Subtracts the term value from this value.
     * @param term A [Term]
     * @return [Expression]
     * @throws ArithmeticException if the term is not a like term
     */
    @Throws(ArithmeticException::class)
    operator fun minus(term: Term): Term = this + -term

    /**
     * Multiplies this value by the number value.
     * @param number A [Number]
     * @return [Term]
     */
    operator fun times(number: Number): Term

    /**
     * Multiplies this value by the term value.
     * @param term A [Term]
     * @return [Term]
     */
    operator fun times(term: Term): Term

    /**
     * Divides this value by the number value.
     * @param number A [Number]
     * @return [Expression]
     */
    operator fun div(number: Number): Term

    /**
     * Divides this value by the term value.
     * @param term A [Term]
     * @return [Expression]
     */
    operator fun div(term: Term): Term

    /**
     * Raises this variable to the power [x].
     *
     * Special cases:
     *   - `b.pow(0.0)` is `1.0`
     *   - `b.pow(1.0) == b`
     *   - `b.pow(NaN)` is `NaN`
     *   - `NaN.pow(x)` is `NaN` for `x != 0.0`
     *   - `b.pow(Inf)` is `NaN` for `abs(b) == 1.0`
     *   - `b.pow(x)` is `NaN` for `b < 0` and `x` is finite and not an integer
     * @return [Term]
     */
    infix fun pow(x: Double): Term

    /**
     * Raises this value to the integer power [n].
     *
     * See the other overload of [pow] for details.
     * @return [Term]
     */
    infix fun pow(n: Int): Term = this.pow(n.toDouble())


//    override fun equals(other: Any?): Boolean
//    override fun hashCode(): Int
    
    companion object {

        /**
         * A constant holding the "not a number" value of Term.
         * */
        val NaN: Term = TermImpl(Double.NaN, setOf())

        /**
         * A constant holding the negative infinity value of Term.
         */
        val NEGATIVE_INFINITY: Term = TermImpl(Double.NEGATIVE_INFINITY, setOf())

        /**
         * A constant holding the positive infinity value of Term.
         */
        val POSITIVE_INFINITY: Term = TermImpl(Double.POSITIVE_INFINITY, setOf())

        /**
         * A constant holding the zero value of Term.
         */
        val ZERO: Term = TermImpl(0.0, setOf())

        /**
         * A constant holding the one value of Term.
         */
        val ONE: Term = TermImpl(1.0, setOf())

        fun termOf(number: Number) : Term = TermImpl(number.toDouble(), setOf())
        fun termOf(number: Number, variables: Set<Variable>) : Term = TermImpl(number.toDouble(), variables)
        fun termOf(number: Number, vararg variables: Variable) : Term = TermImpl(number.toDouble(), setOf(*variables))
        fun termOf(number: Number, base: Number, exponent: Number) : Term = TermImpl(number.toDouble(), setOf(variableOf(base, exponent)))
        fun termOf(number: Number, base: Char, exponent: Char) : Term = TermImpl(number.toDouble(), setOf(variableOf(base, exponent)))
        fun termOf(number: Number, base: Number, exponent: Char) : Term = TermImpl(number.toDouble(), setOf(variableOf(base, exponent)))
        fun termOf(number: Number, base: Char, exponent: Number) : Term = TermImpl(number.toDouble(), setOf(variableOf(base, exponent)))
        fun termOf(base: Number, exponent: Number) : Term = TermImpl(1.0, setOf(variableOf(base, exponent)))
        fun termOf(base: Char, exponent: Char) : Term = TermImpl(1.0, setOf(variableOf(base, exponent)))
        fun termOf(base: Number, exponent: Char) : Term = TermImpl(1.0, setOf(variableOf(base, exponent)))
        fun termOf(base: Char, exponent: Number) : Term = TermImpl(1.0, setOf(variableOf(base, exponent)))
        fun termOf(base: Char) : Term = TermImpl(1.0, setOf(variableOf(base)))
        fun termOf(vararg variables: Variable) : Term = TermImpl(1.0, setOf(*variables))


    }
}