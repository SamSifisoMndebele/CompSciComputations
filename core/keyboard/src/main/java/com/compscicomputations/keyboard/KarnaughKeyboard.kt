package com.compscicomputations.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.compscicomputations.keyboard.model.AlphabetKey
import com.compscicomputations.keyboard.model.Key
import com.compscicomputations.keyboard.model.UtilityKey

@Composable
fun KarnaughKeyboard(
    modifier: Modifier = Modifier,
    textFieldState: MutableState<TextFieldValue>,
    onClose: () -> Unit,
    onAction: () -> Unit = {},
    onClick: (key: Key) -> Unit
) {
    val keys = listOf(
        AlphabetKey.A,
        AlphabetKey.B,
        UtilityKey.Or,
        UtilityKey.ArrowLeft,
        UtilityKey.ArrowRight,
        UtilityKey.Backspace,
        AlphabetKey.C,
        AlphabetKey.D,
        UtilityKey.Not,
        UtilityKey.Action
    )

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceDim, RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .wrapContentHeight(),
            columns = GridCells.Fixed(9)
        ) {
            items(
                count = keys.size,
                key = { keys[it].value },
                span = { GridItemSpan(keys[it].span) }
            ) { i ->
                KeyboardButton(
                    key = keys[i],
                ) {
                    when(it) {
                        is UtilityKey.Action -> return@KeyboardButton onAction()
                        is UtilityKey.Backspace -> textFieldState.backspace()
                        is UtilityKey.Or -> textFieldState.append("+")
                        is UtilityKey.Not -> textFieldState.append("'")
                        is UtilityKey.ArrowLeft -> textFieldState.leftArrow()
                        is UtilityKey.ArrowRight -> textFieldState.rightArrow()
                        is AlphabetKey -> textFieldState.append(it.value)
                    }
                    onClick(it)
                }
            }
        }
        IconButton(onClick = onClose) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Close")
        }
    }
}