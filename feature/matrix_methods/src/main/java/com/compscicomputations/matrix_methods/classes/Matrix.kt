package com.compscicomputations.matrix_methods.classes

import android.widget.LinearLayout
import androidx.core.text.isDigitsOnly
import com.compscicomputations.matrix_methods.classes.Expressions.Expression
import com.compscicomputations.matrix_methods.classes.Expressions.isOne
import com.compscicomputations.matrix_methods.classes.Expressions.isZero
import com.compscicomputations.matrix_methods.classes.Expressions.removePlusOnStart
import com.compscicomputations.matrix_methods.utils.Constants.xVariables
import com.compscicomputations.matrix_methods.utils.Utils.printLatex
import com.compscicomputations.matrix_methods.utils.Utils.space
import com.compscicomputations.matrix_methods.classes.Expressions.times
import com.compscicomputations.matrix_methods.utils.Utils.toFractionNumber
import kotlin.math.pow

object Matrix {
    private const val MATRIX_NEW_LINE = """ \\[0.7em] """

    fun LinearLayout.printMatrix(string: String, matrix: Array<Array<Expression>>){
        val matrixString = StringBuilder().apply{
            append("""$string \begin{pmatrix} """)
        }
        matrix.forEachIndexed {i, a ->
            a.forEachIndexed { j, b->
                matrixString.append(b.toString().removePlusOnStart())
                if(j < a.size-1){
                    matrixString.append(""" & """)
                }

            }
            if(i < matrix.size-1){
                matrixString.append(MATRIX_NEW_LINE)
            }
        }
        matrixString.append(""" \end{pmatrix} """)

        printLatex("""\($matrixString\)""")
    }

    fun LinearLayout.printDetMatrix(string: String, matrix: Array<Array<Expression>>, determinant : Double){
        val matrixString = StringBuilder().apply{
            append("""$string \begin{vmatrix} """)
        }
        matrix.forEachIndexed {i, a ->
            a.forEachIndexed { j, b->
                matrixString.append(b.toString())
                if(j < a.size-1){
                    matrixString.append(""" & """)
                }

            }
            if(i < matrix.size-1){
                matrixString.append(MATRIX_NEW_LINE)
            }
        }
        matrixString.append(""" \end{vmatrix} """)
        matrixString.append(""" = ${determinant.toFractionNumber().latex} """)

        printLatex("""\($matrixString\)""")
    }

