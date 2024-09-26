package com.compscicomputations.ui.utils.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.compscicomputations.R
import com.compscicomputations.data.source.local.AiResponse
import com.compscicomputations.theme.comicNeueFamily
import kotlinx.coroutines.flow.Flow

@Composable
fun StepsList(
    show: Boolean,
    onDismissRequest: () -> Unit,
    listFlow: Flow<List<AiResponse>?>,
    onSelect: (response: AiResponse) -> Unit,
    onDelete: (response: AiResponse) -> Unit,
) {
    if (show) {
        val list by listFlow.collectAsState(initial = listOf(
            AiResponse(
            "",
            "",
            "",
            "Loading...",
            ""
        )
        ))
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
                        items(itemsList, key = { it.convertFrom + it.value }) {
                            ListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (it.module.isNotBlank()) onSelect(it)
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
                                    if (it.module.isNotBlank()) {
                                        Text("Converted from: "+it.convertFrom)
                                    }
                                },
                                supportingContent = {
                                    if (it.module.isNotBlank()) {
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
                                    if (it.module.isNotBlank()) {
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
                                        painter = painterResource(id = R.drawable.ic_ai),
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