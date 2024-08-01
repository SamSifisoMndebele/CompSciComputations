package com.compscicomputations.number_systems.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.number_systems.data.source.local.AiResponse
import com.compscicomputations.number_systems.R
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.CurrentTab
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.ui.AnnotatedText
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun StepsList(
    show: Boolean,
    onDismissRequest: () -> Unit,
    listFlow: Flow<List<AiResponse>?>,
    onSelect: (response: AiResponse) -> Unit,
    onDelete: (response: AiResponse) -> Unit,
) {
    if (show) {
        val list by listFlow.collectAsState(initial = listOf(AiResponse(
            0,
            CurrentTab.BaseN,
            ConvertFrom.Decimal,
            "Loading...",
            ""
        )))
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(22.dp),
            ) {
                val itemsList = list?.toList()
                if (itemsList != null) {
                    LazyColumn(Modifier.fillMaxWidth().heightIn(54.dp)) {
                        items(itemsList, key = { it.id }) {
                            ListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (it.id != 0) onSelect(it)
                                    },
                                headlineContent = {
                                    Text(
                                        text = it.value,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        maxLines = 1,
                                        fontFamily = comicNeueFamily,
                                    )
                                },
                                overlineContent = {
                                    if (it.id != 0) {
                                        Text("Converted from: "+it.convertFrom.text)
                                    }
                                },
                                supportingContent = {
                                    if (it.id != 0) {
                                        Text(
                                            text = it.text.take(500).replace("#", "")
                                                .replace("*", "")
                                                .replace("\n", " ").trimIndent(),
                                            maxLines = 2,
                                            fontSize = 12.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            fontStyle = FontStyle.Italic
                                        )
                                    }
                                },
                                trailingContent = {
                                    if (it.id != 0) {
                                        IconButton(onClick = {
                                            onDelete(it)
                                        }) {
                                            Icon(
                                                Icons.Outlined.Delete,
                                                contentDescription = "Response List",
                                            )
                                        }
                                    }
                                },
                                leadingContent = {
                                    Image(
                                        modifier = Modifier.size(24.dp),
                                        painter = painterResource(id = R.drawable.ic_google_gemini),
                                        contentDescription = null
                                    )
                                },
                            )
                            HorizontalDivider()
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                            text = "Empty history.\nContinue using our AI, your history list will display here."
                        )
                        TextButton(
                            onClick = onDismissRequest,
                        ) {
                            Text(text = "OK", fontSize = 18.sp)
                        }
                    }
                }
            }
        }

    }
}