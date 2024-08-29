package com.compscicomputations.ui.navigation

import android.app.Activity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.compscicomputations.client.auth.data.source.local.AuthDataStore.firstLaunchFlow
import com.compscicomputations.client.auth.data.source.local.AuthDataStore.setFirstLaunch
import com.compscicomputations.client.auth.data.source.local.AuthDataStore.setTermsAccepted
import com.compscicomputations.client.auth.data.source.local.AuthDataStore.termsAcceptedFlow
import com.compscicomputations.ui.auth.login.LoginScreen
import com.compscicomputations.ui.auth.onboarding.OnboardingScreen
import com.compscicomputations.ui.auth.register.RegisterScreen
import com.compscicomputations.ui.auth.register.RegisterViewModel
import com.compscicomputations.ui.auth.register.TermsScreen
import com.compscicomputations.ui.auth.reset.PasswordResetScreen
import com.compscicomputations.ui.auth.reset.PasswordResetViewModel
import com.compscicomputations.utils.rememberConnectivityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.navigationAuth(
    activity: Activity,
    navController: NavHostController,
    gotoOnboarding: MutableState<Boolean>
) {
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
                activity = activity,
                navigateOnboarding = { navController.navigate(route = Onboarding) { launchSingleTop = true } },
                navigateRegister = { navController.navigate(route = Register) { launchSingleTop = true } },
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
                    coroutineScope.launch(Dispatchers.IO) { context.setFirstLaunch(false) }
                },
                navigateLogin = { firstLaunch ->
                    navController.navigate(route = Login) { launchSingleTop = true }
                    coroutineScope.launch(Dispatchers.IO) { context.setFirstLaunch(firstLaunch) }
                }
            )
        }
        composable<Register> {
            val viewModel: RegisterViewModel = hiltViewModel()
            val context = LocalContext.current
            val termsAccepted by context.termsAcceptedFlow.collectAsStateWithLifecycle(initialValue = false)
            LaunchedEffect(termsAccepted) {
                withContext(Dispatchers.IO) {
                    viewModel.setTermsAccepted(termsAccepted)
                }
            }
            RegisterScreen(
                activity = activity,
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
            val viewModel: PasswordResetViewModel = hiltViewModel()
            passwordReset.email?.let { viewModel.onEmailChange(it) }
            PasswordResetScreen(
                viewModel = viewModel,
                navigateOnboarding = { navController.navigate(route = Onboarding) { launchSingleTop = true } },
                navigateUp = { navController.navigateUp() },
            )
        }
    }
}