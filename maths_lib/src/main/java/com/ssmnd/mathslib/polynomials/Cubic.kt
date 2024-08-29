package com.ssmnd.mathslib.polynomials

import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cbrt
import kotlin.math.cos
import kotlin.math.sqrt

object Cubic {
    @JvmStatic
    fun main(args: Array<String>) {

    }

    @JvmStatic
    fun solveRoots(a: Double, b: Double, c: Double, d: Double) {
        val delta = b * b - 3 * a * c
        if (delta == 0.0) {
            val x = (-b + cbrt(b * b * b * 27 * a * a * d)) / (3 * a)

            println("Real roots:-")
            println("x1 = $x")

            val newB = a * x + b
            val newC = a * x * x + b * x + c

            //Solve equation ax^2 + newBx + newC = 0 for non-real roots
            val newDelta = (newB * newB - 4 * a * newC) / (a * a)
            Quadratic.solveImmRoots(newDelta, a, newB, "x2", "x3")
        } else {
            val k = (9 * a * b * c - 2 * b * b * b - 27 * a * a * d) / (2 * sqrt(abs(delta * delta * delta)))

            if (delta < 0) {
                val A = sqrt(abs(delta)) / (3 * a)
                val B = cbrt(k + sqrt(k * k + 1)) + cbrt(k - sqrt(k * k + 1))
                val x = A * B - b / (3 * a)

                println("Real roots:-")
                println("x = $x")

                val newB = a * x + b
                val newC = a * x * x + b * x + c
                //Solve equation ax^2 + newBx + newC = 0 for non-real roots
                val newDelta = (newB * newB - 4 * a * newC) / (a * a)
                Quadratic.solveImmRoots(newDelta, a, newB)
            } else if (abs(k) > 1) {
                val A = sqrt(delta) * abs(k) / (3 * a * k)
                val B = cbrt(abs(k) + sqrt(k * k - 1)) + cbrt(abs(k) - sqrt(k * k - 1))
                val x = A * B - b / (3 * a)

                println("Real roots:-")
                println("x = $x")

                val newB = a * x + b
                val newC = a * x * x + b * x + c
                //Solve equation ax^2 + newBx + newC = 0 for non-real roots
                val newDelta = (newB * newB - 4 * a * newC) / (a * a)
                Quadratic.solveImmRoots(newDelta, a, newB)
            } else {
                val A = acos(k) / 3
                val B = acos(k) / 3 - 2 * Math.PI / 3
                val C = acos(k) / 3 + 2 * Math.PI / 3

                val x1 = (2 * sqrt(delta) * cos(A) - b) / (3 * a)
                val x2 = (2 * sqrt(delta) * cos(B) - b) / (3 * a)
                val x3 = (2 * sqrt(delta) * cos(C) - b) / (3 * a)

                println("Real roots:-")
                println("x1 = $x1")
                println("x2 = $x2")
                println("x3 = $x3")
            }
        }
    }
}