package com.compscicomputations.ui.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.compscicomputations.ui.theme.hintPassword

@Composable
fun LoginScreen(
    padding: PaddingValues = PaddingValues(8.dp),
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    navigateRegister: () -> Unit = {},
    navigateResetPassword: () -> Unit = {},
    navigateMain: () -> Unit = {}
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

    Column(
        Modifier.padding(padding).fillMaxSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.img_logo_name),
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
            text = "Login",
            fontFamily = comicNeueFamily,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 52.sp,
            modifier = Modifier.padding(start = 8.dp, top=8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.BottomStart
        ) {
            LottieAnimation(
                composition = preloaderLottieComposition,
                progress = preloaderProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = "Login into your account using your email.",
                Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.email,
            onValueChange = { onEvent(LoginUiEvent.OnEmailChange(it)) },
            label = {
                Text(text = hintEmail)
            },
            shape = RoundedCornerShape(22.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.errors contain FieldType.EMAIL,
            supportingText = uiState.errors getMessage FieldType.EMAIL
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.password,
            onValueChange = { onEvent(LoginUiEvent.OnPasswordChange(it)) },
            label = {
                Text(text = hintPassword)
            },
            shape = RoundedCornerShape(22.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = uiState.errors contain FieldType.PASSWORD,
            supportingText = uiState.errors getMessage FieldType.PASSWORD
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = .25.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navigateResetPassword() }) {
                Text(text = "Forgot Password", fontWeight = FontWeight.Bold,
                    fontSize = 18.sp, fontFamily = comicNeueFamily)
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { navigateRegister() }) {
                Text(text = "Register", fontWeight = FontWeight.Bold,
                    fontSize = 18.sp, fontFamily = comicNeueFamily)
            }
        }

        Button(
            onClick = navigateMain,
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(vertical = 18.dp)
        ) {
            Text(text = "LOGIN", fontWeight = FontWeight.Bold, fontSize = 22.sp,
                fontFamily = comicNeueFamily)
        }

        OutlinedButton(onClick = navigateMain,
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(vertical = 18.dp)
            ) {
            Text(text = "Continue with Google", fontWeight = FontWeight.Bold, fontSize = 22.sp,
                fontFamily = comicNeueFamily)
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