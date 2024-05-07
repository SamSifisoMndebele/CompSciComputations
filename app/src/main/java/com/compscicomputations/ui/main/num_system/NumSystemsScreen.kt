package com.compscicomputations.ui.main.num_system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.compscicomputations.R
import com.compscicomputations.ui.main.AppBar
import com.compscicomputations.ui.main.dashboard.DashboardOption
import com.ssmnd.karnaughmap.Karnaugh4Fragment

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NumSystemsScreen(
    padding: PaddingValues = PaddingValues(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp),
    navigateUp: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(padding),
    ) {
        AppBar(title = "Number Systems", navigateUp = navigateUp) {
            //TODO Menu
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item {
                Card(
                    modifier = Modifier.padding(vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        )
                    ) {
                        AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            factory = { context ->
                                val map = Karnaugh4Fragment()


                                map.rootView
                            }
                        )
                    }
                    val optionPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_number_64),
                        text = "Number Systems",
                        onClick = {  }
                    )
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_abc),
                        text = "Polish Expressions",
                        onClick = {  }
                    )
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_grid),
                        text = "Karnaugh Maps",
                        onClick = {  }
                    )

                }
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                DashboardOption(
                    iconVector = Icons.AutoMirrored.Outlined.Help,
                    text = "Help",
                    onClick = {  }
                )
            }
        }
    }
}