package com.compscicomputations.polish_expressions.domain

import androidx.datastore.preferences.protobuf.Empty
import com.compscicomputations.polish_expressions.domain.TokenName.FUNCTION
import com.compscicomputations.polish_expressions.domain.TokenName.LEFT_BR
import com.compscicomputations.polish_expressions.domain.TokenName.NUMBER
import com.compscicomputations.polish_expressions.domain.TokenName.OPERATOR
import com.compscicomputations.polish_expressions.domain.TokenName.RIGHT_BR
import com.compscicomputations.polish_expressions.domain.TokenName.UNARY_OP
import com.compscicomputations.polish_expressions.domain.TokenName.VARIABLE
import java.util.EmptyStackException
import java.util.LinkedList
import java.util.Queue
import java.util.Stack
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * Convert an Infix expression to a Postfix expression
 * @receiver Infix tokens
 * @return Postfix tokens
 */
internal fun List<Token>.infixToPostfix(): PostfixResults {
    val table = mutableListOf<RowData>()
    val postfix: Queue<Token> = LinkedList()
    val stack: Stack<Token> = Stack()

    var prevToken: Token? = null
    for ((i, token) in this.withIndex()) {
        when(token.name) {
            NUMBER, VARIABLE -> postfix.offer(token)
            LEFT_BR, FUNCTION -> stack.push(token)
            RIGHT_BR -> {
                while (stack.isNotEmpty() && stack.peek().name != LEFT_BR)
                    postfix.offer(stack.pop())
                try { stack.pop() } catch (e: EmptyStackException) {
                    throw ExpressionException(this.joinToString(" ") { it.lexeme }, "Brackets are not balanced.")
                }

                if (stack.isNotEmpty() && stack.peek().name == FUNCTION)
                    postfix.offer(stack.pop())
            }
            OPERATOR -> {
                while (stack.isNotEmpty() && token.precedence <= stack.peek().precedence)
                    postfix.offer(stack.pop())

                stack.push(token)

                try { this[i + 1] } catch (e: IndexOutOfBoundsException) {
                    throw ExpressionException(this.joinToString(" ") { it.lexeme })
                }
            }
            UNARY_OP -> {
                if (prevToken != null && (prevToken.lexeme == "^" || prevToken.name == UNARY_OP)) {
                    stack.push(token)
                } else {
                    while (stack.isNotEmpty() && token.precedence <= stack.peek().precedence)
                        postfix.offer(stack.pop())

                    stack.push(token)
                }
            }
        }
        prevToken = token
        table.add(RowData(token.lexeme, stack.joinToString(" ") { it.lexeme }, postfix.joinToString(" ") { it.lexeme }))
    }

    while (stack.isNotEmpty()) {
        val token = stack.pop()
        if (token.name == LEFT_BR || token.name == RIGHT_BR) continue
        postfix.add(token)
        table.add(RowData("", stack.joinToString(" ") { it.lexeme }, postfix.joinToString(" ") { it.lexeme }))
    }

    return PostfixResults(postfix.toList(), table)
}

/**
 * Convert a Postfix expression to an Infix expression
 * @receiver Postfix tokens
 * @return Infix tokens
 */
internal fun List<Token>.postfixToInfix(): List<Token> {
    when (this[0].name) {
        OPERATOR, UNARY_OP -> throw ExpressionException("Postfix cannot start with operator")
        else -> {}
    }
    if (this.contains(Token.LEFT_BR) || contains(Token.RIGHT_BR)) throw ExpressionException("Postfix cannot contain brackets")

    val infix = Stack<InfixData>()

    for (token in this) whenToken(false, token, infix)

    return infix.pop().tokens
}

/**
 * Convert an Infix expression to a Prefix expression
 * @receiver Infix tokens
 * @return Prefix tokens
 */
