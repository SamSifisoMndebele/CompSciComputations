package com.compscicomputations.polish_expressions.ui.trace_table

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.polish_expressions.data.model.ConvertTo
import com.compscicomputations.theme.comicNeueFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraceTablesScreen(
    viewModel: TraceTableViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .clickable { expanded = !expanded }
                    .focusable(false)
                    .padding(vertical = 4.dp),
                value = uiState.convertTo.text,
                onValueChange = {},
                textStyle = TextStyle(
                    lineBreak = LineBreak.Simple,
                    hyphens = Hyphens.Auto,
                    fontSize = 20.sp,
                    fontFamily = comicNeueFamily,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                label = { Text(text = "Convert to_") },
                readOnly = true,
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                trailingIcon = { TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ConvertTo.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.text) },
                        onClick = {
                            viewModel.setConvertTo(it)
                            expanded = false
                        }
                    )
                }
            }
        }
        HorizontalDivider()

        AnimatedVisibility(visible = uiState.convertTo.postfix || uiState.convertTo.postfixPrefix) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 8.dp, top = 8.dp)
            ) {
                item {
                    Text(
                        text = "Postfix Trace Table",
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
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
                items(uiState.postfixData) {
                    Row {
                        Text(
                            text = it.operand.toString(),
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
            }
        }

        AnimatedVisibility(visible = uiState.convertTo.prefix || uiState.convertTo.postfixPrefix) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 8.dp, top = 8.dp)
            ) {
                item {
                    Text(
                        text = "Prefix Trace Table",
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
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
                items(uiState.prefixData) {
                    Row {
                        Text(
                            text = it.operand.toString(),
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
                            textAlign = TextAlign.End,
                            maxLines = 1
                        )
                    }
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
                }
            }
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

