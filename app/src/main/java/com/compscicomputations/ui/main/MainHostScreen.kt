package com.compscicomputations.ui.main

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.compscicomputations.AuthActivity
import com.compscicomputations.MainActivity
import com.compscicomputations.ui.main.dashboard.DashboardScreen
import com.compscicomputations.ui.main.feedback.FeedbackScreen
import com.compscicomputations.ui.main.help.HelpScreen
import com.compscicomputations.ui.main.karnaugh.KarnaughScreen
import com.compscicomputations.ui.main.matrix.MatrixScreen
import com.compscicomputations.ui.main.num_systems.NumSystemsScreen
import com.compscicomputations.ui.main.polish.PolishScreen
import com.compscicomputations.ui.main.profile.ProfileScreen
import com.compscicomputations.ui.main.settings.SettingsScreen

enum class MainNavigation(val route: String) {
    DASHBOARD("dashboard_route"),
    PROFILE("profile_route"),
    NUMBER_SYSTEMS("num_systems_route"),
    POLISH("polish_route"),
    KARNAUGH("karnaugh_route"),
    MATRIX("matrix_route"),
    HELP("help_route"),
    FEEDBACK("feedback_route"),
    SETTINGS("settings_route"),
}

@Composable
fun MainHostScreen(
    activity: MainActivity,
    navController: NavHostController
) {
//    val userInfo = SupabaseModule.provideSupabaseClient().auth.currentSessionOrNull()?.user
    /*var userInfo by remember {
        mutableStateOf<UserInfo?>(null)
    }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                userInfo = SupabaseObject.supabase.auth.currentSessionOrNull()?.user
            } catch (e: Exception) {
                activity.startActivity(Intent(activity, AuthActivity::class.java))
                activity.finishAffinity()
            }
        }
    }*/



    NavHost(
        navController = navController,
        startDestination = MainNavigation.DASHBOARD.route //todo change to DASHBOARD
    ) {
        composable(MainNavigation.DASHBOARD.route) {
            DashboardScreen(
                navigateProfile = {
                    navController.navigate(MainNavigation.PROFILE.route)
                },
                navigateNumSystems = {
                    navController.navigate(MainNavigation.NUMBER_SYSTEMS.route)
                },
                navigatePolish = {
                    navController.navigate(MainNavigation.POLISH.route)
                },
                navigateKarnaugh = {
                    navController.navigate(MainNavigation.KARNAUGH.route)
                },
                navigateMatrix = {
                    navController.navigate(MainNavigation.MATRIX.route)
                },
                navigateHelp = {
                    navController.navigate(MainNavigation.HELP.route)
                },
                navigateFeedback = {
                    navController.navigate(MainNavigation.FEEDBACK.route)
                },
                navigateSettings = {
                    navController.navigate(MainNavigation.SETTINGS.route)
                    throw RuntimeException("Test Crash") // Force a crash
                },
                navigateAuth = {
                    activity.startActivity(Intent(activity, AuthActivity::class.java))
                    activity.finishAffinity()
                }
            )
        }
        composable(MainNavigation.PROFILE.route) {
            ProfileScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                navigateAuth = {
                    activity.startActivity(Intent(activity, AuthActivity::class.java))
                    activity.finishAffinity()
                }
            )
        }
        composable(MainNavigation.NUMBER_SYSTEMS.route) {
            NumSystemsScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(MainNavigation.POLISH.route) {
            PolishScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(MainNavigation.KARNAUGH.route) {
            KarnaughScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(MainNavigation.MATRIX.route) {
            MatrixScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                navigateCramer = {
                    //navController.navigate(MainNavigation.NUMBER_SYSTEMS.route)
                },
                navigateGauss = {
                    //navController.navigate(MainNavigation.POLISH.route)
                },
            )
        }
        /*composable(
            MainNavigation.KARNAUGH.route,
            arguments = listOf(navArgument("email"){
                type = NavType.StringType
                nullable = true
            })
        ) {backStackEntry ->

        }*/
        composable(MainNavigation.HELP.route) {
            HelpScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(MainNavigation.FEEDBACK.route) {
            FeedbackScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(MainNavigation.SETTINGS.route) {
            SettingsScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}