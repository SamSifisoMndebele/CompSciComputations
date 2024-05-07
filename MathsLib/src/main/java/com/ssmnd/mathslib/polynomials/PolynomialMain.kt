package com.ssmnd.mathslib.polynomials

import java.util.Scanner

private fun main() {
    println("""
        A : Quadratic
        B : Cubic
        C : Quartic
    """.trimIndent())
    val option: Char
    val scanner = Scanner(System.`in`)
    option = scanner.next()[0]
    when (option) {
        'A', 'a' -> {
            println("\nGiven a quadratic equation ax^2 + bx + c = 0, enter the values of a, b and c to calculate the roots.")
            print("a = ")
            val a = scanner.nextDouble()
            print("b = ")
            val b = scanner.nextDouble()
            print("c = ")
            val c = scanner.nextDouble()
            println()

            /*val quadratic = Polynomial(a, b, c)
            Polynomial.Companion.printRoots(quadratic)*/

            Quadratic.solveRoots(a, b, c)
        }
        'B', 'b' -> {
            println("\nGiven a cubic equation ax^3 + bx^2 + cx + d = 0, enter the values of a, b, c and d to calculate the roots.")
            print("a = ")
            val a = scanner.nextDouble()
            print("b = ")
            val b = scanner.nextDouble()
            print("c = ")
            val c = scanner.nextDouble()
            print("d = ")
            val d = scanner.nextDouble()
            println()

            /*val cubic = Polynomial(a, b, c, d)
            Polynomial.printRoots(cubic)*/

            Cubic.solveRoots(a, b, c, d)
        }
        'C', 'c' -> {
            println("\nGiven a cubic equation ax^4 + bx^3 + cx^2 + dx + e = 0, enter the values of a, b, c, d and e to calculate the roots.")
            print("a = ")
            val a = scanner.nextDouble()
            print("b = ")
            val b = scanner.nextDouble()
            print("c = ")
            val c = scanner.nextDouble()
            print("d = ")
            val d = scanner.nextDouble()
            print("e = ")
            val e = scanner.nextDouble()
            println()

            /// 2 1,5 -4 5 6
            /// 2 0 -6 4 0
            /// 2 3 -4 5 6
            //val quartic = Polynomial(a, b, c, d, e)
            //println(quartic)

            //Polynomial.Companion.printRoots(quartic)

            Quartic.solveRoots(a, b, c, d, e)
        }
    }
}

