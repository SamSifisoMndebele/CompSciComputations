package com.compscicomputations.feature.karnaugh_maps

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.compscicomputations.presentation.main.AppBar

enum class NumVariables(val string: String) {
    TWO("2 variables"),
    THREE("3 variables"),
    FOUR("4 variables")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KarnaughScreen(
    navigateUp: () -> Unit,
) {
    var numVariables by rememberSaveable { mutableStateOf(NumVariables.FOUR) }

    Column(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp),
    ) {
        AppBar(title = "Karnaugh Maps", navigateUp = navigateUp) {
            //TODO Menu
        }
        var variablesDropdown by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = variablesDropdown,
            onExpandedChange = { variablesDropdown = !variablesDropdown }
        ) {
            OutlinedTextField(
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .padding(top = 8.dp)
                    .clickable { variablesDropdown = !variablesDropdown }
                    .focusable(false),
                value = numVariables.string,
                onValueChange = {},
                trailingIcon =  {
                    Icon(imageVector = if (variablesDropdown) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown, contentDescription = null)
                },
                shape = RoundedCornerShape(22.dp),
            )
            ExposedDropdownMenu(
                expanded = variablesDropdown,
                onDismissRequest = { variablesDropdown = false }
            ) {
                NumVariables.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.string) },
                        onClick = {
                            numVariables = it
                            variablesDropdown = false
                        }
                    )
                }
            }
        }

        Karnaugh4FragmentInCompose()
        /*when(numVariables) {
            NumVariables.TWO -> Karnaugh2()
            NumVariables.THREE -> Karnaugh3()
            NumVariables.FOUR -> Karnaugh4()
        }*/

    }
}

@SuppressLint("ClickableViewAccessibility")
@Composable
fun Karnaugh4FragmentInCompose() {
    AndroidView(factory = {
//        val inflater = LayoutInflater.from(context)
//        val binding = Karnaugh4VariablesBinding.inflate(inflater)

        val karnaugh4Fragment = Karnaugh4Fragment()



        karnaugh4Fragment.rootView
    })
}