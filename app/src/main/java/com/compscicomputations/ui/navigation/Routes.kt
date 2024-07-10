package com.compscicomputations.ui.navigation

import com.compscicomputations.core.ktor_client.auth.models.Usertype
import kotlinx.serialization.Serializable


// Routes
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
@Serializable data class Profile(
    val email: String,
    val displayName: String,
    val photoUrl: String?
)
@Serializable object Help
@Serializable object Feedback
@Serializable object Settings
@Serializable data class DynamicFeatureRoute(
    val moduleName: String,
    val className: String,
    val methodName: String
)