package com.compscicomputations.ui.auth.login

import android.view.KeyEvent.ACTION_DOWN
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.R
import com.compscicomputations.theme.AppRed
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.theme.hintEmail
import com.compscicomputations.theme.hintPassword
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.CompSciAuthScaffold
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.asActivity

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
    val (field1, field2) = remember { FocusRequester.createRefs() }
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
                .focusRequester(field1)
                .focusProperties { next = field2 }
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text(text = hintEmail) },
            shape = RoundedCornerShape(22.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            isError = uiState.emailError.isError,
            supportingText = uiState.emailError.showMessage()
        )
        OutlinedTextField(
            modifier = Modifier
                .focusRequester(field2)
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(text = hintPassword) },
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

        val activity by lazy { context.asActivity }
        OutlinedButton(
            onClick = { viewModel.onLoginWithGoogle(activity) },
            modifier = Modifier.fillMaxWidth()
                .height(68.dp)
                .padding(bottom = 8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        ) {
            Image(
                modifier = Modifier.padding(start = 4.dp).size(64.dp),
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Button Icon"
            )
            Text(
                modifier = Modifier.fillMaxWidth().padding(end = 68.dp),
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
