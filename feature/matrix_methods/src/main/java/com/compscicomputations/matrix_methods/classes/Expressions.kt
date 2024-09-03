package com.compscicomputations.matrix_methods.classes

import com.compscicomputations.matrix_methods.utils.Utils.toFractionNumber
import com.compscicomputations.matrix_methods.utils.Utils.zeroIfTooSmall

object Expressions {
    class Term {
        var coefficient: Double
        var variables: Map<Char, Double>
            get() = field.toSortedMap()

        constructor(number: Number) {
            this.coefficient = number.toDouble().zeroIfTooSmall
            variables = mapOf()
        }
        constructor(coefficient : Number, variable: Char){
            this.coefficient = coefficient.toDouble().zeroIfTooSmall
            val vars = mutableMapOf<Char, Double>()
            vars[variable] = 1.0
            variables = vars
        }
        constructor(coefficient : Number, variable: Char, exponent: Number){
            this.coefficient = coefficient.toDouble().zeroIfTooSmall
            if (exponent.toDouble() != 0.0) {
                val vars = mutableMapOf<Char, Double>()
                vars[variable] = exponent.toDouble()
                variables = vars
            } else {
                variables = mapOf()
            }
        }
        constructor(coefficient: Number, variables: Map<Char, Number>) {
            this.coefficient = coefficient.toDouble().zeroIfTooSmall
            val vars = mutableMapOf<Char, Double>()
            variables.forEach {
                if (it.value.toDouble() != 0.0) {
                    vars[it.key] = it.value.toDouble()
                }
            }
            this.variables = vars
        }
        constructor(variable: Char){
            coefficient = 1.0
            val vars = mutableMapOf<Char, Double>()
            vars[variable] = 1.0
            variables = vars
        }
        constructor(variable: Char, exponent: Number){
            coefficient = 1.0
            if (exponent.toDouble() != 0.0) {
                val vars = mutableMapOf<Char, Double>()
                vars[variable] = exponent.toDouble()
                variables = vars
            } else {
                variables = mapOf()
            }
        }
        constructor(variables: Map<Char, Number>) {
            coefficient = 1.0
            val vars = mutableMapOf<Char, Double>()
            variables.forEach {
                if (it.value.toDouble() != 0.0) {
                    vars[it.key] = it.value.toDouble()
                }
            }
            this.variables = vars
        }

