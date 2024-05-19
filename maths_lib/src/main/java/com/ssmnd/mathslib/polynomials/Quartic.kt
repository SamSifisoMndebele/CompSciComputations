package com.ssmnd.mathslib.polynomials

import kotlin.math.pow

object Quartic {

    fun solveRoots(a: Double, b: Double, c: Double, d: Double, e: Double) {
        val delta1 = 3 * b * b - 8 * a * c
        val delta2 = b * b * b - 16 * a * a * d
        if (delta1 == 0.0 && delta2 == 0.0) {
            val root: Double = (b * b * b * b - 256 * a * a * a * e).pow(0.25)
            val x1 = (-b + root) / (4 * a)
            val x2 = (-b - root) / (4 * a)

            println("Real roots:-")
            println("x1 = $x1")
            println("x2 = $x2")

            val newB = a * (x1 + x2) + b
            val newC = a * (x1 * x1 + x1 * x2 + x2 * x2) + b * (x1 + x2) + c

            //Solve equation ax^2 + newBx + newC = 0 for non-real roots
            val newDelta = (newB * newB - 4 * a * newC) / (a * a)
            Quadratic.solveImmRoots(newDelta, a, newB, "x3", "x4")
        }
    }
}
