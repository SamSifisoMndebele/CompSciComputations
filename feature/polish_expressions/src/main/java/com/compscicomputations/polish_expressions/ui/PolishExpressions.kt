package com.compscicomputations.polish_expressions.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.R
import com.compscicomputations.polish_expressions.data.model.CurrentTab
import com.compscicomputations.polish_expressions.ui.conversion.ConversionScreen
import com.compscicomputations.polish_expressions.ui.conversion.ConversionViewModel
import com.compscicomputations.polish_expressions.ui.trace_table.TraceTableViewModel
import com.compscicomputations.polish_expressions.ui.trace_table.TraceTablesScreen
import com.compscicomputations.polish_expressions.ui.tree_diagram.TreeDiagramScreen
import com.compscicomputations.polish_expressions.ui.tree_diagram.TreeDiagramViewModel
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.ui.CompSciScaffold

@Composable
fun PolishExpressions(
    navigateUp: () -> Unit,
) {
    var currentTab by rememberSaveable { mutableStateOf(CurrentTab.Conversion) }

    val context = LocalContext.current

    val conversionViewModel = ConversionViewModel(context)
    val treeDiagramViewModel = TreeDiagramViewModel()
    val traceTableViewModel = TraceTableViewModel()

    val uiState by conversionViewModel.uiState.collectAsStateWithLifecycle()

    CompSciScaffold(
        modifier = Modifier.fillMaxSize(),
        title = "Polish Expressions",
        navigateUp = navigateUp,
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        conversionViewModel.clear()
                        treeDiagramViewModel.onChange(listOf(), listOf(), listOf())
                        traceTableViewModel.onChange("")
                    }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Clear fields")
                    }
//                    IconButton(onClick = {
//
//                    }) {
//                        Icon(
//                            Icons.AutoMirrored.Outlined.List,
//                            contentDescription = "Response List",
//                        )
//                    }
                },
                floatingActionButton = {
                    if (currentTab == CurrentTab.Conversion) {
//                        ExtendedFloatingActionButton(
//                            text = { Text(text = "Generate Steps") },
//                            icon = {
//                                Image(
//                                    modifier = Modifier.size(24.dp),
//                                    painter = painterResource(id = R.drawable.ic_ai),
//                                    contentDescription = null
//                                )
//                            },
//                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
//                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
//                            onClick = {
//                                //TODO("Generate on Conversion")
//                            }
//                        )
                    }
                }
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) { ScrollableTabRow(
                selectedTabIndex = currentTab.ordinal,
                indicator = { tabPositions ->
                    if (currentTab.ordinal < tabPositions.size) {
                        val width by animateDpAsState(
                            targetValue = tabPositions[currentTab.ordinal].contentWidth,
                            label = "TabAnimation"
                        )
                        TabRowDefaults.PrimaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[currentTab.ordinal]),
                            width = width,
                            height = 6.dp,
                        )
                    }
                },
                divider = {},
            ) {
                CurrentTab.entries.forEach { tab ->
                    Tab(
                        modifier = Modifier.clip(RoundedCornerShape(22.dp)),
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        text = {
                            Text(
                                text = tab.title,
                                maxLines = 2,
                                fontSize = 14.sp,
                                fontFamily = comicNeueFamily,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            when(currentTab) {
                CurrentTab.Conversion -> ConversionScreen(
                    viewModel = conversionViewModel,
                    uiState = uiState
                )
                CurrentTab.TreeDiagram -> {
                    treeDiagramViewModel.onChange(uiState.infix, uiState.prefix, uiState.postfix)
                    TreeDiagramScreen(
                        viewModel = treeDiagramViewModel,
                    )
                }
                CurrentTab.TraceTable -> {
                    traceTableViewModel.onChange(uiState.infix)
                    TraceTablesScreen(
                        viewModel = traceTableViewModel
                    )
                }
            }
        }

    }
}