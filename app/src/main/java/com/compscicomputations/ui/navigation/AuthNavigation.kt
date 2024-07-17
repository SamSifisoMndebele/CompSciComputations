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
//            val firstLaunch by context.firstLaunchFlow.collectAsStateWithLifecycle(initialValue = false)
            val firstLaunch  = true
            LaunchedEffect(firstLaunch) {
                if (firstLaunch) navController.navigate(route = Onboarding) {
                    popUpTo<Login> { inclusive = true }
                    launchSingleTop = true
                }
            }
            val viewModel: LoginViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            LoginScreen(
                viewModel = viewModel,
                uiState = uiState,
                navigateOnboarding = { navController.navigate(route = Onboarding) { launchSingleTop = true } },
                navigateRegister = { navController.navigate(route = Register) { launchSingleTop = true } },
                navigateResetPassword = { email ->
                    navController.navigate(route = PasswordReset(email)) { launchSingleTop = true }
                },
                navigateCompleteProfile = {
                    navController.navigate(route = CompleteProfile) {
                        popUpTo<Auth> { inclusive = false }
                        launchSingleTop = true
                    }
                }
            ) {
                navController.navigate(route = Main) {
                    popUpTo<Auth> { inclusive = true }
                    launchSingleTop = true
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
                    navController.navigate(route = Register) { launchSingleTop = true }
                },
                navigateLogin = {
                    coroutineScope.launch(Dispatchers.IO) { context.setFirstLaunch(false) }
                    navController.navigate(route = Login) { launchSingleTop = true }
                }
            )
        }

        composable<Register> {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RegisterScreen(
                viewModel = viewModel,
                uiState = uiState,
                navigateUp = { navController.navigateUp() },
                navigateOnboarding = { navController.navigate(route = Onboarding) { launchSingleTop = true } },
                navigateCompleteProfile = {
                    navController.navigate(route = CompleteProfile) {
                        popUpTo<Auth> { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<CompleteProfile> {
            val viewModel: CompleteProfileViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current
            val termsAccepted by context.termsAcceptedFlow.collectAsStateWithLifecycle(initialValue = false)
            LaunchedEffect(termsAccepted) {
                viewModel.setTermsAccepted(termsAccepted)
            }
            CompleteProfileScreen(
                viewModel = viewModel,
                uiState = uiState,
                navigateUp = { navController.navigateUp() },
                navigateTerms = { navController.navigate(route = Terms) { launchSingleTop = true } },
            ) {
                navController.navigate(route = Dashboard) {
                    popUpTo(route = Auth) { inclusive = true }
                    launchSingleTop = true
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