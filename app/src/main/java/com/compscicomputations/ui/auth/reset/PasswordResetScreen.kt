package com.compscicomputations.ui.auth.reset

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.theme.hintEmail
import com.compscicomputations.theme.hintNewPassword
import com.compscicomputations.theme.hintOtp
import com.compscicomputations.theme.hintPasswordConfirm
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.ui.utils.isSuccess
import com.compscicomputations.ui.utils.ui.CompSciAuthScaffold
import com.compscicomputations.utils.onVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PasswordResetScreen(
    viewModel: PasswordResetViewModel,
    navigateOnboarding: () -> Unit,
    navigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(uiState.progressState) {
        if (uiState.progressState.isSuccess) {
            navigateUp()
            Toast.makeText(context, "Password reset was successful, please login.", Toast.LENGTH_LONG).show()
        }
    }
    val coroutineScope = rememberCoroutineScope()

    val (field1, field2, field3) = remember { FocusRequester.createRefs() }
    CompSciAuthScaffold(
        title = "Password Reset",
        description = "Reset your password with OTP sent to your email.",
        navigateUp = navigateUp,
        navigateOnboarding = navigateOnboarding,
        progressState = uiState.progressState,
        onLoadingDismiss = { viewModel.cancel() },
        onExceptionDismiss = { viewModel.onProgressStateChange(ProgressState.Idle) }
    ) {
        var showFields by remember { mutableStateOf(false) }
        LaunchedEffect(showFields) {
            if (showFields) {
                Toast.makeText(context, "OTP sent successfully to your email!", Toast.LENGTH_SHORT).show()
            }
        }
        OutlinedTextField(
//            enabled = !uiState.otpSent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text(text = hintEmail) },
            singleLine = true,
            trailingIcon = {
                if (uiState.sendingOtp) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                } else if (showFields) {
                    var millis by remember { mutableLongStateOf(300000) }
                    LaunchedEffect(Dispatchers.IO) {
                        while (millis > 0) {
                            delay(1000)
                            millis -= 1000
                        }
                        showFields = false
                    }
                    val min = millis / 60000
                    val sec = (millis % 60000) / 1000
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontFamily = comicNeueFamily,
                        color = MaterialTheme.colorScheme.primary,
                        text = "$min:" + sec.toString().padStart(2, '0')
                    )
                } else {
                    TextButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                viewModel.onSendOtp {
                                    showFields = true
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Email, "otp")
                        Text("OTP")
                    }
                }
            },
            shape = RoundedCornerShape(22.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.emailError.isError,
            supportingText = uiState.emailError.showMessage()
        )

        AnimatedVisibility(visible = showFields) {
            Column {
                var showOtp by remember { mutableStateOf(false) }
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(field1)
                        .focusProperties { next = field2 }
                        .onFocusChanged { if (!it.isFocused) showOtp = false }
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .onVisible { field1.requestFocus() },
                    value = uiState.otp,
                    onValueChange = { viewModel.onOTPChange(it) },
                    label = { Text(text = hintOtp) },
                    singleLine = true,
                    visualTransformation =  if (showOtp) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showOtp = !showOtp }) {
                            Icon(if (showOtp) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_otp")
                        }
                    },
                    shape = RoundedCornerShape(22.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Next),
                    isError = uiState.otpError.isError,
                    supportingText = uiState.otpError.showMessage()
                )

                var showPassword by remember { mutableStateOf(false) }
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(field2)
                        .focusProperties { next = field3 }
                        .onFocusChanged { if (!it.isFocused) showPassword = false }
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text(text = hintNewPassword) },
                    singleLine = true,
                    visualTransformation =  if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                        }
                    },
                    shape = RoundedCornerShape(22.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    isError = uiState.passwordError.isError,
                    supportingText = uiState.passwordError.showMessage()
                )

                var showPasswordConfirm by remember { mutableStateOf(false) }
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(field3)
                        .onFocusChanged { if (!it.isFocused) showPasswordConfirm = false }
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    value = uiState.passwordConfirm,
                    onValueChange = { viewModel.onPasswordConfirmChange(it) },
                    label = { Text(text = hintPasswordConfirm) },
                    singleLine = true,
                    visualTransformation = if (showPasswordConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPasswordConfirm = !showPasswordConfirm }) {
                            Icon(if (showPasswordConfirm) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password_confirm")
                        }
                    },
                    shape = RoundedCornerShape(22.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = uiState.passwordConfirmError.isError,
                    supportingText = uiState.passwordConfirmError.showMessage()
                )

                OutlinedButton(
                    onClick = { viewModel.onPasswordReset() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                        .padding(bottom = 8.dp, top = 16.dp),
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
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 68.dp),
                        text = "Reset Password",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        fontFamily = comicNeueFamily,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}