/*
class RootFactor(root: Double, factor: Polynomial) {
    val root: Double
    val factor: Polynomial

    init {
        this.coefficient = coefficient
        this.exponent = exponent
        this.root = root
        this.factor = factor
    }
}
class RootsFactor(roots: MutableList<Double>, factor: Polynomial) {
    val roots: MutableList<Double>
    var factor: Polynomial

    init {
        this.coefficient = coefficient
        this.exponent = exponent
        root = root
        this.factor = factor
        this.roots = roots
        this.factor = factor
    }
}
companion object {
    private fun getRoot(polynomial: Polynomial): Double {
        if (polynomial.getValue(0.0) == 0.0) return 0

        val EPSILON = 1E-17
        var iteration: Short = 0
        var x: BigDecimal? = null
        while (iteration < 50) {
            iteration++
            val rand = (Math.random() * 50 - 25).toInt()
            val random = BigDecimal.valueOf(rand.toLong())
            val fx = polynomial.getValue(random)
            var dfx = polynomial.getDerivative(1).getValue(random)
            if (dfx == BigDecimal.ZERO) continue
            iteration = 0
            var subtrahend: BigDecimal?
            try {
                subtrahend = fx.divide(dfx, MathContext.DECIMAL128)
            } catch (e: Exception) {
                if (polynomial.getValue(dfx) == BigDecimal.ZERO) {
                    return dfx.toDouble()
                }
                dfx = BigDecimal.valueOf((Math.random() * 50 + 1).toInt().toLong())
                subtrahend = fx.divide(dfx, MathContext.DECIMAL128)
            }
            x = random.subtract(subtrahend)
            break
        }
        if (x == null) return Double.NaN

        var tempX: BigDecimal?
        do {
            tempX = x
            val fx = polynomial.getValue(tempX!!)
            var dfx = polynomial.getDerivative(1).getValue(tempX)
            if (dfx == BigDecimal.ZERO) {
                tempX = BigDecimal.valueOf((Math.random() * 50 - 25).toInt().toLong())
                continue
            }
            var subtrahend: BigDecimal?
            try {
                subtrahend = fx.divide(dfx, MathContext.DECIMAL128)
            } catch (e: Exception) {
                if (polynomial.getValue(dfx) == BigDecimal.ZERO) {
                    return dfx.toDouble()
                }
                dfx = BigDecimal.valueOf((1 + Math.random() * 20).toInt().toLong())
                subtrahend = fx.divide(dfx, MathContext.DECIMAL128)
            }
            x = tempX.subtract(subtrahend)
            iteration++
        } while (tempX!!.subtract(x).abs().toDouble() > EPSILON && iteration < 500)

        if (tempX.subtract(x).abs().toDouble() > EPSILON && iteration.toInt() == 500) return Double.NaN
        return if (x!!.abs().toDouble() < EPSILON) 0
        else x.toDouble()
    }

    private fun getFactor(quartic: Polynomial, root: Double): Polynomial {
        val polynomial = Polynomial()
        if (root == 0.0) {
            for ((coefficient, exponent) in quartic.getTerms()) {
                if (exponent >= 1) polynomial.addTerm(coefficient, exponent - 1)
            }
        } else {
            val exponents = quartic.exponents
            val coefficients = quartic.coefficients

            for (i in 1 until exponents.size) {
                val exponent = exponents[i]
                var coefficient = 0.0

                var j = 0
                var k = i - 1
                while (j < i) {
                    coefficient += coefficients[j] * root.pow(k.toDouble())
                    j++
                    k--
                }

                if (exponent >= 0) polynomial.addTerm(coefficient, exponent)
            }
        }
        return polynomial
    }

    @Contract("_ -> new")
    fun getRootFactor(polynomial: Polynomial): RootFactor {
        var factor = polynomial
        val root = getRoot(factor)
        if (java.lang.Double.isFinite(root)) {
            factor = getFactor(factor, root)
        }
        return RootFactor(root, factor)
    }

    @Contract("_ -> new")
    fun getRootsFactor(polynomial: Polynomial): RootsFactor {
        val roots: MutableList<Double> = ArrayList()
        var rootFactor = getRootFactor(polynomial)
        while (java.lang.Double.isFinite(rootFactor.root)) {
            roots.add(rootFactor.root)
            rootFactor = getRootFactor(rootFactor.factor)
        }
        return RootsFactor(roots, rootFactor.factor)
    }


    fun getFactorized(polynomial: Polynomial): String {
        val rootsFactor = getRootsFactor(polynomial)
        val roots = rootsFactor.roots

        val string = StringBuilder()
        if (rootsFactor.factor.isNumber) {
            string.append(rootsFactor.factor)
        }

        while (!roots.isEmpty()) {
            var root: Double
            if (roots.contains(0.0)) root = 0.0
            else root = roots.getFirst()

            var count = 0
            while (roots.remove(root)) {
                count++
            }
            if (root == 0.0) {
                string.append("x")
            } else if (root > 0) {
                string.append("(x - ")
                string.append(toFractionString(root))
                string.append(")")
            } else {
                string.append("(x + ")
                string.append(toFractionString(-root))
                string.append(")")
            }

            if (count > 1) string.append("^").append(count)
        }

        if (!rootsFactor.factor.isNumber) {
            string.append("(").append(rootsFactor.factor).append(")")
        }

        return string.toString()
    }

    fun printFactorized(polynomial: Polynomial) {
        println(getFactorized(polynomial))
    }

    fun printRoots(polynomial: Polynomial) {
        println("-------------------------------------------------------------------------------")
        val string = StringBuilder()
        val rootsFactor = getRootsFactor(polynomial)
        string.append(polynomial).append(" = 0\n")
        var rootsCount = 0
        if (!rootsFactor.roots.isEmpty()) {
            string.append("Since, ").append(polynomial).append(" = ")
            string.append(getFactorized(polynomial)).append(" = 0\n")
            string.append("Real roots :-\n")

            for (i in rootsFactor.roots.indices) {
                val root = rootsFactor.roots[i]
                string.append("x").append(++rootsCount).append(" = ")
                    .append(toFractionString(root)).append("\n")
            }
            if (!rootsFactor.factor.isNumber) string.append("\n").append(rootsFactor.factor).append(" = 0")
        }

        println(string)

        if (rootsFactor.factor.degree == 3) {
            val coefficients = rootsFactor.factor.coefficients
            Cubic.solveRoots(coefficients[0], coefficients[1], coefficients[2], coefficients[3])
        } else if (rootsFactor.factor.degree == 2) {
            val coefficients = rootsFactor.factor.coefficients
            val a = coefficients[0]
            val b = coefficients[1]
            val c = coefficients[2]
            val delta = (b * b - 4 * a * c) / (a * a)
            Quadratic.solveImmRoots(delta, a, b, "x" + (++rootsCount), "x" + (++rootsCount))
        }

        println("-------------------------------------------------------------------------------")
    }
}*/
