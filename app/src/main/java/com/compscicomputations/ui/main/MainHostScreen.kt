package com.compscicomputations.ui.main

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.compscicomputations.AuthActivity
import com.compscicomputations.BuildConfig
import com.compscicomputations.MainActivity
import com.compscicomputations.ui.main.dashboard.DashboardScreen
import com.compscicomputations.ui.main.feedback.FeedbackScreen
import com.compscicomputations.ui.main.help.HelpScreen
import com.compscicomputations.ui.main.profile.ProfileScreen
import com.compscicomputations.ui.main.settings.SettingsScreen
import com.compscicomputations.utils.loadDynamicFeatureComposable

enum class MainNavigation(val route: String) {
    DASHBOARD("dashboard_route"),
    PROFILE("profile_route"),
    HELP("help_route"),
    FEEDBACK("feedback_route"),
    SETTINGS("settings_route"),

    //Dynamic features
    NUMBER_SYSTEMS("num_systems_route"),
    POLISH("polish_route"),
    KARNAUGH("karnaugh_route"),
    MATRIX("matrix_route"),
}

@Composable
fun MainHostScreen(
    activity: MainActivity,
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = MainNavigation.DASHBOARD.route //todo change to DASHBOARD
    ) {
        composable(MainNavigation.DASHBOARD.route) {
            DashboardScreen(
                navigateProfile = {
                    navController.navigate(MainNavigation.PROFILE.route)
//                    {
//                        popUpTo(navController.graph.startDestinationId)
//                        launchSingleTop = true
//                    }
                },
                navigateHelp = {
                    navController.navigate(MainNavigation.HELP.route)
                },
                navigateFeedback = {
                    navController.navigate(MainNavigation.FEEDBACK.route)
                },
                navigateSettings = {
                    navController.navigate(MainNavigation.SETTINGS.route)
                },
                navigateAuth = {
                    activity.startActivity(Intent(activity, AuthActivity::class.java))
                    activity.finishAffinity()
                },

                //navigateDynamicFeature
                navigateNumSystems = {
                    navController.navigate(MainNavigation.NUMBER_SYSTEMS.route)
                },
                navigatePolish = {
                    navController.navigate(MainNavigation.POLISH.route)
                },
                navigateKarnaugh = {
                    val packageName = BuildConfig.APPLICATION_ID
                    val className = "$packageName.karnaughmaps.KarnaughActivity"
                    activity.startActivity(Intent().setClassName(packageName, className))
                },
                navigateMatrix = {
                    navController.navigate(MainNavigation.MATRIX.route)
                },
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


        composable(MainNavigation.NUMBER_SYSTEMS.route) {
            val packageName = BuildConfig.APPLICATION_ID
            val className = "$packageName.number_systems.NumberSystemsKt"
            val composeMethodName = "NumberSystemsScreen"
            loadDynamicFeatureComposable(
                className = className,
                methodName = composeMethodName,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(MainNavigation.POLISH.route) {
            val packageName = BuildConfig.APPLICATION_ID
            val className = "$packageName.polish_expressions.PolishExpressionsKt"
            val composeMethodName = "PolishExpressionsScreen"
            loadDynamicFeatureComposable(
                className = className,
                methodName = composeMethodName,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(MainNavigation.KARNAUGH.route) {
//            com.compscicomputations.karnaughmaps.KarnaughScreen(
//                navigateUp = {
//                    navController.navigateUp()
//                }
//            )
        }
        composable(MainNavigation.MATRIX.route) {
            val packageName = BuildConfig.APPLICATION_ID
            val className = "$packageName.matrix_methods.MatrixMethodsKt"
            val composeMethodName = "MatrixMethodsScreen"
            loadDynamicFeatureComposable(
                className = className,
                methodName = composeMethodName,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}