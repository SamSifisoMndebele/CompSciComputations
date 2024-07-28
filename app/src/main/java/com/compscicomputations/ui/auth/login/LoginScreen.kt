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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.R
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.theme.hintEmail
import com.compscicomputations.theme.hintPassword
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.ui.utils.isSuccess
import com.compscicomputations.ui.utils.ui.CompSciAuthScaffold


@Composable
fun LoginScreen(
    activity: Activity,
    viewModel: LoginViewModel = hiltViewModel(),
    navigateOnboarding: () -> Unit,
    navigateRegister: () -> Unit,
    navigateResetPassword: (email: String?) -> Unit,
    navigateMain: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.progressState) {
        if (uiState.progressState.isSuccess) {
            navigateMain()
            Toast.makeText(context, "Logged in successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    val (field1, field2) = remember { FocusRequester.createRefs() }

    CompSciAuthScaffold(
        title = "Login",
        description = "Login into your account using password or google.",
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
        var showPassword by remember { mutableStateOf(false) }
        OutlinedTextField(
            modifier = Modifier
                .focusRequester(field2)
                .onFocusChanged { if (!it.isFocused) showPassword = false }
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(text = hintPassword) },
            singleLine = true,
            visualTransformation =  if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (uiState.canShowPassword) {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                    }
                }
            },
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

        OutlinedButton(
            onClick = {
                viewModel.onLogin { email, password ->
                    val createPasswordRequest = CreatePasswordRequest(email, password)
                    CredentialManager.create(activity).createCredential(activity, createPasswordRequest)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(bottom = 8.dp),
            enabled = uiState.isValid,
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(64.dp),
                imageVector = Icons.Default.Password,
                contentDescription = "Password Button Icon",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 68.dp),
                text = "Login",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                fontFamily = comicNeueFamily,
                textAlign = TextAlign.Center
            )
        }

        OutlinedButton(
            onClick = {
                viewModel.onContinueWithGoogle { googleRequest ->
                    CredentialManager
                        .create(activity)
                        .getCredential(activity, googleRequest)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(bottom = 8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(64.dp),
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Button Icon"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 68.dp),
                text = "Continue with Google",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                fontFamily = comicNeueFamily,
                textAlign = TextAlign.Center
            )
        }
    }
}