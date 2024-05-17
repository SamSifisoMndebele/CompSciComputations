package com.compscicomputations.ui.auth.login

import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.compscicomputations.R
import com.compscicomputations.ui.LoadingDialog
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.theme.comicNeueFamily
import com.compscicomputations.ui.theme.hintEmail
import com.compscicomputations.ui.theme.hintPassword

@Composable
fun LoginScreen(
    padding: PaddingValues = PaddingValues(8.dp),
    viewModel: LoginViewModel,
    lottieComposition: LottieComposition?,
    lottieProgress: Float,
    navigateRegister: () -> Unit,
    navigateResetPassword: (email: String?) -> Unit,
    navigateMain: () -> Unit
) {
    val userLogged by viewModel.userLogged.collectAsState()
    val name by viewModel.name.collectAsState()
    val context =  LocalContext.current
    LaunchedEffect(userLogged) {
        if (userLogged) {
            navigateMain()
            Toast.makeText(context, if (name != null) "$name, logged in successfully!"
                else "Logged in successfully!", Toast.LENGTH_SHORT).show()
        }
    }
    val showProgress by viewModel.showProgress.collectAsState()

    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
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
//                Spacer(modifier = Modifier.weight(1f))
//                IconButton(onClick = { /*TODO*/ }) {
//                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
//                }
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
                    composition = lottieComposition,
                    progress = lottieProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    contentScale = ContentScale.FillWidth
                )
                Text(
                    text = "Login into your account using your email.",
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                )
            }

            val email by viewModel.email.collectAsState()
            val password by viewModel.password.collectAsState()

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = {
                    Text(text = hintEmail)
                },
                shape = RoundedCornerShape(22.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = viewModel.emailError.isError,
                supportingText = viewModel.emailError.showMessage()
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = {
                    Text(text = hintPassword)
                },
                singleLine = true,
                shape = RoundedCornerShape(22.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = viewModel.passwordError.isError,
                supportingText = viewModel.passwordError.showMessage()
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = .25.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navigateResetPassword(viewModel.email.value.ifBlank { null }) }) {
                    Text(text = "Forgot Password", fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, fontFamily = comicNeueFamily)
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = { navigateRegister() }) {
                    Text(text = "Register", fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, fontFamily = comicNeueFamily)
                }
            }

            SnackbarHost(hostState = viewModel.snackBarHostState,
                modifier = Modifier
                    .fillMaxWidth()) {
                Snackbar(
                    snackbarData = it,
                    containerColor = OutlinedTextFieldDefaults.colors().errorLabelColor,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Button(
                enabled = email.isNotBlank() && password.isNotBlank(),
                onClick = { viewModel.onLogin() },
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

            OutlinedButton(onClick = { viewModel.continueWithGoogle(context) },
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

        }

        LoadingDialog(show = showProgress, message = "Login...")
    }
}
