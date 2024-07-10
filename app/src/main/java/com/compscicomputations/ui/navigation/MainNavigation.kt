package com.compscicomputations.ui.navigation

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.compscicomputations.BuildConfig
import com.compscicomputations.ui.main.dashboard.DashboardScreen
import com.compscicomputations.ui.main.dashboard.DashboardViewModel
import com.compscicomputations.ui.main.feedback.FeedbackScreen
import com.compscicomputations.ui.main.help.HelpScreen
import com.compscicomputations.ui.main.profile.ProfileScreen
import com.compscicomputations.ui.main.profile.ProfileViewModel
import com.compscicomputations.ui.main.settings.SettingsScreen
import com.compscicomputations.ui.utils.loadDynamicFeature

fun NavGraphBuilder.mainNavigation(activity: Activity, navController: NavHostController) {
    navigation<Main>(startDestination = Dashboard) {
        composable<Dashboard> {
            val viewModel: DashboardViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            if (!uiState.isSignedIn) {
                navController.navigate(route = Auth) {
                    popUpTo<Main> { inclusive = true }
                }
                return@composable
            }
            DashboardScreen(
                viewModel = viewModel,
                uiState = uiState,
                navigateProfile = { email, displayName, photoUrl ->
                    navController.navigate(route = Profile(email, displayName, photoUrl))
                },
                navigateHelp = { navController.navigate(route = Help) },
                navigateFeedback = { navController.navigate(route = Feedback) },
                navigateSettings = { navController.navigate(route = Settings) },
                navigateDynamicFeature = { feature ->
                    try {
                        if (feature.methodName == null) {
                            activity.startActivity(Intent().setClassName(BuildConfig.APPLICATION_ID,
                                "${feature.module}.${feature.className}"))
                        } else {
                            navController.navigate(route = DynamicFeatureRoute(
                                moduleName = feature.moduleName,
                                className = feature.className,
                                methodName = feature.methodName!!
                            ))
                        }
                    } catch (e: Exception) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                        Log.e("MainHostScreen", "navigateDynamicFeature:error", e)
                    }
                },
            )
        }
        composable<Profile> { backStackEntry ->
            val viewModel: ProfileViewModel = hiltViewModel()
            viewModel.setProfile(backStackEntry.toRoute<Profile>())
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ProfileScreen(
                viewModel = viewModel,
                uiState = uiState,
                navigateUp = { navController.navigateUp() },
                navigateAuth = {
                    navController.navigate(route = Auth) {
                        popUpTo<Main> { inclusive = true }
                    }
                }
            )
        }
        composable<Help> {
            HelpScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
        composable<Feedback> {
            FeedbackScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
        composable<Settings> {
            SettingsScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
        composable<DynamicFeatureRoute> { backStackEntry ->
            val dynamicFeature: DynamicFeatureRoute = backStackEntry.toRoute()
            loadDynamicFeature(
                className = dynamicFeature.className,
                methodName = dynamicFeature.methodName,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}