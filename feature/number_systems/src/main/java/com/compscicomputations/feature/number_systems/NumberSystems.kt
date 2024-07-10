package com.compscicomputations.feature.number_systems

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
fun NumberSystemsScreen(
    navigateUp: () -> Unit,
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf("Conversion", "Excess", "Complement", "Floating Point")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
//            AppBar(
//                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp),
//                title = "Number Systems", navigateUp = navigateUp) {
//                //TODO Menu
//            }
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(when(index) {
                            0 -> painterResource(id = R.drawable.ic_num_system)
                            1 -> painterResource(id = R.drawable.ic_excess)
                            2 -> painterResource(id = R.drawable.ic_complement)
                            3 -> painterResource(id = R.drawable.ic_floating)
                            else -> painterResource(id = R.drawable.ic_num_system) },
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
            0 -> ConversionScreen(padding,)
            1 -> ExcessScreen(padding)
            2 -> ComplementScreen(padding)
            3 -> FloatingPointScreen(padding)
        }
    }
}