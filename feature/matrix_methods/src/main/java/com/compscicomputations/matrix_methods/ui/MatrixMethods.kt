package com.compscicomputations.matrix_methods.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.ui.utils.ui.OptionButton

@Composable
fun MatrixMethods(
    navigateUp: () -> Unit
) {
    CompSciScaffold(
        modifier = Modifier.fillMaxSize(),
        title = "Matrix Methods",
        navigateUp = navigateUp,
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = contentPadding
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