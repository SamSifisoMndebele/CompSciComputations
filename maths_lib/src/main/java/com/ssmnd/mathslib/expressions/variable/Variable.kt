package com.ssmnd.mathslib.expressions.variable

import com.ssmnd.mathslib.expressions.term.Term
import com.ssmnd.mathslib.utils.Latex
import kotlin.math.pow

/**
 * The `Variable` class represents a mathematical variable with a character base_convert and an exponent.
 * All mathematical variables, such as `x, y^2, x^y or 2^n`, are
 * implemented as instances of this class.
 */
interface Variable : Comparable<Variable> {
    /**
     * The base_convert of Variable
     */
    val base: Any //Char or Double
    val baseDouble: Double
        get() = try {
            base.toString().toDouble()
        } catch (_: NumberFormatException) {
            Double.NaN
        }
    /**
     * The exponent of Variable
     */
    val exponent: Any //Char or Double
    val exponentDouble: Double
        get() = try {
            exponent.toString().toDouble()
        } catch (_: NumberFormatException) {
            Double.NaN
        }

    
    /**
     * The type of Variable
     */
    val variableType: VariableType
    
    /**
     * String representation of Variable.
     * @return [String]
     */
    override fun toString(): String

    //val comparableString: String
    //override fun compareTo(other: Variable): Int = comparableString.compareTo(other.comparableString)

    /**
     * Latex representation of Variable.
     * @return [Latex]
     */
    fun toLatex(): Latex = Latex(this)

    /**
     * Returns the value of this variable if is Number.
     * @return [Double]
     * @throws IllegalStateException if this Variable is not a Number.
     */
    @Throws(IllegalStateException::class)
    fun value(): Double {
        if (variableType == VariableType.NATURAL)
            throw IllegalArgumentException("The variable $this is not a Number, Polynomial or Exponential, replace with value(x: Number, y: Number)")
        if (variableType == VariableType.POLYNOMIAL || variableType == VariableType.EXPONENTIAL)
            throw IllegalArgumentException("The variable $this is not a Number, replace with value(x: Number)")
        return baseDouble.pow(exponentDouble)
    }
    /**
     * Returns the value of this variable if it is Polynomial or Exponential.
     * @param x base_convert or exponent value.
     * @return [Double]
     * @throws IllegalStateException if this Variable is not a Polynomial or Exponential.
     */
    @Throws(IllegalStateException::class)
    infix fun value(x: Number): Double {
        if (variableType == VariableType.NATURAL)
            throw IllegalArgumentException("The variable $this is not a Number, Polynomial or Exponential, replace with value(x: Number, y: Number)")
        if (variableType == VariableType.POLYNOMIAL)
            return x.toDouble().pow(exponentDouble)
        if (variableType == VariableType.EXPONENTIAL)
            return baseDouble.pow(x.toDouble())
        return baseDouble.pow(exponentDouble)
    }

    /**
     * Returns the value of this variable.
     * @param b base_convert value
     * @param e exponent value.
     * @return [Double]
     */
    fun value(b: Number, e: Number): Double {
        if (variableType == VariableType.NATURAL)
            return b.toDouble().pow(e.toDouble())
        if (variableType == VariableType.POLYNOMIAL)
            return b.toDouble().pow(exponentDouble)
        if (variableType == VariableType.EXPONENTIAL)
            return baseDouble.pow(e.toDouble())
        return baseDouble.pow(exponentDouble)
    }

    /**
     * Returns the positive of this value.
     * @return [Variable]
     */
    operator fun unaryPlus(): Variable = this

    /**
     * Multiplies this variable by the other variable.
     * @param variable other [Variable]
     * @return [Variable]
     * @throws [ArithmeticException] If this variable is not the same as other variable
     */
    @Throws(ArithmeticException::class)
    operator fun times(variable: Variable): Variable

    /**
     * Divides this variable by the other variable.
     * @param variable other [Variable]
     * @return [Variable]
     * @throws [ArithmeticException] If this variable is not the same as other variable
     */
    @Throws(ArithmeticException::class)
    operator fun div(variable: Variable): Variable

    /**
     * Checks if the variable is a Number without variables.
     * @return [Boolean]
     */
    val isNumber get() = try {
        base.toString().toDouble()
        exponent.toString().toDouble()
        true
    } catch (_: NumberFormatException) {
        false
    }

