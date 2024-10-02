package com.compscicomputations.ui.navigation

import com.compscicomputations.client.publik.data.model.remote.RemoteFeedback
import com.compscicomputations.client.utils.Feedback
import kotlinx.serialization.Serializable


/// Routes
@Serializable object Splash
@Serializable object Auth
@Serializable object Main

/// Routes inside Auth nested graph
@Serializable object Login
@Serializable object Onboarding
@Serializable object Register
@Serializable object Terms
@Serializable data class PasswordReset(val email: String?)

/// Routes inside Main nested graph
@Serializable object Dashboard
@Serializable object Profile
@Serializable object Feedback
@Serializable object Settings