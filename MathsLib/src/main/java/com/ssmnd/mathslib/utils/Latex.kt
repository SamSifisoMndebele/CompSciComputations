package com.ssmnd.mathslib.utils

import com.ssmnd.mathslib.expressions.term.Term
import com.ssmnd.mathslib.expressions.variable.Variable

/**
 * The `Latex` class represents character strings. All latex literals, such as `"frac{a}{b}"`, are
 * implemented as instances of this class.
 */
class Latex : CharSequence {
    private val string: String
    override val length: Int get() = string.length
    private constructor() {
        string = ""
    }
    constructor(latex: Latex) {
        this.string = latex.string
    }
    constructor(latexString: String) {
        this.string = latexString.removePrefix("$").removeSuffix("$")
    }
    constructor(number: Number, parenthesis: Boolean = false) {
        var fraction = number.toFraction()
        val isNegative = number.toDouble() < 0
        if (isNegative) fraction = fraction.negate
        val latex = StringBuilder()
        if (isNegative) latex.append('-')
        if (fraction.denominator == 1L) {
            latex.append(fraction.numerator)
        } else {
            if (parenthesis) latex.append('(')
            latex.append("frac{")
                .append(fraction.numerator)
                .append("}{")
                .append(fraction.denominator)
                .append("}")
            if (parenthesis) latex.append(')')
        }
        string = latex.toString()
    }
    constructor(term: Term) {
        var latex = Latex()
        if (term.coefficient == 0.0) {
            string = "0"
            return
        } else if (term.coefficient > 0) {
            if (term.coefficient == 1.0 && term.variables.isEmpty()) {
                string = "1"
                return
            }
            if (term.coefficient != 1.0) latex += Latex(term.coefficient)
        } else {
            if (term.coefficient == -1.0 && term.variables.isEmpty()) {
                string = "-1"
                return
            } else if (term.coefficient == -1.0) {
                latex += '-'
            }
            if (term.coefficient != -1.0) latex += Latex(term.coefficient)
        }
        term.variables.forEach {
            latex += it.toLatex()
        }
        string = latex.string
    }
    constructor(variable: Variable) {
        string = when {
            variable.baseDouble == 0.0 && variable.exponentDouble == 0.0 -> NAN_STR
            variable.baseDouble == 0.0 -> "0"
            variable.exponentDouble == 0.0 || variable.baseDouble == 1.0 -> "1"
            variable.exponentDouble == 1.0 -> if (variable.base is Char) variable.base.toString() else Latex(variable.base.toString().toDouble()).string
            else -> "{${
                if (variable.base is Char) variable.base.toString() else Latex(variable.base.toString().toDouble()).string
            }}^{${
                if (variable.exponent is Char) variable.exponent.toString() else Latex(variable.exponent.toString().toDouble()).string
            }}"
        }
    }

    /**
     * Returns a latex string obtained by concatenating this latex string with the latex string representation of the given [other] object.
     */
    operator fun plus(other: Any?): Latex {
        if (this === other) return Latex(string+string)
        if (other == null) return this
        return when (other) {
            is Latex -> Latex(string + other.string)
            is String -> Latex(string+other)
            is Char -> Latex(string+other)
            else -> Latex(string+other.toString())
        }
    }

    override fun toString(): String {
        return "$$string$"
    }
    override fun get(index: Int): Char {
        return string[index]
    }
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return string.subSequence(startIndex, endIndex)
    }


    fun pow(n: Latex, parenthesis: Boolean = true) : Latex {
        return Latex("${if (parenthesis) "(" else ""}{${this.string}}${if (parenthesis) ")" else ""}^{${n.string}}")
    }

    companion object {
        private val alignment = """
\begin{align*}
y &= x^2-1\\ 
y &= (x-1)(x+1)\\ 
\because y &= 0\\
0 &= (x-1)(x+1)\\
\therefore x &= 1\\
 x &= -1 
\end{align*}
    """

        const val NAN_STR = "NaN"
        const val P_INF_STR = "+\\infty"
        const val N_INF_STR = "-\\infty"
        const val PLUS_MINUS_STR = "\\pm"
        const val MINUS_PLUS_STR = "\\mp"
        const val TIMES_STR = "\\times"
        const val ASTERIC_STR = "\\ast"
        const val DIVIDE_STR = "\\div"
        const val DOT_STR = "\\cdot"
        const val THEREFORE_STR = "\\therefore"
        const val BECAUSE_STR = "\\because"
        const val HOR_DOTS_STR = "\\cdots"
        const val VERT_DOTS_STR = "\\vdots"
        const val DIAG_DOTS_STR = "\\ddots"
        const val EXISTS_STR = "\\exists"
        const val FOR_ALL_STR = "\\forall"
        const val PRIME_SET_STR = "\\mathbb{P}"
        const val NATURAL_SET_STR = "\\mathbb{N}"
        const val INTERGERS_SET_STR = "\\mathbb{Z}"
        const val IRRATIONAL_SET_STR = "\\mathbb{I}"
        const val RATIONAL_SET_STR = "\\mathbb{Q}"
        const val REAL_SET_STR = "\\mathbb{R}"
        const val COMPLEX_SET_STR = "\\mathbb{C}"
        const val EPSILON_STR = "\\epsilon"
        const val THETA_STR = "\\theta"
        const val PI_STR = "\\pi"
        const val NOT_EQL_STR = "\\neq"
        const val LESS_EQL_STR = "\\leq"
        const val GREATER_EQL_STR = "\\geq"
        const val LESS_EQL_STANT_STR = "\\leqslant"
        const val GREATER_EQL_STANT_STR = "\\geqslant"
        const val EQUIVALENT_STR = "\\equiv"
        const val NOT_EQUIVALENT_STR = "\\not\\equiv"
        const val SIM_STR = "\\sim"
        const val SIM_EQL_STR = "\\simeq"
        const val APPROXIMATLY_STR = "\\approx"
        const val PROPOTIONAL_STR = "\\propto"
        const val DELTA_STR = "\\Delta"

        val ZERO = Latex("0")
        val ONE = Latex("1")
        val NEGATIVE_ONE = Latex("-1")
        val PLUS = Latex("+")
        val MINUS = Latex("-")
        val EQL = Latex("=")
        val NaN = Latex(NAN_STR)
        val POSITIVE_INFINITY = Latex(P_INF_STR)
        val NEGATIVE_INFINITY = Latex(N_INF_STR)
        val PLUS_MINUS = Latex(PLUS_MINUS_STR)
        val MINUS_PLUS = Latex(MINUS_PLUS_STR)
        val TIMES = Latex(TIMES_STR)
        val ASTERIC = Latex(ASTERIC_STR)
        val DIVIDE = Latex(DIVIDE_STR)
        val DOT = Latex(DOT_STR)
        val THEREFORE = Latex(THEREFORE_STR)
        val BECAUSE = Latex(BECAUSE_STR)
        val HOR_DOTS = Latex(HOR_DOTS_STR)
        val VERT_DOTS = Latex(VERT_DOTS_STR)
        val DIAG_DOTS = Latex(DIAG_DOTS_STR)
        val EXISTS = Latex(EXISTS_STR)
        val FOR_ALL = Latex(FOR_ALL_STR)
        val PRIME_SET = Latex(PRIME_SET_STR)
        val NATURAL_SET = Latex(NATURAL_SET_STR)
        val INTERGERS_SET = Latex(INTERGERS_SET_STR)
        val IRRATIONAL_SET = Latex(IRRATIONAL_SET_STR)
        val RATIONAL_SET = Latex(RATIONAL_SET_STR)
        val REAL_SET = Latex(REAL_SET_STR)
        val COMPLEX_SET = Latex(COMPLEX_SET_STR)
        val EPSILON = Latex(EPSILON_STR)
        val THETA = Latex(THETA_STR)
        val PI = Latex(PI_STR)
        val NOT_EQL = Latex(NOT_EQL_STR)
        val LESS_EQL = Latex(LESS_EQL_STR)
        val GREATER_EQL = Latex(GREATER_EQL_STR)
        val LESS_EQL_STANT = Latex(LESS_EQL_STANT_STR)
        val GREATER_EQL_STANT = Latex(GREATER_EQL_STANT_STR)
        val EQUIVALENT = Latex(EQUIVALENT_STR)
        val NOT_EQUIVALENT = Latex(NOT_EQUIVALENT_STR)
        val SIM = Latex(SIM_STR)
        val SIM_EQL = Latex(SIM_EQL_STR)
        val APPROXIMATLY = Latex(APPROXIMATLY_STR)
        val PROPOTIONAL = Latex(PROPOTIONAL_STR)
        val DELTA = Latex(DELTA_STR)

        fun fraction(numerator: Latex, denominator: Latex) : Latex {
            return Latex("\\frac{${numerator.string}}{${denominator.string}}")
        }
        fun fraction(number: Latex, numerator: Latex, denominator: Latex) : Latex {
            return Latex("{${number.string}}\\tfrac{${numerator.string}}{${denominator.string}}")
        }
        fun sqrt(number: Latex, /*@Range(min=1, max=Short.MAX_VALUE)*/ n: Int = 2) : Latex {
            if (n == 2) return Latex("\\sqrt{${number.string}}")
            return Latex("\\sqrt[${n}]{${number.string}}")
        }
        fun pow(x: Latex, n: Int, parenthesis: Boolean = true) : Latex {
            return Latex("${if (parenthesis) "(" else ""}{${x.string}}${if (parenthesis) ")" else ""}^{$n}")
        }
        fun pow(x: Latex, n: Latex, parenthesis: Boolean = true) : Latex {
            return Latex("${if (parenthesis) "(" else ""}{${x.string}}${if (parenthesis) ")" else ""}^{${n.string}}")
        }
        fun partialDer(expression: Latex, degree: Int = 1, respectTo: Char = 'x', parenthesis: Boolean = true) : Latex {
            if (degree == 1) return Latex("\\frac{\\partial }{\\partial $respectTo}${if (parenthesis) "(" else ""}{${expression.string}}${if (parenthesis) ")" else ""}")
            return Latex("\\frac{\\partial^{$degree} }{\\partial $respectTo^{$degree}}${if (parenthesis) "(" else ""}{${expression.string}}${if (parenthesis) ")" else ""}")
        }
        fun derivative(expression: Latex, degree: Int = 1, respectTo: Char = 'x', parenthesis: Boolean = true) : Latex {
            if (degree == 1) return Latex("\\frac{\\mathrm{d} }{\\mathrm{d} $respectTo}${if (parenthesis) "(" else ""}{${expression.string}}${if (parenthesis) ")" else ""}")
            return Latex("\\frac{\\mathrm{d}^{$degree} }{\\mathrm{d} $respectTo^{$degree}}${if (parenthesis) "(" else ""}{${expression.string}}${if (parenthesis) ")" else ""}")
        }
        fun bar(char: Char) : Latex {
            return Latex("\\bar{$char}")
        }
    }
}