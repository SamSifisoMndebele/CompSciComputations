package com.ssmnd.mathslib.expressions.variable

import com.ssmnd.mathslib.expressions.term.Term
import com.ssmnd.mathslib.expressions.term.Term.Companion.termOf
import com.ssmnd.mathslib.expressions.variable.Variable.Companion.VariableType

class VariableImpl(
    override val base: Any,
    override val exponent: Any,
    override val variableType: VariableType
) : Variable {
    override fun toString(): String {
        if (baseDouble == 0.0 && exponentDouble == 0.0) return "NaN"
        if (baseDouble == 0.0) return "0"
        if (exponentDouble == 0.0 || baseDouble == 1.0) return "1"
        if (exponentDouble == 1.0) return "$base"
        return "$base^$exponent"
    }

    override fun compareTo(other: Variable): Int {
        /*return when(variableType) {
            VariableType.NUMBER -> TODO()
            VariableType.POLYNOMIAL -> TODO()
            VariableType.EXPONENTIAL -> TODO()
            VariableType.NATURAL -> TODO()
        }*/
        return other.toString().compareTo(toString())
    }

    override fun times(variable: Variable): Variable {
        when(variableType) {
            VariableType.NUMBER -> TODO()
            VariableType.POLYNOMIAL -> TODO()
            VariableType.EXPONENTIAL -> TODO()
            VariableType.NATURAL -> TODO()
        }
    }

    override fun div(variable: Variable): Variable {
        when(variableType) {
            VariableType.NUMBER -> TODO()
            VariableType.POLYNOMIAL -> TODO()
            VariableType.EXPONENTIAL -> TODO()
            VariableType.NATURAL -> TODO()
        }
    }

    override fun pow(x: Double): Variable {
        when(variableType) {
            VariableType.NUMBER -> TODO()
            VariableType.POLYNOMIAL -> TODO()
            VariableType.EXPONENTIAL -> TODO()
            VariableType.NATURAL -> TODO()
        }
    }

    override fun derivative(n: Int): Term {
        when(variableType) {
            VariableType.NUMBER -> TODO()
            VariableType.POLYNOMIAL -> {
                var coefficient = 1.0
                for (i in 0 until n) {
                    coefficient *= exponentDouble - i
                    if (coefficient == 0.0) return termOf(0.0, this.base.toString()[0], 0.0)
                }
                return termOf(coefficient, this.base.toString()[0], exponentDouble - n)
            }
            VariableType.EXPONENTIAL -> TODO()
            VariableType.NATURAL -> TODO()
        }
    }
}