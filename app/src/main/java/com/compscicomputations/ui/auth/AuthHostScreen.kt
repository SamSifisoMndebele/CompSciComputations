package com.compscicomputations.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.compscicomputations.AuthActivity
import com.compscicomputations.MainActivity
import com.compscicomputations.R
import com.compscicomputations.ui.auth.login.LoginScreen
import com.compscicomputations.ui.auth.login.LoginViewModel
import com.compscicomputations.ui.auth.register.RegisterScreen
import com.compscicomputations.ui.auth.register.RegisterViewModel
import com.compscicomputations.ui.auth.register.TermsScreen
import com.compscicomputations.ui.auth.reset.PasswordResetScreen
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
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.login_anim
        )
    )
    val lottieProgress by animateLottieCompositionAsState(
        lottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )
    val preferences = activity.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
    val firstTime = "first_time_open"

    NavHost(
        navController = navController,
        startDestination = AuthNavigation.LOGIN.route
    ) {
        composable(AuthNavigation.WELCOME.route) {
            WelcomeScreen {
                navController.navigate(AuthNavigation.LOGIN.route)
                preferences.edit().putBoolean(firstTime, false).apply()
            }
        }
        composable(AuthNavigation.LOGIN.route) {
            val firstTimeOpen = preferences.getBoolean(firstTime, true)
            if (firstTimeOpen) {
                navController.navigate(route = AuthNavigation.WELCOME.route)
                return@composable
            }
            val viewModel: LoginViewModel = hiltViewModel()
            val emailLink = activity.intent.data?.toString()
            if (emailLink != null) viewModel.onLinkLogin(emailLink)

            LoginScreen(
                viewModel = viewModel,
                lottieComposition = lottieComposition,
                lottieProgress = lottieProgress,
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
                lottieComposition = lottieComposition,
                lottieProgress = lottieProgress,
                viewModel = viewModel,
                navigateUp = { navController.navigateUp() },
                navigateTerms = { navController.navigate(AuthNavigation.TERMS.route) },
                navigateMain = {
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                    activity.finishAffinity()
                }
            )
        }
        composable(
            AuthNavigation.PASSWORD_RESET.route,
            arguments = listOf(navArgument("email"){
                type = NavType.StringType
                nullable = true
            })
        ) {backStackEntry ->
            val viewModel: ResetViewModel = hiltViewModel()
            val email = backStackEntry.arguments?.getString("email")
            if (email != null) viewModel.onEmailChange(email)
            PasswordResetScreen(
                viewModel = viewModel,
                lottieComposition = lottieComposition,
                lottieProgress = lottieProgress,
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