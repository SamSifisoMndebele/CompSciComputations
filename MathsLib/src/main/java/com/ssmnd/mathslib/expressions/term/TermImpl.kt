package com.ssmnd.mathslib.expressions.term

import com.ssmnd.mathslib.expressions.variable.Variable
import com.ssmnd.mathslib.utils.Latex
import java.util.TreeSet

class TermImpl(coefficient: Double, variables: Set<Variable>) : Term {
    override val coefficient: Double
    override val variables: TreeSet<Variable>
    init {
        var number = coefficient
        val vars = mutableSetOf<Variable>()
        for (variable in variables) {
            if (variable.isNumber) number *= variable.value()
            else vars.add(variable)
        }
        this.coefficient = number
        this.variables = TreeSet(vars)
    }

    override fun toString(): String {
        return coefficient.toString() + '•' + variables.joinToString("•")
    }

    override val comparableString: String
        get() = TODO("Not yet implemented")

    override fun toLatex(): Latex = Latex(this)

    override fun value(vararg variables: Pair<Char, Number>): Double {
        var value = coefficient
        val varsMap = mapOf(*variables).mapValues { it.value.toDouble() }
        this.variables.forEach { variable ->
            value *= variable.value(varsMap[variable.base] ?: 0.0)
        }
        return value
    }

    override val isNumber: Boolean
        get() = variables.all { it.isNumber }
    override val isOne: Boolean
        get() = isNumber && coefficient == 1.0
    override val isZero: Boolean
        get() = isNumber && coefficient == 0.0

    override fun isLikeTerm(other: Any): Boolean {
        TODO("Not yet implemented")
    }

    /*private infix fun Map.Entry<Char, Double>.derivative(n: Int) : Triple<Double, Char, Double> {
        var coefficient = 1.0
        for (i in 0 until n) {
            coefficient *= value - i
        }
        val exponent = value - n
        return Triple(coefficient, key, exponent)
    }
    private infix fun partial(base: Char) : PolynomialTerm{
        val exp = variables[base]!!
        return PolynomialTerm(coefficient * exp, variables.also { it[base] = exp - 1 })
    }
    override fun derivative(): Expression {
        var derivative : Expression = ExpressionImpl.ZERO

        for (base in variables.keys) {
            derivative += this partial base
        }
        return derivative
    }*/

    override fun partialDerivative(x: Char, n: Int): Term {
        /*var coefficient = this.coefficient
        var removeVar : Variable
        val removed = variables.removeIf {
            removeVar = it
            when(it) {
                is PVar -> variable == it.base
                is EVar -> variable == it.exponent
                else -> false
            }
        }
        if (removed) {

        }
        variables.forEach {
            when(it) {
                is PVar -> {
                    val exp = it.exponent
                }
                is EVar -> {
                    val base = it.base

                }
            }
        }*/
        TODO("Not yet implemented")
    }

    override fun plus(term: Term): Term {
        //TODO("Not yet implemented")

        /*val sumTerms = mutableSetOf<Term>()
        val iterator = expression.terms.iterator()
        var hasLikeTerm = false
        while (iterator.hasNext()) {
            val term = iterator.next()
            try {
                sumTerms.add(plusLike(term))
                hasLikeTerm = true
                break
            } catch (_:Exception) {
                sumTerms.add(term)
            }
        }
        if (hasLikeTerm) {
            iterator.forEachRemaining { term -> sumTerms.add(term) }
        } else sumTerms.add(this)*/
        if (/*term !is TermImpl || */this.variables != term.variables)
            throw ArithmeticException("$this is not a like term of $term")

        return TermImpl(this.coefficient + term.coefficient, this.variables)
    }

