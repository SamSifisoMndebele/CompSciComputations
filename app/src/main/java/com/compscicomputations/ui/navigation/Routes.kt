package com.compscicomputations.ui.navigation

import kotlinx.serialization.Serializable


// Routes
@Serializable object Splash
@Serializable object Onboarding

// Route for nested graph
@Serializable object Auth
@Serializable object Main

// Routes inside Authentication nested graph
@Serializable object Login
@Serializable object Register
@Serializable object Terms
@Serializable data class PasswordReset(val email: String? = null)

// Routes inside Main nested graph
@Serializable object Dashboard
@Serializable object Profile
@Serializable object Help
@Serializable object Feedback
@Serializable object Settings