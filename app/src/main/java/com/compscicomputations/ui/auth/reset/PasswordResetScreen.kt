package com.compscicomputations.ui.auth.reset

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.compscicomputations.R
import com.compscicomputations.ui.auth.FieldType
import com.compscicomputations.ui.auth.contain
import com.compscicomputations.ui.auth.getMessage
import com.compscicomputations.ui.theme.comicNeueFamily
import com.compscicomputations.ui.theme.hintEmail

@Composable
fun PasswordResetScreen(
    padding: PaddingValues = PaddingValues(8.dp),
    uiState: ResetUiState,
    onEvent: (ResetUiEvent) -> Unit,
    navigateUp: () -> Unit
) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.login_anim
        )
    )
    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = navigateUp, Modifier.padding(vertical = 4.dp)) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
            Image(
                painter = painterResource(id =+ R.drawable.img_logo_name),
                contentDescription = stringResource(id = R.string.app_name),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(128.dp)
                    .padding(start = 8.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
            }
        }
        Text(
            text = "Password\nReset",
            fontFamily = comicNeueFamily,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 52.sp,
            lineHeight = 52.sp,
            modifier = Modifier.padding(start = 8.dp)
        )

        Column(
            Modifier.padding(padding)
        ) {
            LottieAnimation(
                composition = preloaderLottieComposition,
                progress = preloaderProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.FillWidth
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                value = uiState.email,
                onValueChange = { onEvent(ResetUiEvent.OnEmailChange(it)) },
                label = {
                    Text(text = hintEmail)
                },
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = uiState.errors contain FieldType.EMAIL,
                supportingText = uiState.errors getMessage FieldType.EMAIL
            )

            Button(
                onClick = { onEvent(ResetUiEvent.Reset) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text(text = "Sent Reset Email", fontWeight = FontWeight.Bold, fontSize = 22.sp,
                    fontFamily = comicNeueFamily)
            }
        }



        /*val context = LocalContext.current
        LaunchedEffect(key1 = uiState.errorMessage) {
            if (uiState.errorMessage != null) {
                Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                onEvent(UiEvent.ClearError)
            }
        }*/
    }
}