    /*override fun plus(expression: Expression): Expression {
        if (expression.isNaN() || isNaN()) return ExpressionImpl.NaN
        if (expression.isInfinite() || isInfinite())
            return if (expression.isPositive() && isPositive() || expression.isNegative() && isNegative()) ExpressionImpl.POSITIVE_INFINITY
            else ExpressionImpl.NEGATIVE_INFINITY
        if (expression.isZero) return ExpressionImpl(this)
        if (this.isZero) return expression

        val sumTerms = mutableSetOf<Term>()
        val iterator = expression.terms.iterator()
        var hasLikeTerm = false
        while (iterator.hasNext()) {
            val term = iterator.next()
            try {
                sumTerms.add(plusLike(term))
                hasLikeTerm = true
                break
            } catch (_:Exception) {
                sumTerms.add(term)
            }
        }
        if (hasLikeTerm) {
            iterator.forEachRemaining { term -> sumTerms.add(term) }
        } else sumTerms.add(this)

    }*/

    override fun times(number: Number): Term {
        return TermImpl(coefficient * number.toDouble(), variables)
    }

    override fun times(term: Term): Term {
        /*if (term is PolynomialTerm) {
            val productVars = variables.toMutableMap()
            productVars.putAll(term.variables)
            val commonVars = variables.keys
            commonVars.retainAll(term.variables.keys)
            for (variable in commonVars) {
                val exp = variables[variable]!! + term.variables[variable]!!
                if (exp == 0.0) productVars.remove(variable) else productVars[variable] = exp
            }
            return PolynomialTerm(coefficient, productVars)
        }

        return ProductTerm(coefficient,this.also { it.coefficient = 0.0 }, term.also { it.coefficient = 1.0 })*/
        TODO("Not yet implemented")
    }

    /*@Throws(Exception::class)
    private fun divideByHCT(term: PolynomialTerm): PolynomialTerm {
        val coefficient: Double = coefficient / term.coefficient
        return if (coefficient == 0.0 || java.lang.Double.isInfinite(coefficient) || java.lang.Double.isNaN(
                coefficient
            )
        ) {
            PolynomialTerm(coefficient)
        } else {
            val newVars = TreeMap<Char, Double>()
            val varsKeys: MutableSet<Char> = term.variables.keys
            for ((variable, exponent): Map.Entry<Char, Double> in variables)  {
                val exponent2: Double = term.variables.get(variable)
                if (exponent2 == null) {
                    newVars[variable] = exponent
                } else {
                    varsKeys.remove(variable)
                    val newExponent = exponent - exponent2
                    if (newExponent >= 0) {
                        newVars[variable] = newExponent
                    } else {
                        throw Exception("Negative exponent")
                    }
                }
            }
            for (key in varsKeys) {
                val exponent: Double = term.variables.get(key)
                if (exponent != null && exponent <= 0) {
                    newVars[key] = exponent * -1
                } else {
                    throw Exception("Negative exponent")
                }
            }
            PolynomialTerm(coefficient, newVars)
        }
    }*/

    override fun div(number: Number): Term {
        return TermImpl(coefficient / number.toDouble(), variables)
    }

    override fun div(term: Term): Term {
        /*val resultVars: MutableMap<Char, Double> = LinkedHashMap<Char, Double>(variables)
        val commonVars: MutableSet<Char> = LinkedHashSet<Char>(variables.keys)
        val termVars: MutableSet<Char> = LinkedHashSet<Char>(term.variables.keys)
        commonVars.retainAll(termVars)
        termVars.removeAll(commonVars)
        for (variable in commonVars) {
            val expA: Double = variables.get(variable)!!
            val expB: Double = term.variables.get(variable)!!
            val newExponent = expA!! - expB!!
            if (newExponent != 0.0) resultVars[variable] = newExponent
        }
        for (variable in termVars) {
            val exponent: Double = term.variables.get(variable)!!
            resultVars[variable] = exponent!! * -1
        }
        PolynomialTerm(coefficient, resultVars)*/

        /*if (term is PolynomialTerm) {
            val productVars = variables.toMutableMap()
            productVars.putAll(term.variables)
            val commonVars = variables.keys
            commonVars.retainAll(term.variables.keys)
            for (variable in commonVars) {
                val exp = variables[variable]!! + term.variables[variable]!!
                if (exp == 0.0) productVars.remove(variable) else productVars[variable] = exp
            }
            return PolynomialTerm(coefficient, productVars)
        } */
        TODO("Not yet implemented")
    }

    override fun pow(x: Double): Term {
        TODO("Not yet implemented")
    }


}