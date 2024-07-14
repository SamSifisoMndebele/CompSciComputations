package com.compscicomputations.ui.auth.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.R
import com.compscicomputations.core.ktor_client.auth.AuthDataStore.firstLaunchFlow
import com.compscicomputations.theme.AppRed
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.theme.hintEmail
import com.compscicomputations.theme.hintPassword
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.CompSciAuthScaffold
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.getActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    uiState: LoginUiState,
    navigateOnboarding: () -> Unit,
    navigateRegister: () -> Unit,
    navigateResetPassword: (email: String?) -> Unit,
    navigateCompleteProfile: () -> Unit,
    navigateMain: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(uiState.progressState, uiState.isNewUser) {
        if (uiState.isNewUser) {
            navigateCompleteProfile()
            Toast.makeText(context, "Please complete your profile!", Toast.LENGTH_SHORT).show()
        }
        else if (uiState.progressState == ProgressState.Success) {
            navigateMain()
            Toast.makeText(context, "Logged in successfully!", Toast.LENGTH_SHORT).show()
        }
    }
    CompSciAuthScaffold(
        title = "Login",
        description = "Login into your account using your email.",
        navigateUp = null,
        navigateOnboarding = navigateOnboarding,
        progressState = uiState.progressState,
        onLoadingDismiss = { viewModel.cancelLogin() },
        onExceptionDismiss = { viewModel.onProgressStateChange(ProgressState.Idle) }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = {
                Text(text = hintEmail)
            },
            shape = RoundedCornerShape(22.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.emailError.isError,
            supportingText = uiState.emailError.showMessage()
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = {
                Text(text = hintPassword)
            },
            singleLine = true,
            shape = RoundedCornerShape(22.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = uiState.passwordError.isError,
            supportingText = uiState.passwordError.showMessage()
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = .25.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navigateResetPassword(uiState.email.ifBlank { null }) }) {
                Text(
                    text = "Forgot Password", fontWeight = FontWeight.Bold,
                    fontSize = 18.sp, fontFamily = comicNeueFamily
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { navigateRegister() }) {
                Text(
                    text = "Register", fontWeight = FontWeight.Bold,
                    fontSize = 18.sp, fontFamily = comicNeueFamily
                )
            }
        }

        Button(
            enabled = uiState.email.isNotBlank() && uiState.password.isNotBlank(),
            onClick = { viewModel.onLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(vertical = 18.dp)
        ) {
            Text(
                text = "LOGIN",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                fontFamily = comicNeueFamily
            )
        }

        OutlinedButton(
            onClick = { viewModel.onLoginWithGoogle(context.getActivity()) },
            modifier = Modifier.fillMaxWidth()
                .height(68.dp)
                .padding(bottom = 8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Button Icon"
                )
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 4.dp, end = 32.dp),
                    text = "Continue with Google",
                    color = AppRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    fontFamily = comicNeueFamily,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
