package com.compscicomputations.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.compscicomputations.core.ktor_client.auth.AuthDataStore.firstLaunchFlow
import com.compscicomputations.core.ktor_client.auth.AuthDataStore.setFirstLaunch
import com.compscicomputations.core.ktor_client.auth.AuthDataStore.setTermsAccepted
import com.compscicomputations.core.ktor_client.auth.AuthDataStore.termsAcceptedFlow
import com.compscicomputations.ui.auth.login.LoginScreen
import com.compscicomputations.ui.auth.login.LoginViewModel
import com.compscicomputations.ui.auth.onboarding.OnboardingScreen
import com.compscicomputations.ui.auth.onboarding.OnboardingViewModel
import com.compscicomputations.ui.auth.register.CompleteProfileScreen
import com.compscicomputations.ui.auth.register.CompleteProfileViewModel
import com.compscicomputations.ui.auth.register.RegisterScreen
import com.compscicomputations.ui.auth.register.RegisterViewModel
import com.compscicomputations.ui.auth.register.TermsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun NavGraphBuilder.authNavigation(navController: NavHostController) {
    navigation<Auth>(startDestination = Login) {
        composable<Login> {
            val context = LocalContext.current
            val firstLaunch by context.firstLaunchFlow.collectAsStateWithLifecycle(initialValue = false)
            LaunchedEffect(firstLaunch) {
                if (firstLaunch) navController.navigate(route = Onboarding) {
                    popUpTo<Login> { inclusive = true }
                }
            }
            val viewModel: LoginViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            LoginScreen(
                viewModel = viewModel,
                uiState = uiState,
                navigateOnboarding = { navController.navigate(route = Onboarding) },
                navigateRegister = { navController.navigate(route = Register) },
                navigateResetPassword = { email ->
                    navController.navigate(route = PasswordReset(email))
                },
                navigateCompleteProfile = {
                    navController.navigate(route = CompleteProfile) {
                        popUpTo<Auth> { inclusive = false }
                    }
                }
            ) {
                navController.navigate(route = Main) {
                    popUpTo<Auth> { inclusive = true }
                }
            }
        }
        composable<Onboarding> {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val viewModel: OnboardingViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            OnboardingScreen(
                uiState = uiState,
                navigateRegister = {
                    coroutineScope.launch(Dispatchers.IO) { context.setFirstLaunch(false) }
                    navController.navigate(route = Register)
                },
                navigateLogin = {
                    coroutineScope.launch(Dispatchers.IO) { context.setFirstLaunch(false) }
                    navController.navigate(route = Login)
                }
            )
        }

        composable<Register> {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current
            val termsAccepted by context.termsAcceptedFlow.collectAsStateWithLifecycle(initialValue = false)
            LaunchedEffect(termsAccepted) {
                viewModel.setTermsAccepted(termsAccepted)
            }
            RegisterScreen(
                viewModel = viewModel,
                uiState = uiState,
                navigateUp = { navController.navigateUp() },
                navigateOnboarding = { navController.navigate(route = Onboarding) },
                navigateTerms = { navController.navigate(route = Terms) },
                navigateCompleteProfile = {
                    navController.navigate(route = CompleteProfile) {
                        popUpTo<Auth> { inclusive = false }
                    }
                }
            )
        }
        composable<CompleteProfile> {
            val viewModel: CompleteProfileViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            CompleteProfileScreen(
                viewModel = viewModel,
                uiState = uiState,
                navigateUp = { navController.navigateUp() },
            ) {
                navController.navigate(route = Dashboard) {
                    popUpTo(route = Auth) { inclusive = true }
                }
            }
        }
        composable<Terms> {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            TermsScreen(
                navigateUp = { navController.navigateUp() }
            ) { accepted ->
                navController.popBackStack()
                coroutineScope.launch(Dispatchers.IO) { context.setTermsAccepted(accepted) }
            }
        }
        composable<PasswordReset> { backStackEntry ->
            val passwordReset: PasswordReset = backStackEntry.toRoute()
        }
    }
}