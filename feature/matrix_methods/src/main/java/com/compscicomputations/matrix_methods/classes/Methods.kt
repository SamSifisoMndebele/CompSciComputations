package com.compscicomputations.matrix_methods.classes

import com.compscicomputations.matrix_methods.classes.Matrix.determinant
import com.compscicomputations.matrix_methods.classes.Matrix.takeForDet
import com.compscicomputations.matrix_methods.classes.Matrix.toExpressionMatrix
import com.compscicomputations.matrix_methods.classes.Matrix.transpose
import com.compscicomputations.matrix_methods.utils.Constants.xVariables
import com.compscicomputations.matrix_methods.utils.Utils.toFractionNumber

object Methods {

    /*fun LinearLayout.salveByGaussJordanElimination(matrixA: Array<DoubleArray>, matrixB: Array<DoubleArray>) {
        printText("Gauss-Jordan Elimination Method",16)
        space()
        printEquations(matrixA, matrixB)
        printMatrixAB(matrixA, matrixB)
        printEliminationMatrix(matrixA, matrixB, null, """(A|B) = """)

        var reducedA = matrixA
        var reducedB = matrixB

        var isSimplified = false
        reducedA.forEachIndexed { i, rows ->
            rows.forEachIndexed { j, expression ->
                val simplify = expression.simplify()
                if (simplify.isSimplified){
                    printLatex(expression.toLatexString())
                    isSimplified = true
                    reducedA[i][j] = simplify.expression
                }
            }
        }
        reducedB.forEachIndexed { i, rows ->
            rows.forEachIndexed { j, expression ->
                val simplify = expression.simplify()
                if (simplify.isSimplified){
                    isSimplified = true
                    reducedB[i][j] = simplify.expression
                }
            }
        }
        if (isSimplified){
            printEliminationMatrix(reducedA, reducedB, null, """(A|B) = """)
        }

        for (i in reducedA.indices) {
            var tempA = reducedA.copyOf()
            var tempB = reducedB.copyOf()

            var ii = reducedA[i][i]
            if (ii.numerator.isZero() && i == reducedA.size-1) continue

            var exchangeIndex = i+1
            while (ii.numerator.isZero() && i != reducedA.size-1 && exchangeIndex != reducedA.size){
                val temp1 = reducedA[i]
                val temp2 = reducedB[i]
                reducedA[i] = reducedA[exchangeIndex]
                reducedB[i] = reducedB[exchangeIndex]
                tempA = reducedA.copyOf()
                tempB = reducedB.copyOf()
                ii = reducedA[i][i]
                reducedA[exchangeIndex] = temp1
                reducedB[exchangeIndex] = temp2
                exchangeIndex++

                val exchangeOperations = Array(reducedA.size){""" \space """}
                exchangeOperations[i] = """R_${i+1} \leftrightarrow R_$exchangeIndex"""

                printEliminationMatrix(reducedA,reducedB, exchangeOperations)
            }

            val rowOperations = Array(matrixA.size){""" \space """}

            var isOp = false
            if (!ii.numerator.isOne() || !ii.denominator.isOne()) {
                isOp = true
                val inverse = 1.divideBy(ii)
                rowOperations[i] = """$inverse \times R_${i+1} """.removePlusOnStart()
                tempA[i] = inverse.times(reducedA[i])
                tempB[i] = inverse.times(reducedB[i])
            }

            if (ii.isNumber()){
                for (row in i+1 until reducedA.size) {
                    val exp = (-1).divideBy(ii).times(reducedA[row][i])
                    if (!exp.numerator.isZero()){
                        isOp = true
                        rowOperations[row] = """ R_${row+1}${exp.parenthesize()}R_${i+1} """
                    }
                    tempA[row] = reducedA[row].add(exp.times(reducedA[i]))
                    tempB[row] = reducedB[row].add(exp.times(reducedB[i]))
                }
            } else {
                if (isOp){
                    printEliminationMatrix(tempA,tempB, rowOperations)
                    rowOperations[i] = """ \space """
                    isOp = false
                }
                reducedA = tempA
                reducedB = tempB

                for (row in i+1 until reducedA.size) {
                    val exp = (-1).times(reducedA[row][i])
                    if (!exp.numerator.isZero()){
                        isOp = true
                        rowOperations[row] = """ R_${row+1}${exp.parenthesize()}R_${i+1} """
                    }

                    tempA[row] = reducedA[row].add(exp.times(reducedA[i]))
                    tempB[row] = reducedB[row].add(exp.times(reducedB[i]))
                }

                if (isOp){
                    printEliminationMatrix(reducedA,reducedB, rowOperations)
                }
            }


            if (isOp){
                printEliminationMatrix(tempA,tempB, rowOperations)
            }
            reducedA = tempA
            reducedB = tempB
        }

        var lastRowA = Double(0)
        for (elem in reducedA[reducedA.size-1]){
            lastRowA = lastRowA.add(elem)
        }
        var lastRowB = Double(0)
        for (elem in reducedB[reducedB.size-1]){
            lastRowB = lastRowB.add(elem)
        }
        for (i in (if (lastRowA.numerator.isZero()) reducedA.size-3 else reducedA.size-2) downTo  0) {
            val rowOperations = Array(matrixA.size){""" \space """}

            val tempA = reducedA.copyOf()
            val tempB = reducedB.copyOf()
            var isOp = false
            for (row in i downTo  0){
                val exp = (-1.0).times(reducedA[row][i+1])

                if(!exp.numerator.isZero())  {
                    isOp = true
                    rowOperations[row] = """ R_${row+1}${exp.parenthesize()} R_${i+2} """
                }

                tempA[row] = reducedA[row].add(exp.times(reducedA[i+1]))
                tempB[row] = reducedB[row].add(exp.times(reducedB[i+1]))
            }
            reducedA = tempA
            reducedB = tempB
            if (isOp){
                printEliminationMatrix(reducedA,reducedB, rowOperations)
            }
        }

        if (!lastRowA.numerator.isZero()) {
            val x = reducedB.copyOf()
            printX(x)
        }
        else {
            if (!lastRowB.numerator.isZero()) {
                printLatex("""If in the last row(\(R_${reducedA.size}\)), \(A_{${reducedA.size}${reducedA.size}} = 0\) and \(B_{${reducedA.size}1} ≠ 0\), then the system is inconsistent.\(\\\)The system has no solution.""", 12)
                space()
            } else if (lastRowB.numerator.isZero()) {
                printLatex("""If in the last row(\(R_${reducedA.size}\)), \(A_{${reducedA.size}${reducedA.size}} = 0\) and \(B_{${reducedA.size}1} = 0\), then the system is consistent and has infinitely many solutions.""", 12)
                space()
                printLatex("Parametric general solution",14)
                val xVariables = xVariables(matrixA.size)
                printLatex("""Let \(${xVariables[matrixA.size-1]} = t &ensp; ⟹ &ensp; A_{${reducedA.size}${reducedA.size}} = 1\) and \(B_{${reducedA.size}1} = t\)""", 12)


                val tempA = reducedA.copyOf()
                val tempB = reducedB.copyOf()
                tempA[tempA.size-1][tempA[0].size-1] = Double(1)
                tempB[tempB.size-1][tempB[0].size-1] = Double('t')
                printEliminationMatrix(tempA,tempB, null)

                val rowOperations = Array(matrixA.size){""" \space """}
                var isOp = false
                for (row in tempA.size-2 downTo  0) {
                    val lastCol = (-1).times(tempA[row][tempA[0].size-1])

                    if(!lastCol.numerator.isZero()) {
                        isOp = true
                        rowOperations[row] = """ R_${row+1}${lastCol.parenthesize()} R_${tempA.size} """
                    }
                    tempA[row] = tempA[row].add(lastCol.times(tempA[tempA.size-1]))
                    if (lastCol.isNumber()) {
                        tempB[row][0] = tempB[row][0].add(lastCol.numerator.addLikeTerms()[0].coefficient.times(tempB[tempB.size-1][0]))
                    } else {
                        tempB[row][0] = tempB[row][0].add(lastCol.times(tempB[tempB.size-1][0]))
                    }
                }
                if (isOp){
                    printEliminationMatrix(tempA,tempB, rowOperations)
                }

                val x = tempB.copyOf()
                printX(x, withT = true)
            }
        }

    }

    private fun Double.parenthesize() : String {
        return if (denominator.isOne() && numerator.size > 1){
            if (toString().startsWith("-")){
                "-(${(-1).times(this).toString().removePlusOnStart()})"
            } else {
                "+(${toString().removePlusOnStart()})"
            }
        } else toString()
    }*/



