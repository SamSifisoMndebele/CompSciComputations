package com.compscicomputations.matrix_methods.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.compscicomputations.R
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
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {

                    }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Clear fields")
                    }
                    IconButton(onClick = {

                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.List,
                            contentDescription = "Response List",
                        )
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text("Generate Steps") },
                        icon = {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.ic_ai),
                                contentDescription = null
                            )
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        onClick = {

                        }
                    )
                },
            )
        }
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
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Cramer's Rule",
                    iconVector = null,
                    onClick = { /*navigateCramer*/ }
                )
            }
            item {
                OptionButton(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Gauss Elimination method",
                    iconVector = null,
                    onClick = { /*navigateGauss*/ }
                )
            }
            item {
                OptionButton(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Method 3",
                    iconVector = null,
                    onClick = {  }
                )
            }
            item {
                OptionButton(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Method 4",
                    iconVector = null,
                    onClick = {  }
                )
            }
            item {
                OptionButton(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Method 5",
                    iconVector = null,
                    onClick = {  }
                )
            }
        }
    }
}