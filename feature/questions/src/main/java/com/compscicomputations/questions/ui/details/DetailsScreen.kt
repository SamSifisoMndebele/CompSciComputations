package com.compscicomputations.questions.ui.details

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
import com.compscicomputations.questions.R
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.utils.network.ConnectionState.Unavailable
import com.compscicomputations.utils.rememberConnectivityState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val connectivityState by rememberConnectivityState()

    CompSciScaffold(
        modifier = Modifier.fillMaxSize(),
        title = uiState.question.question,
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
//            BottomAppBar(
//                actions = {},
//                floatingActionButton = if (uiState.isStudent) {
//                    {
//                        ExtendedFloatingActionButton(
//                            text = { Text("Ask a Question") },
//                            icon = {
//                                Image(
//                                    modifier = Modifier.size(24.dp),
//                                    imageVector = Icons.Outlined.Email,
//                                    contentDescription = null
//                                )
//                            },
//                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
//                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
//                            onClick = {
//                                // TODO: Ask a question
//                            }
//                        )
//                    }
//                } else null
//            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            Text(
                text = uiState.question.question,
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