        override fun toString(): String {
            return when {
                coefficient == 0.0 -> "+0"
                variables.isNotEmpty() -> {
                    val string = StringBuilder()
                    if (coefficient < 0) {
                        if (coefficient == -1.0) string.append('-')
                        else string.append(coefficient.toFractionNumber().latex)
                    } else if (coefficient > 0){
                        if (coefficient == 1.0) string.append('+')
                        else string.append("+${coefficient.toFractionNumber().latex}")
                    }

                    variables.forEach {
                        val variable = if (it.value == 1.0) "${it.key}"
                        else "${it.key}^{${it.value.toFractionNumber().latex}}"

                        string.append(variable)
                    }
                    string.toString()
                }
                else -> {
                    if (coefficient < 0){
                        coefficient.toFractionNumber().latex
                    } else {
                        "+${coefficient.toFractionNumber().latex}"
                    }
                }
            }
        }
        fun toLatexString(): String {
            return "$${toString()}$"
        }
    }
    private fun Term.isOne() : Boolean {
        val coefficient = this.coefficient == 1.0
        val variable = this.variables.isEmpty()
        return coefficient && variable
    }
    private fun Term.isZero() : Boolean {
        return this.coefficient == 0.0
    }
    private fun Term.equalsTo(term: Term) : Boolean {
        val coefficient = this.coefficient == term.coefficient
        val variable = this.variables == term.variables
        return coefficient && variable
    }
    private fun Term.divideBy(number: Number) : Term {
        val coefficient = this.coefficient/number.toDouble()
        return Term(coefficient, this.variables)
    }
    operator fun Number.times(term: Term) : Term {
        val coefficient = this.toDouble()*term.coefficient
        return Term(coefficient, term.variables)
    }
    operator fun Term.times(term: Term) : Term {
        val coefficient = this.coefficient*term.coefficient
        when {
            this.variables.isEmpty() && term.variables.isEmpty() -> {
                return Term(coefficient)
            }
            this.variables.isEmpty() -> {
                return Term(coefficient, term.variables)
            }
            term.variables.isEmpty() -> {
                return Term(coefficient, this.variables)
            }
            else -> {
                val variables = mutableMapOf<Char, Double>()
                var varsA = this.variables
                var varsB = term.variables
                varsA.forEach { varA ->
                    varsB.forEach { varB ->
                        if (varA.key == varB.key){
                            val exp = varA.value + varB.value
                            if (exp != 0.0) variables[varA.key] = exp
                            varsA = varsA.removeVar(varA.key)
                            varsB = varsB.removeVar(varB.key)
                        }
                    }
                }
                variables.putAll(varsA)
                variables.putAll(varsB)
                return Term(coefficient, variables)
            }
        }
    }
    private fun Map<Char, Double>.removeVar(key: Char) : Map<Char, Double>{
        val res = mutableMapOf<Char, Double>()
        this.forEach { variable ->
            if (variable.key != key){
                res[variable.key] = variable.value
            }
        }
        return res.toMap()
    }
    private fun Term.add(term: Term) : Expression {
        val coefficient = this.coefficient + term.coefficient
        if (coefficient == 0.0) return Expression(0)
        return when {
            this.variables.isEmpty() && term.variables.isEmpty() -> {
                Expression(coefficient)
            }
            this.variables.isEmpty() || term.variables.isEmpty() -> {
                Expression(arrayOf(this, term))
            }
            else -> {
                if (this.variables == term.variables)
                    Expression(coefficient, this.variables)
                else
                    Expression(arrayOf(this, term))
            }
        }
    }
    private fun Term.minus(term: Term) : Expression {
        val coefficient = this.coefficient - term.coefficient
        if (coefficient == 0.0) return Expression(0)
        return when {
            this.variables.isEmpty() && term.variables.isEmpty() -> {
                Expression(coefficient)
            }
            this.variables.isEmpty() || term.variables.isEmpty() -> {
                Expression(arrayOf(this, (-1).times(term)))
            }
            else -> {
                if (this.variables == term.variables)
                    Expression(coefficient, this.variables)
                else Expression(arrayOf(this, (-1).times(term)))
            }
        }
    }





    class Expression {
        var numerator : Array<Term> = arrayOf()
            get() = field.sortedTerms().ifEmpty { arrayOf(Term(0)) }
            set(value) {
                field = value.addLikeTerms()
            }

        var denominator : Array<Term> = arrayOf()
            get() = field.sortedTerms().ifEmpty { arrayOf(Term(1)) }
            set(value) {
                field = value.addLikeTerms()
            }

        constructor(term : Term){
            numerator = arrayOf(term)
            denominator = arrayOf(Term(1))
        }
        constructor(number : Number){
            numerator = arrayOf(Term(number))
            denominator = arrayOf(Term(1))
        }
        constructor(variable: Char){
            numerator = arrayOf(Term(variable))
            denominator = arrayOf(Term(1))
        }
        constructor(variable: Char, exponent: Number){
            numerator = arrayOf(Term(variable, exponent))
            denominator = arrayOf(Term(1))
        }
        constructor(variables: Map<Char, Number>) {
            numerator = arrayOf(Term(variables))
            denominator = arrayOf(Term(1))
        }
        constructor(coefficient : Number, variable: Char){
            val map : MutableMap<Char, Double> = mutableMapOf()
            map[variable] = 1.0
            numerator = arrayOf(Term(coefficient.toDouble(), map))
            denominator = arrayOf(Term(1))
        }
        constructor(coefficient : Number, variable: Char, exponent: Number){
            val map : MutableMap<Char, Double> = mutableMapOf()
            map[variable] = exponent.toDouble()
            numerator = arrayOf(Term(coefficient.toDouble(), map))
            denominator = arrayOf(Term(1))
        }
        constructor(coefficient : Number, variables : Map<Char, Number>){
            val map : MutableMap<Char, Double> = mutableMapOf()
            variables.forEach {
                map[it.key] = it.value.toDouble()
            }
            numerator = arrayOf(Term(coefficient.toDouble(), map))
            denominator = arrayOf(Term(1))
        }
        constructor(terms : Array<Term>){
            numerator = terms
            denominator = arrayOf(Term(1))
        }
        constructor(numerator : Array<Term>, denominator : Array<Term>){
            this.numerator = numerator
            this.denominator = denominator
        }


