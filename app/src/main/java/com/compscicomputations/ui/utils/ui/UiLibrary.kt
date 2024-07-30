package com.compscicomputations.ui.utils.ui


import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.theme.comicNeueFamily

@Composable
fun Modifier.shimmerBackground(
    showShimmer: Boolean,
    shape: Shape = RoundedCornerShape(18.dp),
    targetValue: Float = 1500f
): Modifier = background(
    if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.4f),
            Color.LightGray.copy(alpha = 0.1f),
            Color.LightGray.copy(alpha = 0.4f),
        )

        val transition = rememberInfiniteTransition(label = "Shimmer Infinite Transition")
        val translateAnimation = transition.animateFloat(
            label = "Shimmer Animation State",
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(1000), repeatMode = RepeatMode.Restart
            ),
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    }
    else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent,),
            start = Offset.Zero,
            end = Offset.Zero
        )
    },
    shape
)

private val boldRegex = Regex("(?<!\\*)\\*\\*(?!\\*).*?(?<!\\*)\\*\\*(?!\\*)")
private val subRegex = Regex("<sub>.*?</sub>")

@Composable
fun AnnotatedText(
    text: String,
    modifier: Modifier = Modifier,
) {
    val annotatedString = buildAnnotatedString {
        append("\n")
        var startIndex = 0
        boldRegex.findAll(text)
            .map { it.value }
            .forEach { keyword ->
                val  indexOf = text.indexOf(keyword, startIndex)
                append(text.substring(startIndex, indexOf).replace("*", "●"))
                startIndex = indexOf + keyword.length
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.ExtraBold,)
                ) {
                    append(keyword.removeSurrounding("**"))
                }
            }
        append(text.substring(startIndex, text.length).replace("*", "●"))
        append("\n")

//        startIndex = 0
//        subRegex.findAll(toString())
//            .map { it.value }
//            .forEach { keyword ->
//                val  indexOf = toString().indexOf(keyword, startIndex)
//                append(toString().substring(startIndex, indexOf))
//                startIndex = indexOf + keyword.length
//                withStyle(
//                    style = SpanStyle(baselineShift = BaselineShift.Subscript)
//                ) {
//                    append(keyword.removeSurrounding("<sub>", "</sub>"))
//                }
//            }
    }
    Text(
        text = annotatedString,
        modifier = modifier.fillMaxWidth(),
        fontSize = 18.sp,
        fontFamily = comicNeueFamily
    )
}

@Composable
fun Demo_DropDownMenu() {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Load") },
                onClick = {

                }
            )
            DropdownMenuItem(
                text = { Text("Save") },
                onClick = {

                }
            )
        }
    }
}




