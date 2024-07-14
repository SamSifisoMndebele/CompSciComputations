package com.compscicomputations.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory

internal class FirebaseAdmin {
    private val auth: FirebaseAuth by inject(FirebaseAuth::class.java)
    private val logger = LoggerFactory.getLogger("FirebaseAdmin")

    internal fun authenticateToken(token: String, asAdmin: Boolean): FirebasePrincipal? {
        return try {
            val firebaseToken = auth.verifyIdTokenAsync(token, false).get()
            val isAdmin = firebaseToken.claims["admin"] as Boolean? ?: false
            if (asAdmin && isAdmin.not()) {
                throw Exception("User is not recognized as an admin.")
            }
            firebaseToken?.let {
                FirebasePrincipal(
                    uid = it.uid,
                    email = it.email,
                    isAdmin = isAdmin
                )
            }
        } catch (e: FirebaseAuthException) {
            logger.warn(e.message)
            null
        }
    }
}