        override fun toString(): String {
            if (numerator.isZero()){
                return "0"
            } else {
                val stringNumerator = StringBuilder()
                numerator.forEach { term ->
                    if (!term.isZero())
                        stringNumerator.append(term.toString())
                }

                return if (denominator.isEmpty() || denominator.isOne()){
                    stringNumerator.toString()
                } else {
                    val stringDenominator = StringBuilder()
                    denominator.forEach { term ->
                        if (!term.isZero())
                            stringDenominator.append(term.toString())
                    }

                    """\dfrac{$stringNumerator}{$stringDenominator}"""
                }
            }
        }

        fun toLatexString() : String {
            if (numerator.isZero()){
                return "0"
            } else {
                val stringNumerator = StringBuilder()
                numerator.forEach { term ->
                    if (!term.isZero())
                        stringNumerator.append(term.toString())
                }

                return if (denominator.isEmpty() || denominator.isOne()){
                    "$${stringNumerator.toString().removePlusOnStart()}$"
                } else {
                    val stringDenominator = StringBuilder()
                    denominator.forEach { term ->
                        if (!term.isZero())
                            stringDenominator.append(term.toString())
                    }

                    """$\dfrac{${stringNumerator.toString().removePlusOnStart()}}{${stringDenominator.toString().removePlusOnStart()}}$"""
                }
            }
        }
    }

