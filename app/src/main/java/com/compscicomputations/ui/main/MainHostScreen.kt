package com.compscicomputations.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.compscicomputations.MainActivity
import com.compscicomputations.ui.main.dashboard.DashboardScreen
import com.compscicomputations.ui.main.dashboard.DashboardViewModel
import com.compscicomputations.ui.main.karnaugh.KarnaughScreen
import com.compscicomputations.ui.main.num_system.NumSystemsScreen
import com.compscicomputations.ui.main.profile.ProfileScreen

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
    NavHost(
        navController = navController,
        startDestination = MainNavigation.KARNAUGH.route //todo change to DASHBOARD
    ) {
        composable(MainNavigation.DASHBOARD.route) {
            val dashboardViewModel: DashboardViewModel = viewModel()
            val dashboardUiState by dashboardViewModel.uiState.collectAsStateWithLifecycle()
            DashboardScreen(
                uiState = dashboardUiState,
                navigateProfile = {
                    navController.navigate(MainNavigation.PROFILE.route)
                },
                navigateNumStystems = {
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
                }
            ) {
                dashboardViewModel.onEvent(it)
            }
        }
        composable(MainNavigation.PROFILE.route) {
            ProfileScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            ) {

            }
        }
        composable(MainNavigation.NUMBER_SYSTEMS.route) {
            NumSystemsScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(MainNavigation.POLISH.route) {

        }
        composable(MainNavigation.KARNAUGH.route) {
            KarnaughScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(MainNavigation.MATRIX.route) {

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

        }
        composable(MainNavigation.FEEDBACK.route) {

        }
        composable(MainNavigation.SETTINGS.route) {

        }
    }
}