    /*fun LinearLayout.salveByIteration(matrixA: Array<DoubleArray>,matrixB: Array<DoubleArray>, fixedPoint: Int) {
        printText("Iteration Method", 22)
        space()
        printEquations(matrixA, matrixB)
        space()

        printText("x = (${eq1.d.toTerm()} ${eq1.b.toTerm("y")} ${eq1.c.toTerm("z")})/${eq1.a.toTerm()}")
        printText("y = (${eq2.d.toTerm()} ${eq2.a.toTerm("x")} ${eq2.c.toTerm("z")})/${eq2.b.toTerm()}")
        printText("z = (${eq3.d.toTerm()} ${eq3.a.toTerm("x")} ${eq3.b.toTerm("y")})/${eq3.c.toTerm()}")
        space()

        var x = 0.0
        var y = 0.0
        var z = 0.0
        var error = 0.0

        printText ("i       x(i)       y(i)       z(i)       Error\n")
        var i = 0
        while(true) {
            val string = StringBuilder().apply {
                append("$i       ")
                append("$x       ")
                append("$y       ")
                append("$z       ")
                append(error)
            }
            printText(string.toString())

            var ex = false
            val xV = ((eq1.d - eq1.b*y - eq1.c*z) / eq1.a).roundTo(fixedPoint+1)
            val yV = ((eq2.d - eq2.a*xV.roundTo(fixedPoint) - eq2.c*z) / eq2.b).roundTo(fixedPoint+1)
            val zV = ((eq3.d - eq3.a*xV.roundTo(fixedPoint) - eq3.b*yV.roundTo(fixedPoint)) / eq3.c).roundTo(fixedPoint+1)
            val err = (abs(xV - x) + abs(yV - y) + abs(zV - z)).roundTo(fixedPoint+1)
            if (xV.roundTo(fixedPoint) == x && yV.roundTo(fixedPoint) == y && zV.roundTo(fixedPoint) == z) ex = true
            x = xV.roundTo(fixedPoint)
            y = yV.roundTo(fixedPoint)
            z = zV.roundTo(fixedPoint)
            i++

            val n = 10.0.pow(-fixedPoint-1)
            if ( ex || err < 5*n || i==30) {
                error = err
                val lastString = StringBuilder().apply {
                    append("$i       ")
                    append("$x       ")
                    append("$y       ")
                    append("$z       ")
                    append(error)
                }
                printText(lastString.toString())
                break
            }
            error = err
        }

        printText("\n")
        printText("x = $x")
        printText("y = $y")
        printText("z = $z")
        printText("error = $error")


        var actualX = 0.0
        var actualY = 0.0
        var actualZ = 0.0
        repeat(1000) {
            actualX = (eq1.d - eq1.b*actualY - eq1.c*actualZ) / eq1.a
            actualY = (eq2.d - eq2.a*actualX - eq2.c*actualZ) / eq2.b
            actualZ = (eq3.d - eq3.a*actualX - eq3.b*actualY) / eq3.c
        }

        printText("")
        printText("Actual Values")
        printText("x = ${actualX.toFractionNumber().latex}")
        printText("y = ${actualY.toFractionNumber().latex}")
        printText("z = ${actualZ.toFractionNumber().latex}")
    }*/