    private fun Array<Term>.sortedTerms() : Array<Term> {
        return sortedWith(compareBy<Term> { it.variables.keys.ifEmpty { setOf('z') }.first() }
            .thenByDescending { it.variables.values.ifEmpty { setOf(0.0) }.first() }).toTypedArray()
    }
    fun String.removePlusOnStart() : String {
        var equationString = this
        if (equationString.startsWith("+")){
            equationString = equationString.replaceFirst("+","")
        } else if (equationString.contains("{+")){
            equationString = equationString.replace("{+","{")
        }
        return equationString
    }
    private fun Array<Term>.removeTerm(a: Term) : Array<Term>{
        val res = mutableListOf<Term>()
        this.forEach { term ->
            if (!term.equalsTo(a)){
                res.add(term)
            }
        }
        return res.toTypedArray().sortedTerms()
    }
    fun Array<Term>.isZero() : Boolean {
        this.forEach {
            if (!it.isZero()){
                return false
            }
        }
        return true
    }
    fun Array<Term>.isOne() : Boolean {
        var gotOne = false
        this.forEach {
            if (!it.isZero()) {
                if (it.isOne()){
                    if (gotOne){
                        return false
                    } else {
                        gotOne = true
                    }
                } else {
                    return false
                }
            }
        }
        return gotOne
    }
    fun Array<Term>.equalsTo(terms: Array<Term>) : Boolean {
        var termsB = terms
        this.forEach {
            var contains = false
            for (i in termsB.indices){
                if (termsB[i].coefficient == it.coefficient && termsB[i].variables == it.variables){
                    termsB = termsB.removeTerm(it)
                    contains = true
                    break
                }
            }
            if (!contains){
                return false
            }
        }
        return termsB.isEmpty()
    }
    private operator fun Number.times(terms: Array<Term>): Array<Term> {
        val answer = mutableListOf<Term>()
        terms.forEach { term ->
            val product = this.times(term)
            if (!product.isZero()) answer.add(product)
        }
        if (answer.isEmpty()) return arrayOf(Term(0))
        return answer.toTypedArray().sortedTerms()
    }
    private operator fun Term.times(terms: Array<Term>): Array<Term> {
        val answer = mutableListOf<Term>()
        terms.forEach { term ->
            val product = this.times(term)
            if (!product.isZero()) answer.add(product)
        }
        if (answer.isEmpty()) return arrayOf(Term(0))
        return answer.toTypedArray().sortedTerms()
    }
    private operator fun Array<Term>.times(terms: Array<Term>): Array<Term> {
        val answer = mutableListOf<Term>()
        this.forEach { a ->
            terms.forEach { b ->
                val product = a.times(b)
                if (!product.isZero()) answer.add(product)
            }
        }
        if (answer.isEmpty()) return arrayOf(Term(0))
        return answer.toTypedArray().sortedTerms()
    }
    private fun Array<Term>.divideBy(number: Number): Array<Term> {
        val answer = mutableListOf<Term>()
        this.forEach { term ->
            answer.add(term.divideBy(number))
        }
        return answer.toTypedArray().sortedTerms()
    }
    private fun Term.divideByHCF(term: Term) : Term {
        val coefficient = this.coefficient/term.coefficient

        val variables = mutableMapOf<Char, Double>()
        var varsA = this.variables
        var varsB = term.variables
        varsA.forEach { varA ->
            varsB.forEach { varB ->
                if (varA.key == varB.key){
                    val exp = varA.value - varB.value
                    if (exp != 0.0) variables[varA.key] = exp
                    varsA = varsA.removeVar(varA.key)
                    varsB = varsB.removeVar(varB.key)
                }
            }
        }
        variables.putAll(varsA)
        varsB.forEach {
            val exp = -it.value
            if (exp != 0.0)
                variables[it.key] = exp
        }

        return Term(coefficient, variables.toSortedMap())
    }
    private fun Array<Term>.divideByHCF(term: Term): Array<Term> {
        val answer = mutableListOf<Term>()
        this.forEach {
            answer.add(it.divideByHCF(term))
        }
        return answer.toTypedArray().sortedTerms()
    }
    private operator fun Array<Term>.minus(terms: Array<Term>): Array<Term> {
        val answer = arrayListOf<Term>()
        var aTerms = this
        var bTerms = terms

        aTerms.forEach { termA ->
            bTerms.forEach { termB ->
                if (termA.variables == termB.variables){
                    val coefficient = termA.coefficient - termB.coefficient
                    if (coefficient != 0.0) answer.add(Term(coefficient, termA.variables))
                    aTerms = aTerms.removeTerm(termA)
                    bTerms = bTerms.removeTerm(termB)
                }
            }
        }

        answer.addAll(aTerms)
        bTerms.forEach { term ->
            answer.add((-1).times(term))
        }

        return answer.toTypedArray().sortedTerms()
    }
    private val Array<Term>.variables : Array<Map<Char, Double>>
        get() {
            val varsArray = Array(this.size){ mapOf<Char, Double>() }
            this.forEachIndexed { index, term ->
                varsArray[index] = term.variables
            }
            return varsArray
        }
    private fun Array<Term>.add(terms: Array<Term>): Array<Term> {
        val answer = arrayListOf<Term>()

        val indices = terms.indices.toMutableList()
        this.forEach { term ->
            val i = terms.variables.indexOf(term.variables)
            if (i == -1){
                answer.add(term)
            } else {
                indices.remove(i)
                val coefficient = term.coefficient + terms[i].coefficient
                if (coefficient != 0.0) answer.add(Term(coefficient, term.variables))
            }
        }
        for (i in indices) {
            answer.add(terms[i])
        }


        /*
        var aTerms = this
        var bTerms = terms
        aTerms.forEach { termA ->
            bTerms.forEach { termB ->
                if (termA.variables == termB.variables){
                    val coefficient = termA.coefficient + termB.coefficient
                    if (coefficient != 0.0) answer.add(Term(coefficient, termA.variables))
                    aTerms = aTerms.removeTerm(termA)
                    bTerms = bTerms.removeTerm(termB)
                }
            }
        }

        answer.addAll(aTerms)
        answer.addAll(bTerms)*/


        if (answer.isEmpty()) answer.add(Term(0))
        /*println("A = ${this.toList()}")
        println("B = ${terms.toList()}")
        println("answer = $answer")*/
        return answer.toTypedArray().sortedTerms()
    }
    fun Array<Term>.addLikeTerms(): Array<Term> {
        val answer = arrayListOf<Term>()
        var terms = this

        var index = 0
        while (index < this.size) {
            val termA = this[index]
            for (i in index+1 until this.size){
                val termB = this[i]
                if (termA.variables == termB.variables){
                    termA.coefficient += termB.coefficient
                    if (termA.coefficient != 0.0) answer.add(Term(termA.coefficient, termA.variables))
                    terms = terms.removeTerm(termA)
                    terms = terms.removeTerm(termB)
                    index++
                }
            }
            index++
        }
        answer.addAll(terms)

        return answer.toTypedArray().sortedTerms()
    }

