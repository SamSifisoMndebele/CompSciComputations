package com.compscicomputations.ui.navigation

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.compscicomputations.client.publik.data.model.DynamicFeature
import com.compscicomputations.ui.main.dashboard.DashboardScreen
import com.compscicomputations.ui.main.feedback.FeedbackScreen
import com.compscicomputations.ui.main.profile.ProfileScreen
import com.compscicomputations.ui.main.profile.ProfileViewModel
import com.compscicomputations.ui.main.settings.SettingsScreen

fun NavGraphBuilder.navigationMain(navController: NavHostController) {
    navigation<Main>(startDestination = Dashboard) {
        composable<Dashboard> {
            val context = LocalContext.current
            DashboardScreen(
                navigateProfile = { navController.navigate(route = Profile) },
                navigateFeedback = { navController.navigate(route = Feedback) },
                navigateSettings = { navController.navigate(route = Settings) },
                navigateDynamicFeature = { feature ->
                    try {
                        val activityClass = Class.forName(feature.className)
                        context.startActivity(Intent().setClass(context, activityClass))
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                        Log.e("MainNavigation", "navigateDynamicFeature::error", e)
                    }
                },
            )
        }
        composable<Profile> {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                viewModel = viewModel,
                navigateUp = { navController.navigateUp() },
                navigateAuth = {
                    navController.navigate(route = Auth) {
                        popUpTo<Main> { inclusive = true }
                    }
                }
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
    }
}