    /*fun LinearLayout.salveByLUDecomposition(matrixA: Array<DoubleArray>,matrixB: Array<DoubleArray>) {
        printText("LU Decomposition Method", 22)
        space()
        //printEquations(matrixA, matrixB)
        printMatrixAB(matrixA, matrixB)
        space()

        val upperTriM = matrixA.copyOf()
        val lowerTriM = identityMatrix(matrixA.size)

        for (row in 1 until matrixA.size) {
            val rows = Array(matrixB.size){""}

            for (index in row until matrixA.size){
                val num = upperTriM[index][row-1]/upperTriM[row-1][row-1]

                lowerTriM[index][row-1] = num

                upperTriM[index] = if (num < 0) {
                    rows[index] = ("R<sub><small>${index+1}</small></sub> + ${(-num).toFractionNumber().latex}R<sub><small>$row</small></sub>")
                    upperTriM[index].add((-num).times(upperTriM[row-1]))
                } else {
                    rows[index] = ("R<sub><small>${index+1}</small></sub> - ${num.toFractionNumber().latex}R<sub><small>$row</small></sub>")
                    upperTriM[index].minus(num.times(upperTriM[row-1]))
                }
            }

            printEliminationMatrix(lowerTriM.toExpressionMatrix(), Array(lowerTriM.size){ Array(0){Double()} }, rows)
        }

        printMatrix("U = ", upperTriM)
        printMatrix("L = ", lowerTriM)

        space()
        printText("Ax = LUx = b, then if Ux = y, then", 20)
        printText("Ly = b", 20)
        space(0,10)
        printMatrixTimesX( lowerTriM, yCoefficients.take(lowerTriM.size).toTypedArray()  , " = ", matrixB, 4)

        val y = Array(lowerTriM.size) { DoubleArray(1) }
        lowerTriM.forEachIndexed { row, doubles ->
            if (row == 0) {
                y[row][0] = matrixB[row][0]
                printText("${yCoefficients[row]} = ${matrixB[row][0].toFractionNumber().latex}", 18, 8)
            }
            else {
                val equation = StringBuilder()
                for (col in 0 .. row) {
                    val term = when(yCoefficients[col]) {
                        "y<sub><small>1</small></sub>" -> {
                            if (doubles[col] < 0) {
                                if (doubles[col] == -1.0) "-${yCoefficients[col]}"
                                else "-${(-doubles[col]).toFractionNumber().latex}${yCoefficients[col]}"
                            } else if (doubles[col] == 0.0) {
                                ""
                            } else {
                                if (doubles[col] == 1.0) yCoefficients[col]
                                else "${doubles[col].toFractionNumber().latex}${yCoefficients[col]}"
                            }
                        }
                        else -> {
                            if (doubles[col] < 0) {
                                if (doubles[col] == -1.0) " ‒ ${yCoefficients[col]}"
                                else " ‒ ${(-doubles[col]).toFractionNumber().latex}${yCoefficients[col]}"
                            } else if (doubles[col] == 0.0) {
                                ""
                            } else {
                                if (doubles[col] == 1.0) " + ${yCoefficients[col]}"
                                else " + ${doubles[col].toFractionNumber().latex}${yCoefficients[col]}"
                            }
                        }
                    }
                    equation.append(term)
                }
                equation.append(" = ${matrixB[row][0].toFractionNumber().latex}")

                var answer = matrixB[row][0]
                for (col in 0 until row) {
                    answer -= doubles[col] * y[col][0]
                }
                y[row][0] = answer
                equation.append("  , ⟹ ${yCoefficients[row]} = ${answer.toFractionNumber().latex}")
                printText(equation.toString(), 16,4)
            }
        }
        space(height=10)
        printMatrix("∴ y = ", y)


        space(height = 15)
        printText("So, Ux = y", 20, 6)
        printMatrixTimesX( upperTriM, xVariables().take(upperTriM.size).toTypedArray(), " = ", y, 4)

        var lastRowSum = 0.0
        for (elem in upperTriM[upperTriM.size-1]){
            lastRowSum += elem
        }
        val lastY = y[y.size-1][0]

        val x = Array(upperTriM.size) { DoubleArray(1) }
        if (lastRowSum != 0.0){
            for (row in upperTriM.size-1 downTo 0){
                val doubles = upperTriM[row]

                val equation = StringBuilder()
                for (col in row until  upperTriM.size) {
                    equation.append(doubles[col].toSubject(row, xVariables()[col]))
                }
                equation.append(" = ${y[row][0].toFractionNumber().latex}")

                var answer = y[row][0]
                for (col in row+1 until upperTriM.size) {
                    answer -= doubles[col] * x[col][0]
                }
                if (doubles[row] != 0.0 && doubles[row] != 1.0) {
                    val inverse = 1/doubles[row]
                    answer *= inverse
                }
                x[row][0] = answer
                equation.append("  , ⟹ ${xVariables()[row]} = ${answer.toFractionNumber().latex}")
                printText(equation.toString(), 16,4)
            }

            printX(x)
        }
        else if (lastY != 0.0){
            printText("The system has no solution.",18, 4)
        }
        else {
            printText("The system has infinitely many solutions.",18, 4)
            space()
            printText("Parametric general solution",18, 4)

            printText("Let ${xVariables()[upperTriM.size-1]} = t", 16,4)
            val t = DoubleArray(upperTriM.size); t[upperTriM.size-1] = 1.0
            x[upperTriM.size-1][0] = 0.0
            val xMatrix = Array(upperTriM.size) { Array(1){""} }
            xMatrix[0][0] = ("t")
            for (row in upperTriM.size-2 downTo 0){
                val doubles = upperTriM[row]

                val equation = StringBuilder()
                for (col in row until  upperTriM.size) {
                    equation.append(doubles[col].toSubject(row, xVariables()[col]))
                }
                equation.append(" = ${y[row][0].toFractionNumber().latex}")

                var answer = y[row][0]
                var tCoef = 0.0
                for (col in row+1 until upperTriM.size) {
                    answer -= doubles[col] * x[col][0]
                    tCoef -= doubles[col] * t[col]
                }
                if (doubles[row] != 0.0 && doubles[row] != 1.0) {
                    val inverse = 1/doubles[row]
                    answer *= inverse
                    tCoef *= inverse
                }
                x[row][0] = answer
                t[row] = tCoef

                equation.append("&ensp;&ensp;⟹&ensp;${xVariables()[row]} = ")
                val string = if (tCoef < 0.0){
                    if (tCoef == -1.0) "${answer.toFractionNumber().latex} ‒ t"
                    else "${answer.toFractionNumber().latex} ‒ ${(-tCoef).toFractionNumber().latex}t"
                } else if (tCoef > 0.0){
                    if (tCoef == 1.0) "${answer.toFractionNumber().latex} + t"
                    else "${answer.toFractionNumber().latex} + ${tCoef.toFractionNumber().latex}t"
                } else answer.toFractionNumber().latex
                equation.append(string)
                xMatrix[row][0] = (string)
                printText(equation.toString(), 16,4)
            }
           // printX(xMatrix.reversed().toTypedArray())
        }
    }*/