    fun LinearLayout.printMatrixAB(matrixA: Array<Array<Expression>>, matrixB: Array<Array<Expression>>){

        val matrixString = StringBuilder().apply {
            append("""A = \begin{pmatrix} """)
        }
        matrixA.forEachIndexed {i, a ->
            a.forEachIndexed { j, b->
                matrixString.append(b.toString().removePlusOnStart())
                if(j < a.size-1){
                    matrixString.append(""" & """)
                }

            }
            if(i < matrixA.size-1){
                matrixString.append(MATRIX_NEW_LINE)
            }
        }
        matrixString.append(""" \end{pmatrix} """)



        matrixString.append("""B = \begin{pmatrix} """)
        matrixB.forEachIndexed {i, a ->
            a.forEachIndexed { j, b->
                matrixString.append(b.toString().removePlusOnStart())
                if(j < a.size-1){
                    matrixString.append(""" & """)
                }

            }
            if(i < matrixB.size-1){
                matrixString.append(MATRIX_NEW_LINE)
            }
        }
        matrixString.append(""" \end{pmatrix} """)

        printLatex("""\($matrixString\)""")
    }
    fun LinearLayout.printEquations(matrixA : Array<Array<Expression>>, matrixB : Array<Array<Expression>>) {
        val xVariables = xVariables(matrixA[0].size)

         matrixA.forEachIndexed { row, expressions ->
             val equation = StringBuilder()
             expressions.forEachIndexed { col, expression ->
                 if (!expression.numerator.isZero()){
                     if (expression.numerator.isOne() && expression.denominator.isOne()){
                         equation.append(expression.toString().take(1)+xVariables[col])
                     } else if (expression.numerator.size > 1) {
                         if (expression.toString().startsWith("-")){
                             val temp = -1 * expression
                             val expString = temp.toString().removePlusOnStart()
                             equation.append("-($expString)${xVariables[col]}")
                         } else {
                             equation.append("+(${expression.toString().removePlusOnStart()})${xVariables[col]}")
                         }
                     } else {
                        equation.append("$expression${xVariables[col]}")
                     }
                 }
             }
             if (matrixB[row][0].numerator.size > 1 && matrixB[row][0].toString().startsWith("-")) {
                 val temp = -1 * matrixB[row][0]
                 val expString = temp.toString().removePlusOnStart()
                 equation.append(" = -($expString)")
             } else {
                 equation.append(" = ${matrixB[row][0].toString().removePlusOnStart()}")
             }

             printLatex("$${equation.toString().removePlusOnStart()}$")
        }
        space()
    }
    fun LinearLayout.printEliminationMatrix(matrixA: Array<Array<Expression>>, matrixB: Array<Array<Expression>>,
                                            rowOperations : Array<String>?, string: String = """ \sim """) {
        val matrixString = StringBuilder().apply {
            append("""$string \left ( \left.\begin{matrix} """)
        }
        matrixA.forEachIndexed {i, a ->
            a.forEachIndexed { j, b->
                matrixString.append(b.toString().removePlusOnStart())
                if(j < a.size-1){
                    matrixString.append(""" & """)
                }

            }
            if(i < matrixA.size-1){
                matrixString.append(MATRIX_NEW_LINE)
            }
        }
        matrixString.append(""" \end{matrix}\right| \begin{matrix} """)
        matrixB.forEachIndexed { i, a ->
            a.forEachIndexed { j, b->
                matrixString.append(b.toString().removePlusOnStart())
                if(j < a.size-1){
                    matrixString.append(""" & """)
                }

            }
            if(i < matrixB.size-1){
                matrixString.append(MATRIX_NEW_LINE)
            }
        }
        matrixString.append(""" \end{matrix} \right ) """)

        if (!rowOperations.isNullOrEmpty()) {
            matrixString.append(""" \begin{matrix} """)
            rowOperations.forEachIndexed { i, a ->
                matrixString.append(a.removePlusOnStart())
                if(i < rowOperations.size-1){
                    matrixString.append(MATRIX_NEW_LINE)
                }
            }
            matrixString.append(""" \end{matrix} """)
        }

        printLatex("""\($matrixString\)""")
    }

    fun LinearLayout.printMatrixMultiplication(string: String, matrix1: Array<Array<Expression>>, matrix2: Array<Array<Expression>>) : Array<Array<Expression>>{

        return matrix1
    }

    fun LinearLayout.printX(x : Array<Array<Expression>>, str: String = "X = ", withT: Boolean = false) {
        space(height=10)
        printMatrix(str, x)

        val xCoe = xVariables(x.size)
        val subString = StringBuilder().apply { append("") }
        x.forEachIndexed { row, strings ->
            subString.append("${xCoe[row]} = ${strings[0].toString().replace("\\frac","\\dfrac").removePlusOnStart()}")
            if (row < x.size-2) {
                subString.append(""", \enspace """)
            } else if (row == x.size-2) {
                subString.append(""" \enspace and \enspace """)
            }
        }
        if (withT) {
            printLatex("""\(\therefore \enspace $subString \space , \newline \qquad \) where \(t ∈ &#8477;\)""")
        } else {
            printLatex("""\(\therefore \enspace $subString \)""")
        }
        space()
    }







