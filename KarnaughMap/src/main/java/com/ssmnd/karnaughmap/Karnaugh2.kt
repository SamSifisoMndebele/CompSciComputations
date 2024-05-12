package com.ssmnd.karnaughmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Karnaugh2() {
    val viewModel: Karnaugh2ViewModel = viewModel()
    Karnaugh(
        viewModel.booleanExp, 2,
        answers = viewModel.answers,
        selectedAnswer = viewModel.selectedAnswer
    )  {
        Image(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            painter = painterResource(id = R.drawable.kmap_2_var_ab),
            contentDescription = "2 Var K Map",
            contentScale = ContentScale.FillWidth
        )
        /*AndroidView(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            factory = {
                KMap2VariablesImageView(it).apply {
                    adjustViewBounds = true
                }
            }
        ) {

        }*/
    }
}