    /*fun LinearLayout.salveByInverseMethod(matrixA: Array<DoubleArray>,matrixB: Array<DoubleArray>) {
        printText("Inverse of Matrix Method", 22)
        space()
        printEquations(matrixA, matrixB)
        printMatrixAB(matrixA, matrixB)

        val c = matrixA.cofactors()
        printMatrix("C<sub><small>A</small></sub> = ", c)
        val adj = c.transpose()
        printMatrix("<small>Adj</small>A = C<sub><small>A</small></sub><sup><small>T</small></sup> = ", adj)
        space()

        val det = matrixA.determinant()
        printDetMatrix("|A| = ", matrixA, det)
        space()

        if (det != 0.0){
            val inverse = (1.0/det).times(adj)
            printText("A<sup><small>-1</small></sup> = <sup>1</sup>/<sub>|A|</sub> \times <small>Adj</small>A", 18, 8)
            printMatrix("A<sup><small>-1</small></sup> = <sup>1</sup>/<sub>${det.toFractionNumber().latex}</sub> \times ", adj)
            printMatrix("A<sup><small>-1</small></sup> = ", inverse)
            space()

            val x = inverse.times(matrixB)
            printText("X = A<sup>-1</sup> \times B", 18, 8)
            printMatrixMultiplication("X = ", inverse,  matrixB)
            printX(x)
        }
        else {
            val adjB = printMatrixMultiplication("<small>Adj</small>A \times B = ", adj, matrixB)
            if (!adjB.isZero()) {
                printText("If |A| = 0, and <small>Adj</small>A \times B ≠ 0 then the system is inconsistent. <br/>The system has no solution.",18, 4)
                space()
            }
            else {
                printText("If |A| = 0, and <small>Adj</small>A \times B = 0, then the system is consistent and has infinitely many solutions.",18, 4)
                space()
                printText("Parametric general solution",18, 4)
                printText("Let ${xVariables()[matrixA.size-1]} = t &ensp;&ensp;⟹", 16,4)


                val expMatrixB = Array(matrixA.size){Array(1){Double()} }

                matrixA.forEachIndexed { row, doubles ->
                    val string = StringBuilder()

                    for (col in 0 until doubles.size-1) {
                        val d = doubles[col]
                        val term = when(xVariables()[col]) {
                            "x<sub><small>1</small></sub>" -> {
                                if (d < 0) {
                                    if (d == -1.0) "-${xVariables()[col]}" else "-${(-d).toFractionNumber().latex}${xVariables()[col]}"
                                } else if (d == 0.0) {
                                    ""
                                } else {
                                    if (d == 1.0) xVariables()[col] else "${d.toFractionNumber().latex}${xVariables()[col]}"
                                }
                            }
                            "x<sub><small>2</small></sub>", "x<sub><small>3</small></sub>", "x<sub><small>4</small></sub>",
                            "x<sub><small>5</small></sub>", "x<sub><small>6</small></sub>" -> {
                                if (d < 0) {
                                    if (d == -1.0) " ‒ ${xVariables()[col]}" else " ‒ ${(-d).toFractionNumber().latex}${xVariables()[col]}"
                                } else if (d == 0.0) {
                                    ""
                                } else {
                                    if (d == 1.0) " + ${xVariables()[col]}" else " + ${d.toFractionNumber().latex}${xVariables()[col]}"
                                }
                            }
                            else -> {
                                if (d < 0) {
                                    " = -${(-d).toFractionNumber().latex}"
                                } else {
                                    " = ${d.toFractionNumber().latex}"
                                }
                            }
                        }
                        string.append(term)
                    }

                    expMatrixB[row][0] = Double(matrixB[row][0], -doubles[doubles.size-1])

                    string.append(" = ${expMatrixB[row][0]}")
                    printText(string.toString(),18, 4)
                }

                val matrix = matrixA.toExpressionMatrix().takeForDet(expMatrixB)
                inverseParametricSol(matrix.matrixA, matrix.matrixB, matrix.excludedRow)
            }
        }
    }
    private fun LinearLayout.inverseParametricSol(expMatrixA: Array<DoubleArray>, expMatrixB: Array<DoubleArray>, excludedRow: Int) {
        if (expMatrixA.size == 1) {
            val x = Array(expMatrixA.size+1) { Array(1){Double()} }
            printText("Then we choose any equation and salve for x<sub><small>1</small></sub>. If we choose the first equation then,")

            val coef = expMatrixA[0][0]
            printText("x<sub><small><small>1</small></small></sub> = <sup>(${expMatrixB[0][0]})</sup>/<sub>$coef</sub>", 16,4)

            x[0][0] = (1/coef.getNumber()).times(expMatrixB[0][0])
            x[1][0] = Double(0.0, 1.0)
            printText("x<sub><small>1</small></sub> = ${x[0][0]}",16,6)

            printX(x)
        }
        else {
            printText("Then we choose any ${expMatrixA.size} system of equations and salve. If we choose to cancel equation $excludedRow then,")
            printMatrixAB(expMatrixA, expMatrixB)

            val c = expMatrixA.cofactors()
            printMatrix("C<sub><small>A<sub><small>1</small></sub></small></sub> = ", c)
            val adj = c.transpose()
            printMatrix("<small>Adj</small>A<sub><small>1</small></sub></sub> = C<sub><small>A<sub><small>1</small></sub></sub></small></sub><sup><small>T</small></sup> = ", adj)
            space(height=10)

            val det = expMatrixA.determinant()
            printDetMatrix("|A<sub><small>1</small></sub>| = ", expMatrixA, det)
            space()

            val inverse = (1.0/det.getNumber()).times(adj)
            printText("A<sup><small>-1</small></sup> = <sup>1</sup>/<sub>|A|</sub> \times <small>Adj</small>A", 18, 8)
            printMatrix("A<sup><small>-1</small></sup> = <sup>1</sup>/<sub>$det</sub> \times ", adj)
            printMatrix("A<sup><small>-1</small></sup> = ", inverse)
            space()

            printText("X = A<sup>-1</sup> \times B", 18, 8)
            printMatrixMultiplication("X = ", inverse,  expMatrixB)

            val x = Array(expMatrixA.size+1) { Array(1){Double()} }
            val xTemp = inverse.times(expMatrixB)
            xTemp.forEachIndexed { index, expressions ->
                x[index][0] = expressions[0]
            }
            x[expMatrixA.size][0] = Double(0.0, 1.0)

            printX(x)
        }
    }*/

