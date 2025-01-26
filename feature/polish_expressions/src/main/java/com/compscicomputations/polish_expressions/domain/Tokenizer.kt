package com.compscicomputations.polish_expressions.domain

import com.compscicomputations.polish_expressions.domain.TokenName.*
import org.intellij.lang.annotations.Language

internal class ExpressionException(expr: String, message: String = ""): Exception("Invalid expression: $expr. $message")
internal class InvalidTokenException(token: Token) : Exception("The token: ${token.lexeme} is invalid.")

enum class TokenName {
    LEFT_BR,
    RIGHT_BR,
    OPERATOR,
    UNARY_OP,
    NUMBER,
    FUNCTION,
    VARIABLE,
}
data class Token(val name: TokenName, val lexeme: String) {
    constructor(name: TokenName, lexeme: Char) : this(name, lexeme.toString())
    constructor(double: Double) : this(NUMBER, double.toString())

    val precedence: Int
        get() = lexeme.precedence(name == UNARY_OP)

    override fun toString(): String = "$lexeme\t\t-> $name\t\t\t: $precedence"

    companion object {
        val LEFT_BR = Token(TokenName.LEFT_BR, '(')
        val RIGHT_BR = Token(TokenName.RIGHT_BR, ')')
    }
}

@Suppress("unused")
internal fun printTokens(tokens: List<Token>) {
    println()
    var i = 1
    for (token in tokens) {
        println("${i++}\t:\t$token")
    }
}

internal fun List<Token>.asString(filterAsterisk: Boolean = false, spaces: Boolean = true): String =
    filterNot { filterAsterisk && it.lexeme.contains('*') }.joinToString(if (spaces) " " else "") { it.lexeme }

private fun String.precedence(isUnaryOp: Boolean) = when (this) {
    "=", "≠" -> 1
    ">", "≥", "≤", "<" -> 2
    "+", "-", "—" -> if (isUnaryOp) 6 else 3
    "×", "÷", "%" -> 4
    "*" -> 5
    "^" -> 7
    "sqrt", "cbrt", "abs",
    "ln", "log",
    "sin", "cos", "tan", "csc", "cosec", "sec", "cot",
    "arcsin", "arccos", "arctan", "arccsc", "arccosec", "arcsec", "arccot",
    "sinh", "cosh", "tanh", "csch", "cosech", "sech", "coth",
    "arcsinh", "arccosh", "arctanh", "arccsch", "arccosech", "arcsech", "arccoth"
         -> 8
    else -> Int.MIN_VALUE
}
private fun String.isFunction(): Boolean = when (this) {
    "sqrt", "cbrt", "abs",
    "ln", "log",
    "sin", "cos", "tan", "csc", "cosec", "sec", "cot",
    "arcsin", "arccos", "arctan", "arccsc", "arccosec", "arcsec", "arccot",
    "sinh", "cosh", "tanh", "csch", "cosech", "sech", "coth",
    "arcsinh", "arccosh", "arctanh", "arccsch", "arccosech", "arcsech", "arccoth"
        -> true
    else -> false
}
private fun Char.isOperator(): Boolean = when (this) {
    '=', '≠', '>', '≥', '≤', '<', '+', '-', '—', '*', '×', '/', '÷', '%', '^' -> true
    else -> false
}
private fun Char.isLeftBracket(): Boolean = when (this) {
    '(', '[', '{' -> true
    else -> false
}
private fun Char.isRightBracket(): Boolean = when (this) {
    ')', ']', '}' -> true
    else -> false
}

