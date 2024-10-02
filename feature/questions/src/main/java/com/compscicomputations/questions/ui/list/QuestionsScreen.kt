package com.compscicomputations.questions.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compscicomputations.questions.Question
import com.compscicomputations.questions.R
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.utils.network.ConnectionState.Unavailable
import com.compscicomputations.utils.rememberConnectivityState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun QuestionsScreen(
    viewModel: QuestionsViewModel,
    navigateUp: () -> Unit,
    navigateToDetails: (Question) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val connectivityState by rememberConnectivityState()

    CompSciScaffold(
        modifier = Modifier.fillMaxSize(),
        title = "Questions",
        navigateUp = navigateUp,
        tabsBar = {
            if (connectivityState is Unavailable) {
                Text(
                    text = "Enable your internet connection to show steps.",
                    fontFamily = comicNeueFamily,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1C45C)),
                    color = Color.DarkGray
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {},
                floatingActionButton = if (uiState.isStudent) {
                    {
                        ExtendedFloatingActionButton(
                            text = { Text("Ask a Question") },
                            icon = {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = null
                                )
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                            onClick = {
                                // TODO: Ask a question
                            }
                        )
                    }
                } else null
            )
        }
    ) { contentPadding ->
        if (uiState.questions.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                LazyColumn(Modifier.fillMaxWidth().heightIn(54.dp)) {
                    items(uiState.questions, key = { it.question }) {
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navigateToDetails(it) },
                            headlineContent = {
                                Text(
                                    text = it.question,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontFamily = comicNeueFamily,
                                )
                            },
                            overlineContent = {
                                Text("From: "+it.userName)
                            },
//                            supportingContent = {
//                                if (it.module.isNotBlank()) {
//                                    Text(
//                                        text = it.text.take(500).replace("#", "")
//                                            .replace("*", "")
//                                            .replace("\n", " ").trimIndent(),
//                                        maxLines = 2,
//                                        fontSize = 12.sp,
//                                        overflow = TextOverflow.Ellipsis,
//                                        fontStyle = FontStyle.Italic
//                                    )
//                                }
//                            },
                            trailingContent = {
                                if (it.imageUrl != null) {
//                                    Image()
                                }
                            },
                            leadingContent = {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = com.compscicomputations.R.drawable.ic_ai),
                                    contentDescription = null
                                )
                            },
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
        else {
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No questions yet.",
                    fontFamily = comicNeueFamily,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(128.dp)
                        .padding(16.dp),
                    painter = painterResource(id = R.drawable.ic_empty),
                    contentDescription = null
                )
            }
        }
    }


}
