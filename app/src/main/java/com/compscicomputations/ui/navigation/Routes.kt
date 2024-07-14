package com.compscicomputations.ui.navigation

import com.compscicomputations.core.ktor_client.auth.models.Usertype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// Routes
@Serializable object Onboarding

// Route for nested graph
@Serializable object Auth
@Serializable object Main

// Routes inside Authentication nested graph
@Serializable object Login
@Serializable object Register
@Serializable object CompleteProfile
@Serializable object Terms
@Serializable data class PasswordReset(val email: String? = null)

// Routes inside Main nested graph
@Serializable object Dashboard
@Serializable object Profile
@Serializable object Help
@Serializable object Feedback
@Serializable object Settings
@Serializable data class DynamicFeatureRoute(
    val moduleName: String,
    val className: String,
    val methodName: String
)