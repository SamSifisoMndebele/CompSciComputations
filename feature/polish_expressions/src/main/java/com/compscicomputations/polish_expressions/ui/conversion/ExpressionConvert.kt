package com.compscicomputations.polish_expressions.ui.conversion

import com.compscicomputations.polish_expressions.utils.ExpSigned
import java.util.EmptyStackException
import java.util.LinkedList
import java.util.Queue
import java.util.Stack
import kotlin.math.pow

object ExpressionConvert {

    internal fun Char.precedence(): Int {
        return when (this){
            '=','≠' -> 1
            '>','≥','≤','<' -> 2
            '+','—','-' -> 3
            '*','×','/','÷','%'-> 4
            '^'  -> 5
            else -> 0
        }
    }

    private fun Double.toNumber() : Number{
        return if (this.toLong().toDouble() != this) this else this.toLong()
    }

    //Calculate an Expression
    fun String.evaluatePostfix(): String {
        val answer = StringBuilder()
        val num = StringBuilder()
        val stack: Stack<Number> = Stack()
        var postfix = this

        answer.append("Evaluate Postfix\n    $this\n")

        for (char in this) {
            if(char.isLetter()){
                throw Exception("Expression with letters can not be evaluated. Enter numbers only.")
            }
            if (char.isDigit() || char == '.'){
                num.append(char)
                continue
            }
            if (num.isNotEmpty()){
                val double = num.toString().toDouble()
                if (double.toInt() < double){
                    stack.push(double)
                } else {
                    stack.push(double.toInt())
                }
                num.clear()
            }
            if (char.isWhitespace()) continue

            val b = stack.pop().toDouble()
            val a = stack.pop().toDouble()

            when (char) {
                '+' -> stack.push((a + b).toNumber())
                '—', '-' -> stack.push((a - b).toNumber())
                '%'-> stack.push((a % b).toNumber())
                '×', '*' -> stack.push((a * b).toNumber())
                '÷', '/' -> stack.push((a / b).toNumber())
                '^' -> stack.push((a.pow(b)).toNumber())

                '=' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a == b}\n".uppercase()}\n")

                    return answer.toString()
                }
                '≠' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a != b}\n".uppercase()}\n")

                    return answer.toString()
                }
                '>' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a > b}\n".uppercase()}\n")

                    return answer.toString()
                }
                '<' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a < b}\n".uppercase()}\n")

                    return answer.toString()
                }
                '≥' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a >= b}\n".uppercase()}\n")

                    return answer.toString()
                }
                '≤' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a <= b}\n".uppercase()}\n")

                    return answer.toString()
                }
                else -> {
                    throw Exception("Unknown operator $char")
                }
            }

            postfix = postfix.replace("${a.toNumber()} ${b.toNumber()} $char" , "(${a.toNumber()}$char${b.toNumber()})")
            answer.append("⇒ $postfix\n")
            postfix = postfix.replace("(${a.toNumber()}$char${b.toNumber()})" , "${stack.peek()}")
            answer.append("⇒ $postfix\n")
        }

        return answer.toString()
    }
    fun String.evaluatePrefix(): String {
        val answer = StringBuilder()
        val num = StringBuilder()
        val stack: Stack<Number> = Stack()
        var prefix = this

        answer.append("\nEvaluate Prefix\n    $this\n")

        for (char in this.reversed()) {
            if(char.isLetter()){
                throw Exception("Expression with letters can not be evaluated. Enter numbers only.")
            }
            if (char.isDigit() || char == '.'){
                num.append(char)
                continue
            }
            if (num.isNotEmpty()){
                val double = num.toString().reversed().toDouble()
                if (double.toInt() < double){
                    stack.push(double)
                } else {
                    stack.push(double.toInt())
                }
                num.clear()
            }
            if (char.isWhitespace()) continue

            val a = stack.pop().toDouble()
            val b = stack.pop().toDouble()

            when (char) {
                '%'-> stack.push((a % b).toNumber())
                '+' -> stack.push((a + b).toNumber())
                '—', '-' -> stack.push((a - b).toNumber())
                '×', '*' -> stack.push((a * b).toNumber())
                '^' -> stack.push((a.pow(b)).toNumber())
                '÷', '/' -> stack.push((a / b).toNumber())

                '=' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a == b}\n".uppercase()}\n")

                    return answer.toString()
                }
                '≠' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a != b}\n".uppercase()}\n")

                    return answer.toString()
                }
                '>' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a > b}\n".uppercase()}\n")

                    return answer.toString()
                }
                '<' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a < b}\n".uppercase()}\n")

                    return answer.toString()
                }
                '≥' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a >= b}\n".uppercase()}\n")

                    return answer.toString()
                }
                '≤' -> {
                    answer.append("⇒ (${a.toNumber()}$char${b.toNumber()})\n")
                    answer.append("∴ ${a.toNumber()}$char${b.toNumber()} is ${"${a <= b}\n".uppercase()}\n")

                    return answer.toString()
                }
                else -> {
                    throw Exception("Unknown operator $char")
                }
            }

            println("$char ${a.toNumber()} ${b.toNumber()}")
            prefix = prefix.replace("$char ${a.toNumber()} ${b.toNumber()}" , "(${a.toNumber()}$char${b.toNumber()})")
            answer.append("⇒ $prefix\n")
            prefix = prefix.replace("(${a.toNumber()}$char${b.toNumber()})" , "${stack.peek()}")
            answer.append("⇒ $prefix\n")
        }

        answer.append("\n∴ Answer = ${stack.pop()}\n")

        return answer.toString()
    }

    //Convert Infix to Postfix
    fun String.infixToPostfix(): String {
        val infix = this
        val postfix: Queue<String> = LinkedList()
        val stack: Stack<Char> = Stack()

        val num = StringBuilder()

        var prevChar = ' '
        for (char in infix){
            if (char.isWhitespace()) continue

            if (char.isLetterOrDigit() || char == '.') {
                if (prevChar == ')') {
                    while (stack.isNotEmpty() && '×'.precedence() <= stack.peek().precedence()) {
                        val element = stack.pop()
                        postfix.add(element.toString())
                    }
                    stack.push('×')
                }
                num.append(char)
                prevChar = char
                continue
            }

            if (num.isNotEmpty()){
                postfix.add("$num")
                num.clear()
            }

            if (char == '(' && (prevChar.isLetterOrDigit() || prevChar == '.' || prevChar == ')')) {
                while (stack.isNotEmpty() && '×'.precedence() < stack.peek().precedence()) {
                    val element = stack.pop()
                    postfix.add(element.toString())
                }
                stack.push('×')
            }

            if (stack.isEmpty() || char == '(') {
                stack.push(char)
                prevChar = char
                continue
            }

            if (char == ')') {
                // Need to remove stack element until the close bracket
                while (stack.isNotEmpty() && stack.peek() != '(')
                {
                    // Get top element
                    postfix.add("${stack.pop()}")
                    //result += stack.peek()
                    // Remove stack element
                    //stack.pop()
                }
                if (stack.isNotEmpty() && stack.peek() == '(')
                {
                    // Remove stack element
                    stack.pop()
                }
                prevChar = char
                continue
            }
            prevChar = char

            while (stack.isNotEmpty() && char.precedence() <= stack.peek().precedence()) {
                // Get top element
                postfix.add("${stack.pop()}")
            }
            // Add new operator
            stack.push(char)
        }

        if (num.isNotEmpty()){
            postfix.add("$num")
            num.clear()
        }

        while (stack.isNotEmpty()) {
            val e = stack.pop()
            if (e == '(' || e == ')') continue
            postfix.add("$e")
        }

        return postfix.joinToString("")
    }

    //Convert Infix to Prefix
    fun String.infixToPrefix(): String {
        val infixRev = this.reversed()
        val prefix: Queue<String> = LinkedList()
        val stack: Stack<Char> = Stack()

        val num = StringBuilder()
        var prevChar = ' '

        for (char in infixRev){
            if (char.isWhitespace()) continue

            if (char.isLetterOrDigit() || char == '.') {
                if (prevChar == '(') {
                    while (stack.isNotEmpty() && '×'.precedence() < stack.peek().precedence()) {
                        val element = stack.pop()
                        prefix.add(element.toString())
                    }
                    stack.push('×')
                }
                num.append(char)
                prevChar = char
                continue
            }

            if (num.isNotEmpty()){
                prefix.add("$num")
                num.clear()
            }

            if (char == ')' && ((prevChar.isLetterOrDigit() || prevChar == '.') || prevChar == '(')) {
                while (stack.isNotEmpty() && '×'.precedence() < stack.peek().precedence()) {
                    val element = stack.pop()
                    prefix.add(element.toString())
                }
                stack.push('×')
            }

            if (stack.isEmpty() || char == ')') {
                stack.push(char)
                prevChar = char
                continue
            }

            if (char == '(') {
                // Need to remove stack element until the close bracket
                while (stack.isNotEmpty() && stack.peek() != ')')
                {
                    // Get top element
                    prefix.add("${stack.pop()}")
                    //result += stack.peek()
                    // Remove stack element
                    //stack.pop()
                }
                if (stack.peek() == ')')
                {
                    // Remove stack element
                    stack.pop()
                }
                prevChar = char
                continue
            }
            prevChar = char

            while (stack.isNotEmpty() && char.precedence() < stack.peek().precedence()) {
                // Get top element
                prefix.add("${stack.pop()}")
            }
            // Add new operator
            stack.push(char)
        }

        if (num.isNotEmpty()){
            prefix.add("$num")
            num.clear()
        }

        while (stack.isNotEmpty()) {
            val e = stack.pop()
            if (e == '(' || e == ')') continue
            prefix.add("$e")
        }

        return prefix.joinToString("").reversed()
    }

    //Convert Postfix to Infix
    fun String.postfixToInfix(): String {
        val postfix = this
        val stack: Stack<ExpSigned> = Stack()

        val num = StringBuilder()
        var prevChar = '0'

        for (char in postfix) {
            if (char.isLetterOrDigit() || char == '.') {
                num.append(char)

                if (num.isNotEmpty() && prevChar.isLetter() && char.isLetter()){
                    stack.push(ExpSigned("$prevChar"))
                    stack.push(ExpSigned(char.toString()))
                    num.clear()
                }
                prevChar = char
                continue
            }
            prevChar = '0'
            if (num.isNotEmpty()){
                stack.push(ExpSigned(num.toString()))
                num.clear()
            }
            if (char.isWhitespace()) continue

            val right = stack.pop()
            val left = try { stack.pop() } catch (e: EmptyStackException) { ExpSigned("") }

            if (left.operator != null && right.operator != null){
                if (char.precedence() > left.operator!!.precedence() && char.precedence() > right.operator!!.precedence()) {
                    stack.push(ExpSigned("(${left.string})$char(${right.string})", char))
                } else if (char.precedence() > left.operator!!.precedence()){
                    stack.push(ExpSigned("(${left.string})$char${right.string}", char))
                } else if (char.precedence() > right.operator!!.precedence()){
                    stack.push(ExpSigned("${left.string}$char(${right.string})", char))
                } else {
                    stack.push(ExpSigned("${left.string}$char${right.string}", char))
                }
            } else if (left.operator != null && char.precedence() > left.operator!!.precedence()) {
                stack.push(ExpSigned("(${left.string})$char${right.string}", char))
            } else if (right.operator != null && char.precedence() > right.operator!!.precedence()){
                stack.push(ExpSigned("${left.string}$char(${right.string})", char))
            } else {
                stack.push(ExpSigned("${left.string}$char${right.string}", char))
            }
        }

        val infix = StringBuilder()
        stack.forEach {
            infix.append(it.string)
        }

        return infix.toString()
    }

    //Convert Postfix to Prefix
    fun String.postfixToPrefix(): String = postfixToInfix().infixToPrefix()

    //Convert Prefix to Infix
    fun String.prefixToInfix(): String {
        val prefixRev = reversed()
        val stack: Stack<ExpSigned> = Stack()//ds

        val num = StringBuilder() //ds
        var prevChar = '0' //d

        for (char in prefixRev) {
            if (char.isLetterOrDigit() || char == '.'){
                num.append(char)

                if (num.isNotEmpty() && prevChar.isDigit() && char.isDigit()){
                    stack.push(ExpSigned("$prevChar"))
                    stack.push(ExpSigned(char.toString()))
                    num.clear()
                }
                prevChar = char
                continue
            }
            prevChar = '0'
            if (num.isNotEmpty()) {
                stack.push(ExpSigned(num.toString()))
                num.clear()
            }
            if (char.isWhitespace()) continue

            val right = stack.pop()
            val left = stack.pop()

            when {
                left.operator != null && right.operator != null -> when {
                    char.precedence() > left.operator!!.precedence() && char.precedence() > right.operator!!.precedence() -> stack.push(
                        ExpSigned(")${left.string}($char)${right.string}(", char)
                    )
                    char.precedence() > left.operator!!.precedence() -> stack.push(ExpSigned(")${left.string}($char${right.string}", char))
                    char.precedence() > right.operator!!.precedence() -> stack.push(ExpSigned("${left.string}$char)${right.string}(", char))
                    else -> stack.push(ExpSigned("${left.string}$char${right.string}", char))
                }
                left.operator != null && char.precedence() > left.operator!!.precedence() -> stack.push(
                    ExpSigned(")${left.string}($char${right.string}", char)
                )
                right.operator != null && char.precedence() > right.operator!!.precedence() -> stack.push(
                    ExpSigned("${left.string}$char)${right.string}(", char)
                )
                else -> stack.push(ExpSigned("${left.string}$char${right.string}", char))
            }
        }

        val infix = StringBuilder()
        stack.forEach {
            infix.append(it.string)
        }
        return infix.toString().reversed()
    }

    //Convert Prefix to Postfix
    fun String.prefixToPostfix(): String = prefixToInfix().infixToPostfix()
}