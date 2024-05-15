package com.compscicomputations.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compscicomputations.AuthActivity
import com.compscicomputations.MainActivity
import com.compscicomputations.ui.auth.login.LoginScreen
import com.compscicomputations.ui.auth.register.RegisterScreen
import com.compscicomputations.ui.auth.register.RegisterViewModel
import com.compscicomputations.ui.auth.register.TermsScreen
import com.compscicomputations.ui.auth.reset.PasswordResetScreen
import com.compscicomputations.ui.auth.reset.ResetUiEvent
import com.compscicomputations.ui.auth.reset.ResetViewModel
import com.compscicomputations.ui.auth.welcome.WelcomeScreen
import com.compscicomputations.utils.navigate

enum class AuthNavigation(val route: String) {
    WELCOME("welcome_route"),
    LOGIN("login_route"),
    REGISTER("register_route"),
    TERMS("terms_route"),
    PASSWORD_RESET("password_reset_route"),
}

@Composable
fun AuthHostScreen(
    activity: AuthActivity,
    navController : NavHostController = rememberNavController()
) {
    /*val supabase = createSupabaseClient(
        supabaseUrl = "https://qgidphztnqzgabsvvxmf.supabase.co",
        supabaseKey = BuildConfig.SUPABASE_KEY
    ) {
        install(Auth)
    }*/

    NavHost(
        navController = navController,
        startDestination = AuthNavigation.LOGIN.route //todo change to LOGIN
    ) {
        composable(AuthNavigation.WELCOME.route) {
            WelcomeScreen {
                navController.navigate(AuthNavigation.LOGIN.route)
            }
        }
        composable(AuthNavigation.LOGIN.route) {
            LoginScreen(
                navigateRegister = { navController.navigate(AuthNavigation.REGISTER.route) },
                navigateResetPassword = { email ->
                    val bundle = Bundle().apply { putString("email", email) }
                    navController.navigate(route = AuthNavigation.PASSWORD_RESET.route, args = bundle)
                },
                navigateMain = {
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                    activity.finishAffinity()
                }
            )
        }
        composable(AuthNavigation.REGISTER.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val accepted = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("accepted")
            if (accepted != null) {
                viewModel.setTermsAccepted(accepted)
                navController.currentBackStackEntry!!.savedStateHandle["accepted"] = null
            }
            RegisterScreen(
                viewModel = viewModel,
                navigateUp = { navController.navigateUp() },
                navigateTerms = { navController.navigate(AuthNavigation.TERMS.route) }
            )
        }
        composable(
            AuthNavigation.PASSWORD_RESET.route,
            arguments = listOf(navArgument("email"){
                type = NavType.StringType
                nullable = true
            })
        ) {backStackEntry ->
            val resetViewModel: ResetViewModel = viewModel()
            val resetUiState by resetViewModel.uiState.collectAsStateWithLifecycle()

            val email = backStackEntry.arguments?.getString("email")
            if (email != null) resetViewModel.onEvent(ResetUiEvent.OnEmailChange(email))

            PasswordResetScreen(
                uiState = resetUiState,
                onEvent = { resetViewModel.onEvent(it) },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(AuthNavigation.TERMS.route) {
            TermsScreen(
                navigateUp = { navController.navigateUp() },
                acceptTerms = { accepted ->
                    if (accepted) {
                        navController.previousBackStackEntry?.savedStateHandle?.set("accepted", true)
                        navController.popBackStack()
                    } else {
                        navController.previousBackStackEntry?.savedStateHandle?.set("accepted", false)
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}