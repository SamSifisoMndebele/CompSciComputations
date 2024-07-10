package com.compscicomputations.core.ktor_client.auth

import android.app.Activity

interface AuthRepository {
    /**
     * Login with email and password
     * @throws NoSuchElementException if there's no user wih email.
     * @throws IllegalArgumentException if wrong credentials
     */
    suspend fun login(email: String, password: String)

    /**
     * Login with google credentials.
     */
    suspend fun loginWithGoogle(activity: Activity)

    /**
     * Logout the user with email and password.
     */
    suspend fun logout()

    /**
     * Register a user using
     */
    suspend fun register()

    /**
     * Register a user using google credentials.
     */
    suspend fun registerWithGoogle(activity: Activity)
}