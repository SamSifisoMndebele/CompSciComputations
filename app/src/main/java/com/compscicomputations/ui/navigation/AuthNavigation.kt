package com.compscicomputations.ui.navigation

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.compscicomputations.di.AppModule.setTermsAccepted
import com.compscicomputations.ui.auth.login.LoginScreen
import com.compscicomputations.ui.auth.login.LoginViewModel
import com.compscicomputations.ui.auth.onboarding.OnboardingScreen
import com.compscicomputations.ui.auth.onboarding.OnboardingViewModel
import com.compscicomputations.ui.auth.register.RegisterScreen
import com.compscicomputations.ui.auth.register.RegisterViewModel
import com.compscicomputations.ui.auth.register.TermsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun NavGraphBuilder.authNavigation(activity: Activity, navController: NavHostController) {
    val preferences = activity.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
    val firstTimeOpenKey = "first_time_open"
    val firstTimeOpen = preferences.getBoolean(firstTimeOpenKey, true)
    navigation<Auth>(startDestination = if (firstTimeOpen) Onboarding else Login) {
        composable<Onboarding> {
            val viewModel: OnboardingViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            OnboardingScreen(
                uiState = uiState,
                navigateRegister = {
                    navController.navigate(route = Register)
                    preferences.edit().putBoolean(firstTimeOpenKey, false).apply()
                },
                navigateLogin = {
                    navController.navigate(route = Login)
                    preferences.edit().putBoolean(firstTimeOpenKey, false).apply()
                }
            )
        }
        composable<Login> {
            val viewModel: LoginViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            LoginScreen(
                viewModel = viewModel,
                uiState = uiState,
                activity = activity,
                navigateUp = if (firstTimeOpen) {{ navController.navigateUp() }} else null,
                navigateRegister = { navController.navigate(route = Register) },
                navigateResetPassword = { email ->
                    navController.navigate(route = PasswordReset(email))
                }
            ) {
                navController.navigate(route = Main) {
                    popUpTo<Auth> { inclusive = true }
                }
            }
        }
        composable<Register> {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val accepted = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("accepted")
            if (accepted != null) {
                viewModel.setTermsAccepted(accepted)
                navController.currentBackStackEntry!!.savedStateHandle["accepted"] = null
            }
            RegisterScreen(
                activity = activity,
                viewModel = viewModel,
                uiState = uiState,
                navigateUp = { navController.navigateUp() },
                navigateTerms = { navController.navigate(route = Terms) }
            ) {
                navController.navigate(route = Dashboard) {
                    popUpTo(route = Auth) { inclusive = true }
                }
            }
        }
        composable<Terms> {
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            TermsScreen(
                navigateUp = { navController.navigateUp() }
            ) { accepted ->
                navController.previousBackStackEntry?.savedStateHandle?.set("accepted", accepted)
                navController.popBackStack()
                coroutineScope.launch(Dispatchers.IO) { context.setTermsAccepted(accepted) }
            }
        }
        composable<PasswordReset> { backStackEntry ->
            val passwordReset: PasswordReset = backStackEntry.toRoute()
        }
    }
}