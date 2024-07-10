package com.compscicomputations.feature.polish_expressions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
@Composable
fun PolishExpressionsScreen(
    navigateUp: () -> Unit,
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf("Conversion", "Tree Diagram", "Trace Table")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
//            AppBar(
//                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp),
//                title = "Polish Expressions", navigateUp = navigateUp) {
//                //TODO Menu
//            }
        },
        bottomBar = {

            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(when(index) {
                            0 -> painterResource(id = R.drawable.ic_abc)
                            1 -> painterResource(id = R.drawable.ic_abc)
                            2 -> painterResource(id = R.drawable.ic_abc)
                            else -> painterResource(id = R.drawable.ic_abc) },
                            contentDescription = item,
//                            tint = Color.White
                        )
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        },
    ) { innerPadding ->
        val padding = PaddingValues(start = 8.dp, end = 8.dp,
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding())
        val postfixData = listOf(
            RowData('A', "", "A"),
            RowData('+', "+", "A"),
            RowData('(', "+(", "AB"),
            RowData('B', "+(", "AB"),
            RowData('-', "+(-", "AB"),
            RowData('C', "+(-", "ABC"),
            RowData(')', "+", "ABC-"),
            RowData('*', "+*", "ABC-"),
            RowData('A', "+*", "ABC-A"),
            RowData('-', "-", "ABC-A*+"),
            RowData('C', "-", "ABC-A*+C"),
            RowData(' ', "", "ABC-A*+C-"),
        )
        val prefixData = listOf(
            RowData('C', "", "C"),
            RowData('-', "-", "C"),
            RowData('A', "-", "AC"),
            RowData('*', "-*", "AC"),
            RowData(')', "-*)", "AC"),
            RowData('C', "-*)", "CAC"),
            RowData('-', "-*)-", "CAC"),
            RowData('B', "-*)-", "BCAC"),
            RowData('(', "-*", "-BCAC"),
            RowData('+', "-+", "*-BCAC"),
            RowData('A', "-+", "A*-BCAC"),
            RowData(' ', "", "-+A*-BCAC"),
        )

        when(selectedItem){
            0 -> {
                Text(modifier = Modifier.padding(padding), text = "Conversion")
            }
            1 -> {
                Text(modifier = Modifier.padding(padding), text = "Tree Diagram")
            }
            2 -> TraceTablesScreen(modifier = Modifier.padding(padding),
                postfixData = postfixData, prefixData = prefixData)
        }
    }
}