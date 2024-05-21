package com.compscicomputations.ui.main

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.compscicomputations.AuthActivity
import com.compscicomputations.MainActivity
import com.compscicomputations.ui.main.dashboard.DashboardScreen
import com.compscicomputations.ui.main.feedback.FeedbackScreen
import com.compscicomputations.ui.main.help.HelpScreen
import com.compscicomputations.ui.main.profile.ProfileScreen
import com.compscicomputations.ui.main.settings.SettingsScreen
import com.compscicomputations.utils.LoadDynamicFeature

enum class MainNavigation(val route: String) {
    DASHBOARD("dashboard_route"),
    PROFILE("profile_route"),
    HELP("help_route"),
    FEEDBACK("feedback_route"),
    SETTINGS("settings_route"),
    DYNAMIC_FEATURE("dynamic_feature_route"),
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

                navigateDynamicFeature = { packageName, className, composeMethodName ->
                    try {
                        if (composeMethodName == null) {
                            activity.startActivity(Intent().setClassName(packageName, className))
                        } else {
                            navController.navigate(MainNavigation.DYNAMIC_FEATURE.route
                                    + "/$className/$composeMethodName")
                        }
                    } catch (e: Exception) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                        Log.e("MainHostScreen", "navigateDynamicFeature:error", e)
                    }
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

        composable(MainNavigation.DYNAMIC_FEATURE.route+"/{class_name}/{method_name}") {
            LoadDynamicFeature(
                className = it.arguments!!.getString("class_name")!!,
                methodName = it.arguments!!.getString("method_name")!!,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}