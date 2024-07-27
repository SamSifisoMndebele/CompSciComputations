package com.compscicomputations.number_systems.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.compscicomputations.number_systems.R
import com.compscicomputations.number_systems.ui.bases.BasesScreen
import com.compscicomputations.number_systems.ui.complement.ComplementScreen
import com.compscicomputations.number_systems.ui.excess.ExcessScreen
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointScreen
import com.compscicomputations.ui.utils.ui.CompSciScaffold

@Composable
fun NumberSystems(
    navigateUp: () -> Unit,
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf("Conversion", "Excess", "Complement", "Floating Point")
    CompSciScaffold(
        modifier = Modifier.fillMaxSize(),
        title = "Number Systems",
        navigateUp = navigateUp,
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = when(index) {
                                    0 -> painterResource(id = R.drawable.ic_num_system)
                                    1 -> painterResource(id = R.drawable.ic_excess)
                                    2 -> painterResource(id = R.drawable.ic_complement)
                                    3 -> painterResource(id = R.drawable.ic_floating)
                                    else -> painterResource(id = R.drawable.ic_num_system) },
                                contentDescription = item,
                            )
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        },
    ) { contentPadding ->
        when(selectedItem){
            0 -> BasesScreen(contentPadding)
            1 -> ExcessScreen(contentPadding)
            2 -> ComplementScreen(contentPadding)
            3 -> FloatingPointScreen(contentPadding)
        }
    }
}