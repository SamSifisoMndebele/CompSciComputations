package com.compscicomputations.ui.main.polish

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TraceTablesScreen(
    modifier: Modifier = Modifier,
    postfixData: List<RowData>,
    prefixData: List<RowData>
) {
    LazyColumn(modifier) {
        item {
            Text(
                text = "Postfix Trace Table",
                Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontSize = 22.sp
            )
        }
        item {
            Row {
                Text(
                    text = "Char",
                    Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .weight(.15f)
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = "Stack",
                    Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .weight(.3f)
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = "Postfix",
                    Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .weight(.55f)
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
        items(postfixData) {
            Row {
                Text(
                    text = it.char.toString(),
                    Modifier
                        .weight(.15f)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = it.stack,
                    Modifier
                        .weight(.3f)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = .1f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    maxLines = 1
                )
                Text(
                    text = it.polish,
                    Modifier
                        .weight(.55f)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = .15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    maxLines = 1
                )
            }
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
        }
        item { Spacer(modifier = Modifier.padding(16.dp)) }
        item {
            Text(
                text = "Prefix Trace Table",
                Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontSize = 22.sp
            )
        }
        item {
            Row {
                Text(
                    text = "Char",
                    Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .weight(.15f)
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = "Stack",
                    Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .weight(.3f)
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = "Prefix",
                    Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .weight(.55f)
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
        items(prefixData) {
            Row {
                Text(
                    text = it.char.toString(),
                    Modifier.weight(.15f)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = it.stack,
                    Modifier.weight(.3f)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = .1f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    maxLines = 1
                )
                Text(
                    text = it.polish,
                    Modifier.weight(.55f)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = .15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
            }
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
        }
    }
}

/*private fun drawPostfixTable(infix: String){
    val postfix: Queue<String> = LinkedList()
    val stack: Stack<Char> = Stack()

    val num = StringBuilder()
    var prevChar = ' '

    for (char in infix){
        if (char.isWhitespace()) continue

        if (char.isLetterOrDigit() || char == '.') {
            if (prevChar == ')') {
                stack.push('×')
                val stackWithChar = stack.joinToString("")
                stack.pop()
                while (stack.isNotEmpty() && '×'.precedence() <= stack.peek().precedence()) {
                    val element = stack.pop()
                    postfix.add(element.toString())
                }
                stack.push('×')
                postfixTableDataList.add(RowData('×', stackWithChar ,  postfix.joinToString(" ")))
            }
            num.append(char)
            postfixTableDataList.add(RowData(char, stack.joinToString("") ,  postfix.joinToString(" ") + " $num"))
            prevChar = char
            continue
        }

        if (num.isNotEmpty()){
            postfix.add("$num")
            num.clear()
        }

        if (char == '(' && ((prevChar.isLetterOrDigit() || prevChar == '.') || prevChar == ')')) {
            stack.push('×')
            val stackWithChar = stack.joinToString("")
            stack.pop()
            while (stack.isNotEmpty() && '×'.precedence() <= stack.peek().precedence()) {
                val element = stack.pop()
                postfix.add(element.toString())
            }
            stack.push('×')
            postfixTableDataList.add(RowData('×', stackWithChar ,  postfix.joinToString(" ")))
        }

        if (stack.isEmpty() || char == '(') {
            stack.push(char)
            postfixTableDataList.add(RowData(char, stack.joinToString("") ,  postfix.joinToString(" ")))
            prevChar = char
            continue
        }

        if (char == ')') {
            stack.push(char)
            val stackWithBr = stack.joinToString("")
            stack.pop()
            // Need to remove stack element until the close bracket
            while (stack.isNotEmpty() && stack.peek() != '(')
            {
                // Get top element
                val element = stack.pop()
                postfix.add(element.toString())
            }
            if (stack.isNotEmpty() && stack.peek() == '(')
            {
                // Remove stack element
                stack.pop()
            }
            postfixTableDataList.add(RowData(char, stackWithBr ,  postfix.joinToString(" ")))

            prevChar = char
            continue
        }
        prevChar = char

        // Remove stack element until precedence of
        // top is greater than current infix operator
        stack.push(char)
        val stackWithChar = stack.joinToString("")
        stack.pop()
        while (stack.isNotEmpty() && char.precedence() <= stack.peek().precedence()) {
            // Get top element
            val element = stack.pop()
            postfix.add(element.toString())
        }
        // Add new operator
        stack.push(char)
        postfixTableDataList.add(RowData(char, stackWithChar,  postfix.joinToString(" ")))
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
    postfixTableDataList.add(RowData(' ' , "", postfix.joinToString(" ")))
}

private fun drawPrefixTable(infix: String){
    val infixRev = infix.reversed()
    val prefix: Queue<String> = LinkedList()
    val stack: Stack<Char> = Stack()

    val num = StringBuilder()
    var prevChar = ' '

    for (char in infixRev){
        if (char.isWhitespace()) continue

        if (char.isLetterOrDigit() || char == '.') {
            if (prevChar == '(') {
                stack.push('×')
                val stackWithChar = stack.joinToString("")
                stack.pop()
                while (stack.isNotEmpty() && '×'.precedence() < stack.peek().precedence()) {
                    val element = stack.pop()
                    prefix.add(element.toString())
                }
                stack.push('×')
                prefixTableDataList.add(
                    RowData('×', stackWithChar ,
                        prefix.joinToString(" ").reversed())
                )
            }
            num.append(char)
            prefixTableDataList.add(
                RowData(char, stack.joinToString("") ,
                    "$num " +prefix.joinToString(" ").reversed())
            )
            prevChar = char
            continue
        }

        if (num.isNotEmpty()){
            prefix.add("$num")
            num.clear()
        }

        if (char == ')' && ((prevChar.isLetterOrDigit() || prevChar == '.') || prevChar == '(')) {
            stack.push('×')
            val stackWithChar = stack.joinToString("")
            stack.pop()
            while (stack.isNotEmpty() && '×'.precedence() < stack.peek().precedence()) {
                val element = stack.pop()
                prefix.add(element.toString())
            }
            stack.push('×')
            prefixTableDataList.add(RowData('×', stackWithChar ,  prefix.joinToString(" ")))
        }

        if ( stack.isEmpty() || char == ')') {
            stack.push(char)
            prefixTableDataList.add(
                RowData(char, stack.joinToString("") ,
                    prefix.joinToString(" ").reversed())
            )
            prevChar = char
            continue
        }

        if (char == '(') {
            stack.push(char)
            val stackWithBr = stack.joinToString("")
            stack.pop()
            // Need to remove stack element until the close bracket
            while (stack.isNotEmpty() && stack.peek() != ')')
            {
                // Get top element
                val element = stack.pop()
                prefix.add(element.toString())
            }
            if (stack.peek() == ')')
            {
                // Remove stack element
                stack.pop()
            }
            prefixTableDataList.add(
                RowData(char, stackWithBr ,
                    prefix.joinToString(" ").reversed())
            )
            prevChar = char
            continue
        }
        prevChar = char

        // Remove stack element until precedence of
        // top is greater than current infix operator
        stack.push(char)
        val stackWithBr = stack.joinToString("")
        stack.pop()
        while (stack.isNotEmpty() && char.precedence() < stack.peek().precedence()) {
            // Get top element
            val element = stack.pop()
            prefix.add(element.toString())
        }
        // Add new operator
        stack.push(char)
        prefixTableDataList.add(
            RowData(char, stackWithBr ,
                prefix.joinToString(" ").reversed())
        )
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
    prefixTableDataList.add(
        RowData(' ', stack.joinToString(" ") ,
            prefix.joinToString(" ").reversed())
    )
}*/

