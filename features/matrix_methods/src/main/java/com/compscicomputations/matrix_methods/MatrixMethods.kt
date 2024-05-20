package com.compscicomputations.matrix_methods

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compscicomputations.ui.main.AppBar
import com.compscicomputations.ui.main.OptionButton

@Composable
fun MatrixMethodsScreen(navigateUp: () -> Unit) {
    Column(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp),
    ) {
        AppBar(title = "Matrix Methods", navigateUp = navigateUp) {
            //TODO Menu
        }
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item {
                OptionButton(
                    text = "Cramer's Rule",
                    onClick = { /*navigateCramer*/ }
                )
            }
            item {
                OptionButton(
                    text = "Gauss Elimination method",
                    onClick = { /*navigateGauss*/ }
                )
            }
            item {
                OptionButton(
                    text = "Method 3",
                    onClick = {  }
                )
            }
            item {
                OptionButton(
                    text = "Method 4",
                    onClick = {  }
                )
            }
            item {
                OptionButton(
                    text = "Method 5",
                    onClick = {  }
                )
            }
        }
    }
}