internal fun tokenizeInfix(@Language("md") expr: String): List<Token> {
    val expression = expr.replace("[ \t\n]".toRegex(), "")
        .replace("!=", "≠")
        .replace(">=", "≥")
        .replace("<=", "≤")
        .replace('*', '×')
        .replace('/', '÷') + ' '

    val tokens = mutableListOf<Token>()

    var prevToken: TokenName? = null
    var i = 0
    while (i < expression.length - 1) {
        var char = expression[i]

        when {
            // brackets tokens
            char.isLeftBracket() -> {
                if (prevToken != null && (prevToken == NUMBER || prevToken == VARIABLE || prevToken == RIGHT_BR))
                    tokens.add(Token(OPERATOR, '*'))

                tokens.add(Token(LEFT_BR, '('))
                prevToken = LEFT_BR
                char = expression[++i] // Next Char

                if (char.isOperator() && !(char == '+' || char == '-')) throw ExpressionException(expr, "Invalid Operator")
                if (char.isRightBracket()) throw ExpressionException(expr, "Empty Bracket")
            }
            char.isRightBracket() -> {
                if (prevToken == null) throw ExpressionException(expr, "Invalid Bracket")
                if (prevToken == OPERATOR || prevToken == UNARY_OP) throw ExpressionException(expr, "Invalid Operator")
                if (prevToken == FUNCTION) throw ExpressionException(expr, "Invalid Function")

                tokens.add(Token(RIGHT_BR, ')'))
                prevToken = RIGHT_BR
                ++i
            }

            // Operator tokens
            char.isOperator() -> when {
                // Unary Operator
                (char == '+' || char == '-') && (prevToken == null || prevToken == LEFT_BR || prevToken == OPERATOR || prevToken == UNARY_OP) -> {
                    tokens.add(Token(UNARY_OP, char))
                    prevToken = UNARY_OP
                    char = expression[++i] // Next Char

                    if (char.isOperator() && !(char == '+' || char == '-')) throw ExpressionException(expr, "Invalid Operator")
                    if (char.isRightBracket()) throw ExpressionException(expr, "Empty Bracket")
                }
                else -> {
                    tokens.add(Token(OPERATOR, if (char == '-') '—' else char))
                    prevToken = OPERATOR
                    char = expression[++i] // Next Char

                    if (char.isOperator() && !(char == '+' || char == '-')) throw ExpressionException(expr, "Invalid Operator")
                    if (char.isRightBracket()) throw ExpressionException(expr, "Empty Bracket")
                }
            }

            // Number tokens
            char.isDigit() || char == '.' || char == ',' -> {
                if (prevToken != null && (prevToken == NUMBER || prevToken == VARIABLE || prevToken == RIGHT_BR))
                    tokens.add(Token(OPERATOR, '*'))

                val number = buildString {
                    append(if (char == ',') '.' else char)
                    char = expression[++i] // Next Char
                    while (char.isDigit() || char == '.' || char == ',') {
                        append(if (char == ',') '.' else char)
                        char = expression[++i] // Next Char
                    }
                }
                tokens.add(Token(NUMBER, number))
                prevToken = NUMBER
            }

            // Function and Variable tokens
            char.isLetter() -> {
                if (prevToken != null && (prevToken == NUMBER || prevToken == VARIABLE || prevToken == RIGHT_BR))
                    tokens.add(Token(OPERATOR, '*'))

                val literal = buildString {
                    append(char)
                    char = expression[++i] // Next Char
                    while (char.isLetter()) {
                        append(char)
                        char = expression[++i] // Next Char
                    }
                }
                if (literal.isFunction()) {
                    tokens.add(Token(FUNCTION, literal))
                    prevToken = FUNCTION

                    if (!char.isLeftBracket()) throw ExpressionException(expr, "Invalid Function Brackets")
                } else {
                    var l = literal.length
                    for (variable in literal) {
                        tokens.add(Token(VARIABLE, variable))
                        if (--l != 0) tokens.add(Token(OPERATOR, "*"))
                    }
                    prevToken = VARIABLE
                }
            }

            else -> throw ExpressionException(expr, "Invalid character: $char")
        }
    }

    return tokens
}

internal fun tokenizePolish(@Language("md") expr: String): List<Token> {
    val expression = expr.replace("[ \t\n]".toRegex(), "")
        .replace("!=", "≠")
        .replace(">=", "≥")
        .replace("<=", "≤")
        .replace('/', '÷') + ' '

    val tokens = mutableListOf<Token>()

    var prevToken: TokenName? = null
    var i = 0
    while (i < expression.length - 1) {
        var char = expression[i]
        when {
            char.isLeftBracket() || char.isRightBracket() -> throw ExpressionException(expr, "Brackets in polish expression.")
            char.isOperator() -> {
                tokens.add(Token(OPERATOR, if (char == '-') '—' else char))
                prevToken = OPERATOR
                char = expression[++i] // Next Char

//                if (char.isOperator() && !(char == '+' || char == '-')) throw ExpressionException(expr, "Invalid Operator")
//                if (char.isRightBracket()) throw ExpressionException(expr, "Empty Bracket")
            }
            char.isDigit() || char == '.' || char == ',' -> {
//                if (prevToken != null && (prevToken == NUMBER || prevToken == VARIABLE || prevToken == RIGHT_BR))
//                    tokens.add(Token(OPERATOR, '*'))

                val number = buildString {
                    append(if (char == ',') '.' else char)
                    char = expression[++i] // Next Char
                    while (char.isDigit() || char == '.' || char == ',') {
                        append(if (char == ',') '.' else char)
                        char = expression[++i] // Next Char
                    }
                }
                tokens.add(Token(NUMBER, number))
                prevToken = NUMBER
            }
            char.isLetter() -> {
//                if (prevToken != null && (prevToken == NUMBER || prevToken == VARIABLE || prevToken == RIGHT_BR))
//                    tokens.add(Token(OPERATOR, '*'))

                val literal = buildString {
                    append(char)
                    char = expression[++i] // Next Char
                    while (char.isLetter()) {
                        append(char)
                        char = expression[++i] // Next Char
                    }
                }
                if (literal.isFunction()) {
                    tokens.add(Token(FUNCTION, literal))
                    prevToken = FUNCTION

//                    if (!char.isLeftBracket()) throw ExpressionException(expr, "Invalid Function Brackets")
                } else {
                    var l = literal.length
                    for (variable in literal) {
                        tokens.add(Token(VARIABLE, variable))
                        if (--l != 0) tokens.add(Token(OPERATOR, "*"))
                    }
                    prevToken = VARIABLE
                }
            }
            else -> throw ExpressionException(expr, "Invalid character: $char")
        }
    }

    return tokens
}