    fun Array<DoubleArray>.toExpressionMatrix(rows : Int = this.size, cols: Int = this[0].size) : Array<Array<Expression>> {
        val res = Array(rows){ Array(cols){ Expression(0) } }
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                res[row][col] = Expression(this[row][col])
            }
        }
        return res
    }














    

    /*fun Array<Array<Expression>>.cofactors(isDetUse : Boolean = false) : Array<Array<Expression>> {
        val res = Array(this.size) { Array(this[0].size){ Expression(0) } }
        if (this.size == 1){
            res[0][0] = Expression(1.0)
            return res
        }

        for (row in this.indices) {
            for (col in this[0].indices) {
                // To store cofactors
                val temp = Array(this.size-1) { Array(this[0].size-1){ Expression(0) } }
                var y = 0
                var x = 0
                for (r in this.indices) {
                    for (c in this[0].indices) {
                        if (r != row && c != col) {
                            temp[y][x++] = this[r][c]
                            if (x == this.size - 1) {
                                x = 0
                                y++
                            }
                        }
                    }
                }
                res[row][col] = (-1.0).pow(row+col).times(temp.determinant())
            }
            if (isDetUse) break
        }
        return res
    }*/
    /*fun Array<Array<Expression>>.determinant(): Expression {
        if (this.size == 1) return this[0][0]
        if (this.size == 2) {
            val detA: Double = this[0][0].getNumber() * this[1][1].getNumber() - this[0][1].getNumber() * this[1][0].getNumber()
            val detB1 = this[0][0].getNumber() * this[1][1].getB() - this[0][1].getB() * this[1][0].getNumber()
            val detB2 = this[0][0].getB() * this[1][1].getNumber() - this[0][1].getNumber() * this[1][0].getB()
            val detB = detB1 + detB2
            val detC: Double = this[0][0].getB() * this[1][1].getB() - this[0][1].getB() * this[1][0].getB()

            return Expression(detA, detB, detC)
        }
        var det = Expression(0)
        val cofactors = cofactors(true)
        for (row in this.indices) {
            det = det.add(this[0][row].times(cofactors[0][row]))
        }
        return det
    }*/
    /*fun Array<Array<Expression>>.minus(expM: Array<Array<Expression>>): Array<Array<Expression>> {
        val res = Array(this.size) { Array(this[0].size){ Expression(0) } }
        for (row in this.indices)
            for (col in this[0].indices) {
                val a = this[row][col].getNumber() - expM[row][col].getNumber()
                val b = this[row][col].getB() - expM[row][col].getB()
                val c = this[row][col].getC() - expM[row][col].getC()
                res[row][col] = Expression(a, b, c)
            }
        return res
    }*/
    /*fun Array<Expression>.minus(expA: Array<Expression>): Array<Expression> {
        val res = Array(this.size){ Expression(0) }
        for (row in this.indices){
            val a = this[row].getNumber() - expA[row].getNumber()
            val b = this[row].getB() - expA[row].getB()
            val c = this[row].getC() - expA[row].getC()
            res[row] = Expression(a, b, c)
        }
        return res
    }*/
    /*fun Array<Array<Expression>>.add(expM: Array<Array<Expression>>): Array<Array<Expression>> {
        val res = Array(this.size) { Array(this[0].size){ Expression(0) } }
        for (row in this.indices)
            for (col in this[0].indices) {
                val a = this[row][col].getNumber() + expM[row][col].getNumber()
                val b = this[row][col].getB() + expM[row][col].getB()
                val c = this[row][col].getC() + expM[row][col].getC()
                res[row][col] = Expression(a, b, c)
            }
        return res
    }*/
    /*fun Array<Expression>.add(expA: Array<Expression>): Array<Expression> {
        val res = Array(this.size){ Expression(0) }
        for (row in this.indices){
            val a = this[row].getNumber() + expA[row].getNumber()
            val b = this[row].getB() + expA[row].getB()
            val c = this[row].getC() + expA[row].getC()
            res[row] = Expression(a, b, c)
        }
        return res
    }*/
    /*fun Expression.add(exp: Expression): Expression {
        val a = this.getNumber() + exp.getNumber()
        val b = this.getB() + exp.getB()
        val c = this.getC() + exp.getC()
        return Expression(a, b, c)
    }*/
    fun Array<Array<Expression>>.transpose(): Array<Array<Expression>> {
        val res = Array(this[0].size) { Array(this.size){ Expression(0) } }
        for (row in this.indices)
            for (col in this[0].indices)
                res[col][row] = this[row][col]
        return res
    }
    fun Array<Array<Expression>>.flip(): Array<Array<Expression>> {
        val res = Array(this.size) { Array(this[0].size){ Expression(0) } }
        val r = this.size - 1
        val c = this[0].size - 1
        for (row in this.indices)
            for (col in this[0].indices)
                res[row][col] = this[r - row][c - col]
        return res
    }
    fun Array<Array<Expression>>.combine(b: Array<Array<Expression>>): Array<Array<Expression>> {
        val res = Array(this.size) { Array(this[0].size+b[0].size){ Expression(0) } }
        for (row in this.indices) {
            for (col in this[0].indices)
                res[row][col] = this[row][col]
            for (col in b[0].indices)
                res[row][this[0].size+col] = b[row][col]
        }
        return res
    }
    /*fun Array<Array<Expression>>.times(b: Array<Array<Expression>>): *//*Matrix x Matrix*//* Array<Array<Expression>> {
        val rows = this.size
        val cols = b[0].size

        val res = Array(rows) { Array(cols){ Expression(0) } }

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                var ret = Expression(0)
                for (x in indices) {
                    ret = ret.add(this[row][x].times(b[x][col]))
                }
                res[row][col] = ret
            }
        }
        return res
    }*/
    /*fun Array<Expression>.times(b: Array<Expression>) *//*Row x Col*//* : Array<Expression> {
        val res = Array(1){ Expression(0) }

        var ret = Expression(0)
        for (x in indices) {
            ret = ret.add(this[x].times(b[x]))
        }
        res[0] = ret

        return res
    }*/
    /*fun Expression.times(a: Array<Array<Expression>>): *//*Expression x Matrix*//* Array<Array<Expression>> {
        val res = Array(a.size) { Array(a[0].size){ Expression(0) } }
        for (row in a.indices)
            for (col in a[0].indices)
                res[row][col] = this.times(a[row][col])
        return res
    }*/
    /*fun Expression.times(a: Array<Expression>): *//*Expression x Row*//* Array<Expression> {
        val res = Array(a.size){ Expression(0) }
        for (row in a.indices)
            res[row] = this.times(a[row])
        return res
    }*/
    /*fun Expression.times(exp : Expression): Expression {
        val a = this.getNumber() * exp.getNumber()
        val b = this.getNumber()*exp.getB() + this.getB()*exp.getNumber()
        val c = this.getB()*exp.getB()
        return Expression(a, b, c)
    }*/
    /*fun Double.times(a: Array<Array<Expression>>):  Array<Array<Expression>> {
        val res = Array(a.size) { Array(a[0].size){ Expression(0) } }
        for (row in a.indices)
            for (col in a[0].indices)
                res[row][col] = this.times(a[row][col])
        return res
    }*/
    /*fun Double.times(a: Array<Expression>): *//*Double x Row*//* Array<Expression> {
        val res =  Array(a.size){ Expression(0) }
        for (row in a.indices)
            res[row] = this.times(a[row])
        return res
    }*/

    fun Double.toExpression(): Expression {
        return Expression(this)
    }
    fun identityExpressionMatrix(size: Int) : Array<Array<Expression>>{
        val res = Array(size) { Array(size){ Expression(0) } }
        for (row in 0 until size)
            for (col in 0 until size) {
                if (row == col) res[row][col] = Expression(1.0)
                else res[row][col] = Expression(0)
            }
        return res
    }
    /*fun Array<Array<Expression>>.isZero() : Boolean {
        for (row in this.indices)
            for (col in this[0].indices) {
                if (this[row][col].getNumber() != 0.0 || this[row][col].getB() != 0.0 || this[row][col].getC() != 0.0){
                    return false
                }
            }
        return true
    }*/
    /*fun Array<Array<Expression>>.isIdentity() : Boolean {
        for (row in this.indices)
            for (col in this[0].indices) {
                if (row == col) {
                    if (this[row][col].getNumber() != 1.0 && (this[row][col].getB() != 0.0 || this[row][col].getC() != 0.0)){
                        return false
                    }
                } else {
                    if (this[row][col].getNumber() != 0.0 || this[row][col].getB() != 0.0 || this[row][col].getC() != 0.0){
                        return false
                    }
                }
            }
        return true
    }*/
    /*fun Array<Expression>.areLinear() : Boolean {
        var gotOne = false
        for (row in this.indices)
            if (this[row].getC() != 0.0) {
                if (!gotOne) gotOne = true
                else return true
            }
        return false
    }*/
    /*fun Expression.isLinear() : Boolean {
        return this.getC() == 0.0
    }*/
    /*fun Expression.isZero() : Boolean {
        return (this.getNumber() == 0.0 && this.getB() == 0.0 && this.getC() == 0.0)
    }*/
    /*fun Expression.isOne() : Boolean {
        return (this.getNumber() == 1.0 && this.getB() == 0.0 && this.getC() == 0.0)
    }*/
    fun Array<Array<Expression>>.take(size: Int = this.size - 1): Array<Array<Expression>> {
        val rows = if (this.size > size) size else this.size
        val cols = if (this[0].size > size) size else this[0].size
        val res = Array(rows) { Array(cols){ Expression(0) } }
        for (row in 0 until rows)
            for (col in 0 until cols)
                res[row][col] = this[row][col]
        return res
    }


    data class TakeMatrix(var matrixA: Array<Array<Expression>>, var matrixB: Array<Array<Expression>>, val excludedRow: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TakeMatrix

            if (!matrixA.contentDeepEquals(other.matrixA)) return false
            if (!matrixB.contentDeepEquals(other.matrixB)) return false
            if (excludedRow != other.excludedRow) return false

            return true
        }

        override fun hashCode(): Int {
            var result = matrixA.contentDeepHashCode()
            result = 31 * result + matrixB.contentDeepHashCode()
            result = 31 * result + excludedRow
            return result
        }
    }

    fun Array<Array<Expression>>.takeForDet(bM: Array<Array<Expression>>): TakeMatrix {

        val res = Array(this.size-1) { Array(this[0].size-1){ Expression(0) } }
        val b = Array(this.size-1) { Array(1){ Expression(0) } }
        var excludedRow = 3
        for (ro in this.size-1 downTo 0) {
            var y = 0
            var x = 0
            for (r in this.indices) {
                for (c in 0 until this[0].size-1) {
                    if (r != ro) {
                        res[y][x++] = this[r][c]
                        b[y][0] = bM[r][0]
                        if (x == this.size - 1) {
                            x = 0
                            y++
                        }
                    } else excludedRow = ro +1
                }
            }
            /*if (res.determinant().getNumber() == 0.0){
                continue
            }*/

            break
        }

        return TakeMatrix(res, b, excludedRow)
    }



    fun Array<DoubleArray>.toText() : String {
        val rowsString = StringBuilder()
        for (row in this.indices) {
            val colString = StringBuilder()
            for (col in this[0].indices) {
                if (col == this[0].size-1)  colString.append(this[row][col].toFractionNumber().latex)
                else colString.append(this[row][col].toFractionNumber().latex + "    ")
            }
            rowsString.append("$colString\n")
        }
        return rowsString.toString()
    }

    fun Array<DoubleArray>.isZero() : Boolean {
        for (row in this.indices)
            for (col in this[0].indices) {
                if (this[row][col] != 0.0){
                    return false
                }
            }
        return true
    }
    fun Array<DoubleArray>.take(size: Int): Array<DoubleArray> {
        val rows = if (this.size > size) size else this.size
        val cols = if (this[0].size > size) size else this[0].size

        val res = Array(rows) { DoubleArray(cols)}
        for (row in 0 until rows)
            for (col in 0 until cols)
                res[row][col] = this[row][col]
        return res
    }

    fun Array<DoubleArray>.minus(b: Array<DoubleArray>): Array<DoubleArray> {
        val res = Array(this.size) { DoubleArray(this[0].size) }
        for (row in this.indices)
            for (col in this[0].indices)
                res[row][col] = this[row][col] - b[row][col]
        return res
    }
    fun DoubleArray.minus(b: DoubleArray): DoubleArray {
        val res = DoubleArray(this.size)
        for (row in this.indices)
            res[row] = this[row] - b[row]
        return res
    }

    fun Array<DoubleArray>.add(b: Array<DoubleArray>): Array<DoubleArray> {
        val res = Array(this.size) { DoubleArray(this[0].size) }
        for (row in this.indices)
            for (col in this[0].indices)
                res[row][col] = this[row][col] + b[row][col]
        return res
    }
    fun DoubleArray.add(b: DoubleArray): DoubleArray {
        val res = DoubleArray(this.size)
        for (row in this.indices)
            res[row] = this[row] + b[row]
        return res
    }

    fun Array<DoubleArray>.times(b: Array<DoubleArray>): /*Matrix x Matrix*/ Array<DoubleArray> {
        val rows = this.size
        val cols = b[0].size

        val res = Array(rows) { DoubleArray(cols) }

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                var ret = 0.0
                for (x in indices) {
                    ret += this[row][x] * b[x][col]
                }
                res[row][col] = ret
            }
        }
        return res
    }
    fun DoubleArray.times(b: DoubleArray) /*Row x Col*/ : DoubleArray {
        val res = DoubleArray(1)

        var ret = 0.0
        for (x in indices) {
            ret += this[x] * b[x]
        }
        res[0] = ret

        return res
    }
    fun Double.times(a: Array<DoubleArray>): /*Double x Matrix*/ Array<DoubleArray> {
        val res = Array(a.size) { DoubleArray(a[0].size) }

        for (row in a.indices)
            for (col in a[0].indices)
                res[row][col] = this * a[row][col]
        return res
    }
    fun Double.times(a: DoubleArray): /*Double x Row*/ DoubleArray {
        val res = DoubleArray(a.size)
        for (row in a.indices)
            res[row] = this * a[row]
        return res
    }


    fun Array<DoubleArray>.transpose(): Array<DoubleArray> {
        val res = Array(this[0].size) { DoubleArray(this.size) }
        for (row in this.indices)
            for (col in this[0].indices)
                res[col][row] = this[row][col]
        return res
    }

    fun Array<DoubleArray>.flip(): Array<DoubleArray> {
        val res = Array(this.size) { DoubleArray(this[0].size) }
        val r = this.size - 1
        val c = this[0].size - 1
        for (row in this.indices)
            for (col in this[0].indices)
                res[row][col] = this[r - row][c - col]
        return res
    }
    fun Array<DoubleArray>.combine(b: Array<DoubleArray>): Array<DoubleArray> {
        val res = Array(this.size) { DoubleArray(this[0].size+b[0].size) }
        for (row in this.indices) {
            for (col in this[0].indices)
                res[row][col] = this[row][col]
            for (col in b[0].indices)
                res[row][this[0].size+col] = b[row][col]
        }
        return res
    }

    fun Array<DoubleArray>.cofactors(isDetUse : Boolean = false) : Array<DoubleArray> {
        val res = Array(this.size) { DoubleArray(this[0].size) }
        if (this.size == 1){
            res[0][0] = 1.0
            return res
        }

        for (row in this.indices) {
            for (col in this[0].indices) {
                // To store cofactors
                val temp = Array(this.size-1) { DoubleArray(this[0].size-1) }
                var y = 0
                var x = 0
                for (r in this.indices) {
                    for (c in this[0].indices) {
                        if (r != row && c != col) {
                            temp[y][x++] = this[r][c]
                            if (x == this.size - 1) {
                                x = 0
                                y++
                            }
                        }
                    }
                }
                res[row][col] = (-1.0).pow(row+col) * temp.determinant()
            }
            if (isDetUse) break
        }
        return res
    }
    fun Array<DoubleArray>.determinant(): Double {
        if (this.size == 1) return this[0][0]
        if (this.size == 2) {
            return this[0][0] * this[1][1] - this[0][1] * this[1][0]
        }
        var det = 0.0
        val cofactors = this.cofactors(true)
        for (row in this.indices) {
            det += this[0][row] * cofactors[0][row]
        }
        return det
    }

    fun identityMatrix(size: Int) : Array<DoubleArray>{
        val res = Array(size) { DoubleArray(size) }
        for (row in 0 until size)
            for (col in 0 until size) {
                if (row == col) res[row][col] = 1.0
                else res[row][col] = 0.0
            }

        return res
    }











    fun Double.toSubject(row: Int, coef: String): String{
        return when(coef) {
            xVariables()[row] -> {
                if (this < 0) {
                    if (this == -1.0) "-${coef}"
                    else "-${(-this).toFractionNumber().latex}${coef}"
                } else {
                    if (this == 1.0) coef
                    else "${this.toFractionNumber().latex}${coef}"
                }
            }
            else -> {
                if (this < 0) {
                    if (this == -1.0) " ‒ $coef"
                    else " ‒ ${(-this).toFractionNumber().latex}${coef}"
                } else if (this == 0.0) {
                    ""
                } else {
                    if (this == 1.0) " + $coef"
                    else " + ${this.toFractionNumber().latex}${coef}"
                }
            }
        }
    }

    fun String.isNumber() : Boolean {
        return isNotEmpty() && ( isDigitsOnly() || try { toDouble();true } catch (e: NumberFormatException){false})
    }

}