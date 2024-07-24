package com.compscicomputations.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.compscicomputations.client.auth.data.source.local.AuthDataStore.firstLaunchFlow
import com.compscicomputations.client.auth.data.source.local.AuthDataStore.setFirstLaunch
import com.compscicomputations.client.auth.data.source.local.AuthDataStore.setTermsAccepted
import com.compscicomputations.client.auth.data.source.local.AuthDataStore.termsAcceptedFlow
import com.compscicomputations.ui.auth.login.LoginScreen
import com.compscicomputations.ui.auth.login.PasswordLoginScreen
import com.compscicomputations.ui.auth.login.PasswordLoginViewModel
import com.compscicomputations.ui.auth.onboarding.OnboardingScreen
import com.compscicomputations.ui.auth.register.RegisterScreen
import com.compscicomputations.ui.auth.register.RegisterViewModel
import com.compscicomputations.ui.auth.register.TermsScreen
import com.compscicomputations.utils.rememberConnectivityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.authNavigation(navController: NavHostController, gotoOnboarding: MutableState<Boolean>) {
    navigation<Auth>(startDestination = Login) {
        composable<Login> {
            val context = LocalContext.current
            val firstLaunch by context.firstLaunchFlow.collectAsState(initial = false)
            val connectivityState by rememberConnectivityState()
            LaunchedEffect(firstLaunch) {
                if (firstLaunch && connectivityState.isAvailable && gotoOnboarding.value) {
                    gotoOnboarding.value = false
                    navController.navigate(route = Onboarding) {
                        popUpTo<Login> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            LoginScreen(
                navigateOnboarding = { navController.navigate(route = Onboarding) { launchSingleTop = true } },
                navigateRegister = { navController.navigate(route = Register) { launchSingleTop = true } },
                navigatePasswordLogin = { email, password ->
                    navController.navigate(route = PasswordLogin(email, password)) { launchSingleTop = true }
                }
            ) {
                navController.navigate(route = Main) {
                    popUpTo<Auth> { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        composable<PasswordLogin> { backStackEntry ->
            val viewModel: PasswordLoginViewModel = hiltViewModel()
            val passwordLogin: PasswordLogin = backStackEntry.toRoute()
            passwordLogin.email?.let { email ->
                viewModel.onEmailChange(email)
                passwordLogin.password?.let { password ->
                    viewModel.onPasswordChange(password)
                    viewModel.onLogin{_,_->}
                }
            }
            PasswordLoginScreen(
                viewModel = viewModel,
                navigateOnboarding = { navController.navigate(route = Onboarding) { launchSingleTop = true } },
                navigateRegister = { navController.navigate(route = Register) { launchSingleTop = true } },
                navigateUp = { navController.navigateUp() },
                navigateResetPassword = { email ->
                    navController.navigate(route = PasswordReset(email)) { launchSingleTop = true }
                },
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

            OnboardingScreen(
                navigateRegister = {
                    navController.navigate(route = Register) { launchSingleTop = true }
                    coroutineScope.launch { context.setFirstLaunch(false) }
                },
                navigateLogin = { firstLaunch ->
                    navController.navigate(route = Login) { launchSingleTop = true }
                    coroutineScope.launch { context.setFirstLaunch(firstLaunch) }
                }
            )
        }

        composable<Register> {
            val viewModel: RegisterViewModel = hiltViewModel()
            val context = LocalContext.current
            val termsAccepted by context.termsAcceptedFlow.collectAsStateWithLifecycle(initialValue = false)
            LaunchedEffect(termsAccepted) {
                viewModel.setTermsAccepted(termsAccepted)
            }
            RegisterScreen(
                viewModel = viewModel,
                navigateUp = { navController.navigateUp() },
                navigateTerms = { navController.navigate(route = Terms) { launchSingleTop = true } },
                navigateOnboarding = { navController.navigate(route = Onboarding) { launchSingleTop = true } },
                navigateMain = {
                    navController.navigate(route = Main) {
                        popUpTo<Auth> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )

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