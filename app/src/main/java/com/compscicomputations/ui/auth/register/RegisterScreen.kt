package com.compscicomputations.ui.auth.register

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.core.ktor_client.auth.AuthDataStore.setTermsAccepted
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.theme.hintEmail
import com.compscicomputations.theme.hintPassword
import com.compscicomputations.theme.hintPasswordConfirm
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.CompSciAuthScaffold
import com.compscicomputations.ui.utils.ProgressState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    uiState: RegisterUiState,
    navigateUp: () -> Unit,
    navigateOnboarding: () -> Unit,
    navigateTerms: () -> Unit,
    navigateCompleteProfile: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(uiState.progressState) {
        if (uiState.progressState == ProgressState.Success) {
            navigateCompleteProfile()
            Toast.makeText(context, "Registered in successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    CompSciAuthScaffold(
        title = "Register",
        description = "Register your account using your email and password.",
        progressState = uiState.progressState,
        onLoadingDismiss = { viewModel.cancelRegister() },
        onExceptionDismiss = { viewModel.onProgressStateChange(ProgressState.Idle) },
        navigateUp = navigateUp,
        navigateOnboarding = navigateOnboarding
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text(text = hintEmail) },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.emailError.isError,
            supportingText = uiState.emailError.showMessage()
        )

        var showPassword by remember { mutableStateOf(false) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it)},
            label = { Text(text = hintPassword) },
            singleLine = true,
            visualTransformation =  if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                }
            },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = uiState.passwordError.isError,
            supportingText = uiState.passwordError.showMessage()
        )

        var showPasswordConfirm by remember { mutableStateOf(false) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.passwordConfirm,
            onValueChange = { viewModel.onPasswordConfirmChange(it) },
            label = { Text(text = hintPasswordConfirm) },
            singleLine = true,
            visualTransformation = if (showPasswordConfirm) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPasswordConfirm = !showPasswordConfirm }) {
                    Icon(if (showPasswordConfirm) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                }
            },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = uiState.passwordConfirmError.isError,
            supportingText = uiState.passwordConfirmError.showMessage()
        )

        val coroutineScope = rememberCoroutineScope()
        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = uiState.termsAccepted,
                onCheckedChange = {
                    coroutineScope.launch(Dispatchers.IO) { context.setTermsAccepted(it) }
                }
            )
            Text(text = "I accept the", Modifier.padding(start = 10.dp), fontSize = 16.sp)
            TextButton(onClick = navigateTerms, contentPadding = PaddingValues(horizontal = 3.dp)) {
                Text(text = "Terms and Conditions.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            uiState.termsAcceptedError.showMessage()
        }

        Button(onClick = { viewModel.onRegister() },
            enabled = uiState.isValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(vertical = 18.dp)
        ) {
            Text(text = "REGISTER", fontWeight = FontWeight.Bold, fontSize = 22.sp,
                fontFamily = comicNeueFamily)
        }
    }
}