internal fun List<Token>.infixToPrefix(): PrefixResults {
    val table = mutableListOf<RowData>()
    val prefix: Queue<Token> = LinkedList()
    val stack: Stack<Token> = Stack()

    var prevToken: Token? = null
    for ((i, token) in this.reversed().withIndex()) {
        when(token.name) {
            NUMBER, VARIABLE -> prefix.offer(token)
            RIGHT_BR, FUNCTION -> stack.push(token)
            LEFT_BR -> {
                while (stack.isNotEmpty() && stack.peek().name != RIGHT_BR)
                    prefix.offer(stack.pop())
                try { stack.pop() } catch (e: EmptyStackException) {
                    throw ExpressionException(this.joinToString(" ") { it.lexeme }, "Brackets are not balanced.")
                }

                if (stack.isNotEmpty() && stack.peek().name == FUNCTION)
                    prefix.offer(stack.pop())
            }
            OPERATOR -> {
                if (prevToken != null && prevToken.lexeme == "-") {
                    prefix.offer(stack.pop())
                    stack.push(token)
                } else {
                    while (stack.isNotEmpty() && token.precedence < stack.peek().precedence)
                        prefix.offer(stack.pop())

                    stack.push(token)

                    try { this[i + 1] } catch (e: IndexOutOfBoundsException) {
                        throw ExpressionException(this.joinToString(" ") { it.lexeme })
                    }
                }
            }
            UNARY_OP -> {
                while (stack.isNotEmpty() && token.precedence < stack.peek().precedence)
                    prefix.offer(stack.pop())

                stack.push(token)
            }
        }
        prevToken = token
    }

    while (stack.isNotEmpty()) {
        val token = stack.pop()
        if (token.name == LEFT_BR || token.name == RIGHT_BR) continue
        prefix.add(token)
    }

    return PrefixResults(prefix.reversed(), table)
}

/**
 * Convert a Prefix expression to an Infix expression
 * @receiver Prefix tokens
 * @return Infix tokens
 */
internal fun List<Token>.prefixToInfix(): List<Token> {
    val prefix = reversed()
    when (prefix[0].name) {
        OPERATOR, UNARY_OP -> throw ExpressionException("Prefix cannot end with operator")
        else -> {}
    }
    if (prefix.contains(Token.LEFT_BR) || prefix.contains(Token.RIGHT_BR)) throw ExpressionException("Prefix cannot contain brackets")

    val infix = Stack<InfixData>()

    for (token in prefix) whenToken(true, token, infix)

    return infix.pop().tokens
}

/**
 * Convert a Postfix expression to a Prefix expression
 * @receiver Postfix tokens
 * @return Prefix tokens
 */
internal fun List<Token>.postfixToPrefix(): PrefixResults = postfixToInfix().infixToPrefix()

/**
 * Convert a Prefix expression to a Postfix expression
 * @receiver Prefix tokens
 * @return Postfix tokens
 */
internal fun List<Token>.prefixToPostfix(): PostfixResults = prefixToInfix().infixToPostfix()


/**
 * Evaluate a Postfix expression to a number
 * @receiver Postfix tokens
 * @param variables a map of variables to actual values, `mapOf('x' to 2)` means `x = 2`. All values default to 0.
 * @return The value of expression [Double]
 */
internal fun List<Token>.evaluatePostfix(variables: Map<Char, Number> = mapOf()): Double {
    try {
        val stack = Stack<Token>()

        for (token in this) when(token.name) {
            LEFT_BR, RIGHT_BR -> throw InvalidTokenException(token)
            NUMBER, VARIABLE -> stack.push(token)
            OPERATOR -> whenOperator(false, stack, token.lexeme, variables)
            UNARY_OP, FUNCTION -> whenUnaryOpOrFun(stack, token.lexeme, variables)
        }

        return stack.pop().lexeme.toDouble()
    } catch (e: Exception) {
        return Double.NaN
    }
}

/**
 * Evaluate a Prefix expression to a number
 * @receiver Prefix tokens
 * @param variables a map of variables to actual values, `mapOf('x' to 2)` means `x = 2`. All values default to 0.
 * @return The value of expression [Double]
 */
internal fun List<Token>.evaluatePrefix(variables: Map<Char, Number> = mapOf()): Double {
    try {
        val stack = Stack<Token>()

        for (token in this.reversed()) when(token.name) {
            LEFT_BR, RIGHT_BR -> throw InvalidTokenException(token)
            NUMBER, VARIABLE -> stack.push(token)
            OPERATOR -> whenOperator(true, stack, token.lexeme, variables)
            UNARY_OP, FUNCTION -> whenUnaryOpOrFun(stack, token.lexeme, variables)
        }

        return stack.pop().lexeme.toDouble()
    } catch (e: Exception) {
        return Double.NaN
    }
}

