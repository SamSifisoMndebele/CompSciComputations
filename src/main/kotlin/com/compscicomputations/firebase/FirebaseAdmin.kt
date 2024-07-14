package com.compscicomputations.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory

internal class FirebaseAdmin {
    private val auth: FirebaseAuth by inject(FirebaseAuth::class.java)
    private val logger = LoggerFactory.getLogger("FirebaseAdmin")

    internal fun authenticateToken(token: String, isAdmin: Boolean): FirebasePrincipal? {
        return try {
            val tokenResult = auth.verifyIdTokenAsync(token, false).get()
            val firebaseToken = when {
                isAdmin && tokenResult.claims["admin"] as Boolean? != true -> throw Exception("User is not recognized as an admin.")
                else -> tokenResult
            }
            firebaseToken?.let {
                FirebasePrincipal(
                    uid = it.uid,
                    isAdmin = it.claims["admin"] as Boolean? ?: false
                )
            }
        } catch (e: FirebaseAuthException) {
            logger.warn(e.message)
            null
        }
    }
}