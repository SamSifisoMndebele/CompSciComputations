package com.compscicomputations.firebase

import com.compscicomputations.services.auth.models.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory

internal class FirebaseAdmin {
    private val auth: FirebaseAuth by inject(FirebaseAuth::class.java)
    private val logger = LoggerFactory.getLogger("FirebaseAdmin")

    internal fun authenticateToken(token: String, isAdmin: Boolean): FirebaseUser? {
        return try {
            val tokenResult = auth.verifyIdToken(token)
            val firebaseToken = when {
                isAdmin && tokenResult.claims["admin"] as Boolean? != true ->
                    throw Exception("User is not recognized as an admin.")
                else -> tokenResult
            }
            firebaseToken?.let {
                FirebaseUser(
                    uid = it.uid,
                    displayName = it.name,
                    photoUrl = it.picture,
                    email = it.email,
                    claims = it.claims,
                    isEmailVerified = it.isEmailVerified
                )
            }
        } catch (e: FirebaseAuthException) {
            logger.warn(e.message)
            null
        }
    }
}