data class RowData(val token: String, val stack: String, val polish : String)
data class PostfixResults(val postfix: List<Token>, val table: List<RowData>)
data class PrefixResults(val prefix: List<Token>, val table: List<RowData>)
private class InfixData(val tokens: List<Token>, val precedence : Int) {
    constructor(token: Token): this(listOf(token), Int.MAX_VALUE)
}
private fun whenToken(startLeft: Boolean, token: Token, infix: Stack<InfixData>) {
    when (token.name) {
        LEFT_BR, RIGHT_BR -> throw InvalidTokenException(token)
        NUMBER, VARIABLE -> infix.push(InfixData(token))
        OPERATOR -> {
            val left: InfixData
            val right: InfixData
            if (startLeft) {
                left = infix.pop()
                right = infix.pop()
            } else {
                right = infix.pop()
                left = infix.pop()
            }

            val tokens = ArrayList<Token>()
            when {
                left.precedence < token.precedence && right.precedence < token.precedence -> {
                    tokens.add(Token.LEFT_BR)
                    tokens.addAll(left.tokens)
                    tokens.add(Token.RIGHT_BR)
                    tokens.add(token)
                    tokens.add(Token.LEFT_BR)
                    tokens.addAll(right.tokens)
                    tokens.add(Token.RIGHT_BR)
                }
                left.precedence < token.precedence -> {
                    tokens.add(Token.LEFT_BR)
                    tokens.addAll(left.tokens)
                    tokens.add(Token.RIGHT_BR)
                    tokens.add(token)
                    tokens.addAll(right.tokens)
                }
                right.precedence < token.precedence -> {
                    tokens.addAll(left.tokens)
                    tokens.add(token)
                    tokens.add(Token.LEFT_BR)
                    tokens.addAll(right.tokens)
                    tokens.add(Token.RIGHT_BR)
                }
                else -> {
                    tokens.addAll(left.tokens)
                    tokens.add(token)
                    tokens.addAll(right.tokens)
                }
            }
            infix.push(InfixData(tokens, token.precedence))
        }
        UNARY_OP -> {
            val right = infix.pop()

            val tokens = ArrayList<Token>()
            if (right.precedence < token.precedence) {
                tokens.add(token)
                tokens.add(Token.LEFT_BR)
                tokens.addAll(right.tokens)
                tokens.add(Token.RIGHT_BR)
            } else {
                tokens.add(token)
                tokens.addAll(right.tokens)
            }
            infix.push(InfixData(tokens, token.precedence))
        }
        FUNCTION -> {
            val argument = infix.pop()

            val tokens = ArrayList<Token>()
            tokens.add(token)
            tokens.add(Token.LEFT_BR)
            tokens.addAll(argument.tokens)
            tokens.add(Token.RIGHT_BR)

            infix.push(InfixData(tokens, token.precedence))
        }
    }
}
private fun whenOperator(startLeft: Boolean, stack: Stack<Token>, lexeme: String, variables: Map<Char, Number>) {
    val left: Token
    val right: Token
    if (startLeft) {
        left = stack.pop()
        right = stack.pop()
    } else {
        right = stack.pop()
        left = stack.pop()
    }

    val doubleLeft = if (left.name == VARIABLE) variables[left.lexeme[0]]?.toDouble() ?: 0.0 else left.lexeme.toDouble()
    val doubleRight = if (right.name == VARIABLE) variables[right.lexeme[0]]?.toDouble() ?: 0.0 else right.lexeme.toDouble()

    when (lexeme) {
        "+" -> stack.push(Token(doubleLeft + doubleRight))
        "—" -> stack.push(Token(doubleLeft - doubleRight))
        "×", "*" -> stack.push(Token(doubleLeft * doubleRight))
        "%" -> stack.push(Token(doubleLeft % doubleRight))
        "^" -> stack.push(Token(doubleLeft.pow(doubleRight)))
        "÷", "/" -> stack.push(Token(doubleLeft / doubleRight))
        else -> stack.push(Token(0.0))
    }
}
private fun whenUnaryOpOrFun(stack: Stack<Token>, lexeme: String, variables: Map<Char, Number>) {
    val right = stack.pop()
    val doubleRight = if (right.name == VARIABLE) variables[right.lexeme[0]]?.toDouble() ?: 0.0 else right.lexeme.toDouble()

    when (lexeme) {
        "+" -> stack.push(Token(doubleRight))
        "-" -> stack.push(Token(-doubleRight))

        "sin" -> stack.push(Token(sin(doubleRight)))
        "cos" -> stack.push(Token(cos(doubleRight)))
        "tan" -> stack.push(Token(tan(doubleRight)))
        "csc" -> stack.push(Token(1 / sin(doubleRight)))
        "sec" -> stack.push(Token(1 / cos(doubleRight)))
        "cot" -> stack.push(Token(1 / tan(doubleRight)))

        "√", "sqrt" -> stack.push(Token(sqrt(doubleRight)))
        "ln" -> stack.push(Token(ln(doubleRight)))
        "log" -> {
            val baseToken = stack.pop()
            val base = if (baseToken.name == VARIABLE) variables[baseToken.lexeme[0]]?.toDouble() ?: 0.0 else baseToken.lexeme.toDouble()
            stack.push(Token(log(doubleRight, base)))
        }
        else -> stack.push(Token(0.0))
    }
}