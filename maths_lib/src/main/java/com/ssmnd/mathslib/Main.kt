package com.ssmnd.mathslib

import com.ssmnd.mathslib.expressions.term.TermImpl
import com.ssmnd.mathslib.expressions.variable.Variable.Companion.variableOf
import com.ssmnd.mathslib.utils.Latex


private fun main() {

//    val v1 = PolynomialVariable('x')
//    val v2 = PolynomialVariable('x', 5)
//    val e1 = ExponentialVariable(2,'x')
//    val e2 = ExponentialVariable(3,'x')

//    println(v1.derivative(2).toString())
//    println(v2.derivative(2).toString())
//    println(e1.derivative(1).toString())
//    println(e2.derivative(2).toString())


    val variableNum = variableOf(3,2)
    val variableNum2 = variableOf(5)
    val variablePol = variableOf('x',2)
    val variablePol2 = variableOf('y')
    val variableExp = variableOf(3,'x')
    val variable = variableOf('x','y')

    val term = TermImpl(-2.0, setOf(variableNum, variableNum2, variableExp, variablePol, variablePol2, variable))

//    println(variableNum)
//    println(variableNum2)
//    println(variablePol)
//    println(variablePol2)
//    println(variableExp)
//    println(variable)
//    println()
//    println(term.toLatex())

    println(variable.value(2, 3))

    println(term.toLatex().pow(Latex.THETA, false))

    println(Latex.partialDer(term.toLatex(), 3))

}