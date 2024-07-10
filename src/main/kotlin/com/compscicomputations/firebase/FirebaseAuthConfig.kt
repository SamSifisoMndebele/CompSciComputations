package com.compscicomputations.firebase

import com.compscicomputations.services.auth.models.FirebaseUser
import io.ktor.server.auth.*


internal class FirebaseAuthConfig(name: String?) : AuthenticationProvider.Config(name) {
  internal var admin: FirebaseAdmin = FirebaseAdmin()
  internal var authenticate: AuthenticationFunction<FirebaseUser> = {
    throw NotImplementedError("Firebase validate function not specified.")
  }

  internal var realm: String = "Ktor Server"

  internal fun validate(block: AuthenticationFunction<FirebaseUser>) {
    this.authenticate = block
  }
}