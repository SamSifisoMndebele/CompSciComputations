package com.compscicomputations.authentication.google

import io.ktor.server.auth.Principal

data class GoogleToken(
    val email: String,              //id
    val name: String?,              //displayName
    val givenName: String?,         //givenName
    val familyName: String?,        //familyName
    val pictureUrl: String?,        //profilePictureUri

    val emailVerified: Boolean?,
    val isAdmin: Boolean,
): Principal


//email:                googleIdTokenCredential.id
//displayName:          googleIdTokenCredential.displayName
//givenName:            googleIdTokenCredential.givenName
//familyName:           googleIdTokenCredential.familyName
//profilePictureUri:    googleIdTokenCredential.profilePictureUri

//phoneNumber:          googleIdTokenCredential.phoneNumber
