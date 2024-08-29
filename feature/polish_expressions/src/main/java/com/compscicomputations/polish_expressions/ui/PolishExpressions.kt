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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.R
import com.compscicomputations.polish_expressions.data.model.CurrentTab
import com.compscicomputations.polish_expressions.ui.trace_table.RowData
import com.compscicomputations.polish_expressions.ui.trace_table.TraceTablesScreen
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.ui.CompSciScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolishExpressions(
    navigateUp: () -> Unit,
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(2) }
    var currentTab by rememberSaveable { mutableStateOf(CurrentTab.TraceTable) }
    val items = listOf("Conversion", "Tree Diagram", "Trace Table")

    val postfixData = listOf(
        RowData('A', "", "A"),
        RowData('+', "+", "A"),
        RowData('(', "+(", "AB"),
        RowData('B', "+(", "AB"),
        RowData('-', "+(-", "AB"),
        RowData('C', "+(-", "ABC"),
        RowData(')', "+", "ABC-"),
        RowData('*', "+*", "ABC-"),
        RowData('A', "+*", "ABC-A"),
        RowData('-', "-", "ABC-A*+"),
        RowData('C', "-", "ABC-A*+C"),
        RowData(' ', "", "ABC-A*+C-"),
    )
    val prefixData = listOf(
        RowData('C', "", "C"),
        RowData('-', "-", "C"),
        RowData('A', "-", "AC"),
        RowData('*', "-*", "AC"),
        RowData(')', "-*)", "AC"),
        RowData('C', "-*)", "CAC"),
        RowData('-', "-*)-", "CAC"),
        RowData('B', "-*)-", "BCAC"),
        RowData('(', "-*", "-BCAC"),
        RowData('+', "-+", "*-BCAC"),
        RowData('A', "-+", "A*-BCAC"),
        RowData(' ', "", "-+A*-BCAC"),
    )

    CompSciScaffold(
        modifier = Modifier.fillMaxSize(),
        title = "Polish Expressions",
        navigateUp = navigateUp,
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {

                    }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Clear fields")
                    }
                    IconButton(onClick = {

                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.List,
                            contentDescription = "Response List",
                        )
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(text = "Generate Steps") },
                        icon = {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.ic_google_gemini),
                                contentDescription = null
                            )
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        onClick = {

                        }
                    )
                }
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            PrimaryScrollableTabRow(
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
                CurrentTab.Conversion -> Text(text = "Conversion")
                CurrentTab.TreeDiagram -> Text(text = "Tree Diagram")
                CurrentTab.TraceTable -> TraceTablesScreen(
                    postfixData = postfixData,
                    prefixData = prefixData
                )
            }
        }

    }
}