    private fun Array<Term>.hcf() : Term {
        var hcfCoefficient = if (this[0].coefficient > 0) this[0].coefficient else -this[0].coefficient
        for (i in 1 until this.size){
            var hcf = hcfCoefficient
            var hcf2 = if (this[i].coefficient > 0) this[i].coefficient else -this[i].coefficient
            while (hcf != hcf2) {
                if (hcf > hcf2)
                    hcf -= hcf2
                else
                    hcf2 -= hcf
            }
            hcfCoefficient = hcf
        }

        var hcf = this[0].variables.toMutableMap()
        val negative = mutableMapOf<Char, Double>()
        this[0].variables.forEach{
            if(it.value < 0){
                negative[it.key] = it.value
            }
        }
        for(i in 1 until this.size){
            val temp = mutableMapOf<Char, Double>()
            this[i].variables.forEach{
                val variable = it.key
                val exponent = it.value

                val index = hcf.keys.indexOf(variable)

                if (index != -1){
                    val value = hcf.values.toList()[index]

                    if(value <= exponent){
                        temp[variable] = value
                    } else {
                        temp[variable] = exponent
                    }
                } else if (exponent < 0) {
                    negative[variable] = exponent
                }
            }
            hcf = temp
        }
        hcf.putAll(negative)

        return Term(hcfCoefficient,hcf.toMap())
    }
    private fun Array<Term>.lcm() : Term {
        var lcmCoefficient = if (this[0].coefficient > 0) this[0].coefficient else -this[0].coefficient
        for (i in 1 until this.size){
            var hcf = lcmCoefficient
            var hcf2 = if (this[i].coefficient > 0) this[i].coefficient else -this[i].coefficient
            val n1 = hcf
            val n2 = hcf2
            while (hcf != hcf2) {
                if (hcf > hcf2)
                    hcf -= hcf2
                else
                    hcf2 -= hcf
            }
            lcmCoefficient = (n1 * n2)/hcf
        }

        var lcm = mutableMapOf<Char, Double>()
        this[0].variables.forEach{
            if(it.value > 0){
                lcm[it.key] = it.value
            }
        }
        for(i in 1 until this.size) {
            val temp = mutableMapOf<Char, Double>()
            this[i].variables.forEach{
                val variable = it.key
                val exponent = it.value

                val index = lcm.keys.indexOf(variable)

                if (index != -1){
                    val value = lcm.values.toList()[index]

                    if(exponent >= value && exponent > 0){
                        temp[variable] = exponent
                    } else if(value > 0) {
                        temp[variable] = value
                    }
                } else if(exponent > 0) {
                    temp[variable] = exponent
                }
            }
            lcm.forEach{
                if(!temp.keys.contains(it.key)){
                    temp[it.key] = it.value
                }
            }

            lcm = temp
        }

        return Term(lcmCoefficient,lcm)
    }

