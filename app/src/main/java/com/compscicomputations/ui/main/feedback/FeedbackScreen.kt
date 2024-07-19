package com.compscicomputations.ui.main.feedback

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compscicomputations.ui.utils.CompSciScaffold


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    navigateUp: () -> Unit,
) {
    CompSciScaffold(
        title = "Feedback",
        navigateUp = navigateUp,
    ) { contentPadding ->

        var dropdown by remember { mutableStateOf(false) }
        val options = listOf("Option 1", "Option 2", "Option 3")
        var selectedoption by remember {
            mutableStateOf(options[0])
        }
        ExposedDropdownMenuBox(
            modifier = Modifier.padding(contentPadding),
            expanded = dropdown,
            onExpandedChange = { dropdown = !dropdown }
        ) {
            OutlinedTextField(
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .padding(top = 8.dp)
                    .clickable { dropdown = !dropdown }
                    .focusable(false),
                value = selectedoption,
                onValueChange = {},
                trailingIcon =  {
                    Icon(imageVector = if (dropdown) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown, contentDescription = null)
                },
                shape = RoundedCornerShape(22.dp),
            )
            ExposedDropdownMenu(
                expanded = dropdown,
                onDismissRequest = { dropdown = false }
            ) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it) },
                        onClick = {
                            selectedoption = it
                            dropdown = false
                        }
                    )
                }
            }
        }
        when(selectedoption) {
            options[0] -> {
                Text(text = "Option 1 Feedback screen.")
            }

            options[1] -> {
                Text(text = "Option 2 Feedback screen.")
            }

            options[2] -> {
                Text(text = "Option 3 Feedback screen.")
            }
        }
    }
}