    fun solveByCramersRule(matrixA: Array<DoubleArray>,matrixB: Array<DoubleArray>) {
        val answer = StringBuilder()
        answer.append("## Solve by Cramer's Rule Method")
//        printEquations(matrixA,matrixB)
//        printMatrixAB(matrixA, matrixB)

        val det = matrixA.determinant()
//        printDetMatrix("D = ", matrixA, det)

        val determinants = mutableListOf<Double>()
        for (index in matrixA.indices) {
            val matrix = Array(matrixA.size) { DoubleArray(matrixA[0].size) }
            matrixA.forEachIndexed { row, doubles ->
                doubles.forEachIndexed { col, d ->
                    if (col == index){
                        matrix[row][index] = matrixB[row][0]
                    } else {
                        matrix[row][col] = d
                    }
                }
            }
            val dx = matrix.determinant()
            determinants.add(dx)
            answer.append("D<sub><small>${xVariables()[index]}</small></sub> = "+ matrix + "=" + dx)
        }

        if (det != 0.0){
            val x = Array(matrixA.size) {DoubleArray(1)}
            val string = StringBuilder()
            determinants.forEachIndexed { index, determinant ->
                string.append("${xVariables()[index]} = ")
                string.append("<sup>D<sub><small>${xVariables()[index]}</small></sub></sup>/<sub>D</sub> = ")
                string.append("<sup>${determinant.toFractionNumber().latex}</sup>/<sub>${det.toFractionNumber().latex}</sub> = ")
                val xValue = determinant/det
                x[index][0] = xValue
                string.append(xValue.toFractionNumber().latex)
                string.append("<br/>")
            }
            answer.append(string.toString())

            answer.append("x = $x")
        }
        else {
            var isNoSolution = false
            determinants.forEach { d ->
                if (d != 0.0){
                    isNoSolution = true
                }
            }
            if (isNoSolution){
                answer.append("* If |D| = 0, and some D<sub><small>x<sub><small>i</small></sub></small></sub> ≠ 0 then the system is inconsistent. <br/>The system has no solution.")
            }
            else {
                answer.append("* If |A| = 0, and all D<sub><small>x<sub><small>i</small></sub></small></sub> = 0, then the system is consistent and has infinitely many solutions.")

                answer.append("* Parametric general solution")

                answer.append("* Let ${xVariables()[matrixA.size-1]} = t &ensp;&ensp;⟹")

                val expMatrixB = Array(matrixA.size){ DoubleArray(1){ 0.0 } }

                matrixA.forEachIndexed { row, doubles ->
                    val string = StringBuilder()

                    for (col in 0 until doubles.size-1) {
                        val d = doubles[col]
                        val term = when(xVariables()[col]) {
                            "x<sub><small>1</small></sub>" -> {
                                if (d < 0) {
                                    if (d == -1.0) "-${xVariables()[col]}" else "-${(-d).toFractionNumber().latex}${xVariables()[col]}"
                                } else if (d == 0.0) {
                                    ""
                                } else {
                                    if (d == 1.0) xVariables()[col] else "${d.toFractionNumber().latex}${xVariables()[col]}"
                                }
                            }
                            "x<sub><small>2</small></sub>", "x<sub><small>3</small></sub>", "x<sub><small>4</small></sub>",
                            "x<sub><small>5</small></sub>", "x<sub><small>6</small></sub>" -> {
                                if (d < 0) {
                                    if (d == -1.0) " ‒ ${xVariables()[col]}" else " ‒ ${(-d).toFractionNumber().latex}${xVariables()[col]}"
                                } else if (d == 0.0) {
                                    ""
                                } else {
                                    if (d == 1.0) " + ${xVariables()[col]}" else " + ${d.toFractionNumber().latex}${xVariables()[col]}"
                                }
                            }
                            else -> {
                                if (d < 0) {
                                    " = -${(-d).toFractionNumber().latex}"
                                } else {
                                    " = ${d.toFractionNumber().latex}"
                                }
                            }
                        }
                        string.append(term)
                    }

//                    expMatrixB[row][0] = Double(matrixB[row][0], -doubles[doubles.size-1])

                    string.append(" = ${expMatrixB[row][0]}")
                    answer.append("---")
                    answer.append(string.toString())
                }

                val matrix = matrixA.takeForDet(expMatrixB)
                cramersRuleParametricSol(matrix.matrixA, matrix.matrixB, matrix.excludedRow)
            }
        }
    }
    private fun cramersRuleParametricSol(expMatrixA: Array<DoubleArray>, expMatrixB: Array<DoubleArray>, excludedRow: Int){
        if (expMatrixA.size == 1) {
            val x = Array(expMatrixA.size+1) { Array(1){ 0.0 } }
//            printText("Then we choose any equation and salve for x<sub><small>1</small></sub>. If we choose the first equation then,")

            val coef = expMatrixA[0][0]
//            printText("x<sub><small><small>1</small></small></sub> = <sup>(${expMatrixB[0][0]})</sup>/<sub>$coef</sub>", 16,4)

            x[0][0] = (1/coef).times(expMatrixB[0][0])
            x[1][0] = 0.0
//            printText("x<sub><small>1</small></sub> = ${x[0][0]}",16,6)

//            printX(x)
        }
        else {
//            printText("Then we choose any ${expMatrixA.size} system of equations and salve. If we choose to cancel equation $excludedRow then,")
//            printMatrixAB(expMatrixA, expMatrixB)

            val expDet = expMatrixA.determinant()
//            printDetMatrix("D = ", expMatrixA, expDet)

            val x = Array(expMatrixA.size+1) { Array(1){ 0.0 } }

            for (index in expMatrixB.indices) {
                val m = expMatrixA.transpose()
                m[index] = expMatrixB.transpose()[0]

                val matrix = m.transpose()
                val dx = matrix.determinant()

                x[index][0] = (1/expDet) * dx

//                printDetMatrix("D<sub><small>${xVariables()[index]}</small></sub> = ", matrix, dx)
//                printText("&ensp;&ensp;${xVariables()[index]} = <sup>D<sub><small>${xVariables()[index]}</small></sub></sup>/<sub>D</sub> = <sup>($dx)</sup>/<sub>${expDet.getNumber().toFractionNumber().latex}</sub> = ${x[index][0]}", paddingVertical = 4)
            }

            x[expMatrixA.size][0] = 0.0
//            printX(x)
        }
    }
}