    /**
     * Checks if the variable is equals to One.
     * @return [Boolean]
     */
    val isOne get() = variableType == VariableType.NUMBER &&
            (base.toString().toDouble() == 1.0 ||
                    exponent.toString().toDouble() == 0.0 && base.toString().toDouble() != 0.0)

    /**
     * Checks if the variable is equals to Zero.
     * @return [Boolean]
     */
    val isZero get() = variableType == VariableType.NUMBER &&
            base.toString().toDouble() == 0.0 && exponent.toString().toDouble() != 0.0

    /**
     * Checks if the term is Not a Number.
     * @param variables The pair of variables values, all variables values are 1.0 by default.
     * @return [Boolean]
     */
    val isNaN get() = try {
        base.toString().toDouble() == 0.0 && exponent.toString().toDouble() == 0.0
    } catch (_: NumberFormatException) {
        false
    }

    /**
     * Raises this variable to the power [x].
     *
     * Special cases:
     *   - `b.pow(0.0)` is `1.0`
     *   - `b.pow(1.0)` is `b`
     *   - `b.pow(NaN)` is `NaN`
     *   - `NaN.pow(x)` is `NaN` for `x != 0.0`
     *   - `b.pow(Inf)` is `NaN` for `abs(b) == 1.0`
     *   - `b.pow(x)` is `NaN` for `b < 0` and `x` is finite and not an integer
     * @return [Variable]
     */
    infix fun pow(x: Double): Variable

    /**
     * Raises this value to the integer power [n].
     *
     * See the other overload of [pow] for details.
     * @return [Variable]
     */
    infix fun pow(n: Int): Variable = this.pow(n.toDouble())

    /**
     * Returns the derivative of this variable.
     * @param n the degree if the derivative. `e.g n = 1 for first derivative, n = 2 for second derivative.`
     * @return [Term]
     */
    infix fun derivative(n: Int): Term


//    override fun equals(other: Any?): Boolean
//    override fun hashCode(): Int

    companion object {
        /**
         * The `Number Variable` class represents a mathematical variable with a base_convert and an exponent.
         * All mathematical variables, such as `2^(-2), 3^2`, are
         * implemented as instances of this class.
         */
        fun variableOf(base: Number, exponent: Number) : Variable =
            VariableImpl(base.toDouble().pow(exponent.toDouble()), 1.0, VariableType.NUMBER)

        /**
         * The `Number Variable` class represents a mathematical variable with a base_convert and an exponent of 1.
         * All mathematical variables, such as `2, -3`, are
         * implemented as instances of this class.
         */
        fun variableOf(number: Number) : Variable =
            VariableImpl(number.toDouble(), 1.0, VariableType.NUMBER)
        /**
         * The `Variable` class represents a mathematical variable with a character base_convert and a character exponent.
         * All mathematical variables, such as `x^y`, are
         * implemented as instances of this class.
         */
        fun variableOf(base: Char, exponent: Char) : Variable =
            VariableImpl(base, exponent, VariableType.NATURAL)

        /**
         * The `Polynomial Variable` class represents a mathematical variable with a character base_convert and an exponent.
         * All mathematical variables, such as `y^2, x^1.5`, are
         * implemented as instances of this class.
         */
        fun variableOf(base: Char, exponent: Number) : Variable =
            VariableImpl(base, exponent.toDouble(), VariableType.POLYNOMIAL)
        /**
         * The `Polynomial Variable` class represents a mathematical variable with a character base_convert and an exponent of 1.
         * All mathematical variables, such as `x, y`, are
         * implemented as instances of this class.
         */
        fun variableOf(base: Char) : Variable =
            VariableImpl(base, 1.0, VariableType.POLYNOMIAL)
        /**
         * The `Exponential Variable` class represents a mathematical variable with a base_convert and a character exponent.
         * All mathematical variables, such as `3^y or 2^n`, are
         * implemented as instances of this class.
         */
        fun variableOf(base: Number, exponent: Char) : Variable =
            VariableImpl(base.toDouble(), exponent, VariableType.EXPONENTIAL)
        /**
         * A constant holding the "not a number" value of Variable.
         * */
        val NaN = VariableImpl(0, 0, VariableType.NUMBER)

        /**
         * A constant holding the 0 value of Variable.
         * */
        val ZERO = VariableImpl(0, 1, VariableType.NUMBER)

        /**
         * A constant holding the 1 value of Variable.
         * */
        val ONE = VariableImpl(1, 1, VariableType.NUMBER)
        
        enum class VariableType {
            NUMBER, POLYNOMIAL, EXPONENTIAL, NATURAL
        }
    }
}