    operator fun Number.times(expression: Expression): Expression {
        return if (expression.isNumber()){
            val coefficient = this.toDouble() * expression.numerator.addLikeTerms()[0].coefficient
            Expression(coefficient)
        }else if (expression.numerator.size == 1 && expression.denominator.isOne()) {
            val coefficient = this.toDouble() * expression.numerator[0].coefficient
            Expression(Term(coefficient, expression.numerator[0].variables))
        } else {
            val numerator = this.times(expression.simplify().expression.numerator).addLikeTerms()
            Expression(numerator, expression.simplify().expression.denominator.addLikeTerms()).simplify().expression
        }
    }
    operator fun Term.times(expression : Expression) : Expression {
        return if (expression.isNumber()){
            val term = expression.numerator.addLikeTerms()[0].coefficient.times(this)
            Expression(term)
        } else {
            val numerator = this.times(expression.simplify().expression.numerator).addLikeTerms()
            Expression(numerator, expression.simplify().expression.denominator.addLikeTerms()).simplify().expression
        }
    }
    operator fun Expression.times(expression: Expression): Expression {
        val numerator = this.numerator.times(expression.numerator).addLikeTerms()
        val denominator = this.denominator.times(expression.denominator).addLikeTerms()
        return Expression(numerator, denominator).simplify().expression
    }
    operator fun Expression.times(matrix: Array<Array<Expression>>): Array<Array<Expression>> {
        val res = Array(matrix.size) { Array(matrix[0].size){ Expression(0) } }
        matrix.forEachIndexed { i, row ->
            row.forEachIndexed { j, col ->
                val numerator = numerator.times(col.numerator).addLikeTerms()
                val denominator = denominator.times(col.denominator).addLikeTerms()
                res[i][j] = Expression(numerator, denominator).simplify().expression
            }
        }
        return res
    }
    operator fun Expression.times(matrixRow: Array<Expression>): Array<Expression> {
        val res = Array(matrixRow.size){ Expression(0) }
        matrixRow.forEachIndexed { j, col ->
            val numerator = numerator.times(col.numerator).addLikeTerms()
            val denominator = denominator.times(col.denominator).addLikeTerms()
            res[j] = Expression(numerator, denominator).simplify().expression
        }
        return res
    }
    fun Array<Expression>.add(matrixRow: Array<Expression>): Array<Expression>{
        val sum = Array(size){ Expression(0) }
        this.forEachIndexed { j, col ->
            sum[j] = matrixRow[j].add(col)
        }
        return sum
    }
    operator fun Array<Array<Expression>>.times(matrix: Array<Array<Expression>>): Array<Array<Expression>>{
        val rows = this.size
        val cols = matrix[0].size
        val product = Array(rows) { Array(cols) { Expression(0) } }
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                var ret = Expression(0)
                for (x in indices) {
                    ret = ret.add(this[row][x].times(matrix[x][col]))
                }
                product[row][col] = ret
            }
        }
        return  product
    }
    private fun Array<Term>.toText() : String {
        val string = StringBuilder()
        this.forEach {
            string.append(it.toString())
        }
        return string.toString()
    }
    fun Expression.isNumber() : Boolean {
        if (!this.denominator.isOne()) return false
        numerator.forEach {
            if (it.variables.isNotEmpty()) return false
        }
        return true
    }
    data class SimplifyData(val expression: Expression, val isSimplified: Boolean)
    fun Expression.simplify() : SimplifyData {
        if (denominator.isOne())  return SimplifyData(this, false)
        if (numerator.isZero()) return SimplifyData(Expression(0), false)
        if (numerator.equalsTo(denominator)) return SimplifyData(Expression(1), true)

        val hcf = numerator.plus(denominator).hcf()

        var simplified = false
        val simplifiedExpression = if (hcf.isOne()) this else {
            simplified = true
            Expression(numerator.divideByHCF(hcf), denominator.divideByHCF(hcf))
        }

        var myNumerator = simplifiedExpression.numerator
        var myDenominator = simplifiedExpression.denominator
        var inverted = false

        if (simplifiedExpression.numerator[0].variables.values.toList().ifEmpty { listOf(-1000000000.0) }[0] <
            simplifiedExpression.denominator[0].variables.values.toList().ifEmpty { listOf(-1000000000.0) }[0]){
            myNumerator = simplifiedExpression.denominator
            myDenominator = simplifiedExpression.numerator
            inverted = true
        }

        val answer = arrayListOf<Term>()
        var remainder = myNumerator
        var i = 0
        while (!remainder.isZero() && i < 10){
            val tempAns = remainder[0].divideByHCF(myDenominator[0])
            if (tempAns.variables.isNotEmpty() && tempAns.variables.values.first() < 0){
                break
            }
            answer.add(tempAns)

            val temp = tempAns.times(myDenominator)
            remainder = remainder.minus(temp)
            i++
        }

        return if (remainder.isZero()){
            if (inverted){
                SimplifyData(Expression(arrayOf(Term(1)), answer.toTypedArray()), true)
            } else {
                SimplifyData(Expression(answer.toTypedArray()), true)
            }
        } else run {
            SimplifyData(simplifiedExpression, simplified)
        }
    }
    fun Expression.divideBy(number: Number): Expression {
        return if (isNumber()){
            val coefficient = numerator.addLikeTerms()[0].coefficient / number.toDouble()
            Expression(coefficient)
        } else if (denominator.isOne()) {
            val terms = numerator.addLikeTerms().divideBy(number)
            Expression(terms)
        } else {
            val numerator = numerator.addLikeTerms()
            val denominator = number.times(this.denominator).addLikeTerms()
            Expression(numerator, denominator).simplify().expression
        }
    }
    fun Number.divideBy(expression: Expression): Expression {
        return if (expression.isNumber()){
            val coefficient = this.toDouble() / expression.numerator.addLikeTerms()[0].coefficient
            Expression(coefficient)
        } else {
            val numerator = this.times(expression.simplify().expression.denominator).addLikeTerms()
            val denominator = expression.simplify().expression.numerator.addLikeTerms()
            Expression(numerator, denominator).simplify().expression
        }
    }

    fun Expression.add(expression: Expression): Expression {
        return if (isNumber()){
            val numerator = expression.numerator.add(expression.denominator.times(numerator)).addLikeTerms()

            if (numerator.isZero()) Expression(0)
            else Expression(numerator, expression.denominator).simplify().expression
        } else if (expression.isNumber()){
            val numerator = numerator.add(denominator.times(expression.numerator)).addLikeTerms()

            if (numerator.isZero()) Expression(0)
            else Expression(numerator, denominator).simplify().expression
        } else if (denominator.isOne() && expression.denominator.isOne()){
            val numerator = expression.numerator.add(numerator)

            if (numerator.isZero()) Expression(0)
            else Expression(numerator)
        } else {
            val numerator = this.numerator.times(expression.denominator)
                .add(this.denominator.times(expression.numerator)).addLikeTerms()
            val denominator = this.denominator.times(expression.denominator).addLikeTerms()

            if (numerator.isZero()) Expression(0)
            else Expression(numerator, denominator).simplify().expression
        }
    }
    fun Expression.minus(expression: Expression): Expression {
        val numerator = this.numerator.times(expression.denominator)
            .add((-1).times(this.denominator.times(expression.numerator))).addLikeTerms()
        val denominator = this.denominator.times(expression.denominator).addLikeTerms()

        return Expression(numerator, denominator).simplify().expression
    }

    /*fun Fraction.times(fraction: Fraction) : Fraction {
        val numerator = this.numerator.times(fraction.numerator)
        val denominator = this.getDenominator().times(fraction.getDenominator())

        return Fraction(numerator, denominator)
    }

    fun Fraction.divideBy(fraction: Fraction) : Fraction {
        val numerator = this.numerator.times(fraction.getDenominator())
        val denominator = this.getDenominator().times(fraction.numerator)

        return Fraction(numerator, denominator)
    }



    fun Fraction.add(fraction: Fraction) : Fraction {
        val denominator = this.getDenominator().times(fraction.getDenominator())
        val numerator = this.numerator.times(fraction.getDenominator())
            .add(this.numerator.times(fraction.getDenominator()))

        return Fraction(numerator, denominator)
    }

    fun Term.divideBy(term: Term) : Fraction {
        val a = (this.coefficient/term.coefficient).toFractionNumber().frac
        when {
            term.variables.isEmpty() && this.variables.isEmpty() -> {
                return Fraction(Expression(a.numerator), Expression(a.denominator))
            }
            term.variables.isEmpty() -> {
                return Fraction(Expression(a.numerator, this.variables), Expression(a.denominator))
            }
            this.variables.isEmpty() -> {
                return Fraction(Expression(a.numerator), Expression(a.denominator, term.variables))
            }
            else -> {
                val numVars = mutableMapOf<Char, Double>()
                val denVars = mutableMapOf<Char, Double>()
                var varsA = this.variables
                var varsB = term.variables
                varsA.forEach { meA ->
                    varsB.forEach { meB ->
                        if (meA.key == meB.key){
                            val exp = meA.value - meB.value
                            if (exp > 0.0) {
                                numVars[meA.key] = exp
                            } else if (exp < 0.0) {
                                denVars[meB.key] = -exp
                            }
                            varsA = varsA.removeVar(meA.key)
                            varsB = varsB.removeVar(meB.key)
                        }
                    }
                }
                numVars.putAll(varsA)
                denVars.putAll(varsB)

                return Fraction(Expression(a.numerator, numVars), Expression(a.denominator, denVars))
            }
        }
    }
    fun Expression.divideBy(term: Term): Fraction {
        val fractions = arrayListOf<Fraction>()
        this.getTerms().forEach {
            fractions.add(it.divideBy(term))
        }
        var answer = Fraction()
        fractions.forEach {
            answer = answer.add(it)
        }
        return answer
    }*/
    /*fun Expression.divideBy(expression: Expression): Fraction {
        return Fraction(this, expression)
    }
    fun Expression.divide(expression: Expression): Fraction {
        val numRes = arrayListOf<Term>()
        val denRes = arrayListOf<Term>()
        var remainder = this.getTerms()

        this.getTerms().forEachIndexed { i, termI ->
            expression.getTerms().forEachIndexed { j, termJ ->
                val temp = mutableMapOf<Char, Double>()
                termI.variables.forEach { mapI ->
                    termJ.variables.forEach { mapJ ->
                        if (mapI.key == mapJ.key){
                            if (mapI.value <= mapJ.value) {
                                temp[mapI.key] = mapI.value
                            } else {
                                temp[mapJ.key] = mapJ.value
                            }
                        }
                    }
                }
            }
        }


        var index = 0
        while (!remainder.toTypedArray().equal(Term(0)) && index < num.size){
            val ans = remainder.getTerms()[0].divideBy(den[0])

            val tempExp = expression.divideBy(ans.getDenominator().getTerm(0).coefficient)
            val temp = ans.numerator.times(tempExp)

            remainder = remainder.minus(temp)

            numRes[index] = ans
            index++
        }

        return Fraction(res)
    }*/

}