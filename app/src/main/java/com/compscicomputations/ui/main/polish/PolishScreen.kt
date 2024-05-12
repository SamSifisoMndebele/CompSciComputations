package com.compscicomputations.ui.main.polish

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
import com.compscicomputations.R
import com.compscicomputations.ui.main.AppBar

@Composable
fun PolishScreen(
    navigateUp: () -> Unit,
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf("Conversion", "Tree Diagram", "Table")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp),
                title = "Polish Expressions", navigateUp = navigateUp) {
                //TODO Menu
            }
        },
        bottomBar = {

            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(when(index) {
                            0 -> painterResource(id = R.drawable.ic_num_system)
                            1 -> painterResource(id = R.drawable.ic_excess)
                            2 -> painterResource(id = R.drawable.ic_complement)
                            else -> painterResource(id = R.drawable.ic_number_64) },
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

        when(selectedItem){
            0 -> {
                Text(modifier = Modifier.padding(padding), text = "Conversion")
            }
            1 -> {
                Text(modifier = Modifier.padding(padding), text = "Tree Diagram")
            }
            2 -> {
                Text(modifier = Modifier.padding(padding), text = "Table")
            }
        }
    }
}