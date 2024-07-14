package com.compscicomputations.firebase

import io.ktor.server.auth.*


internal class FirebaseAuthConfig(name: String?) : AuthenticationProvider.Config(name) {
  internal var admin: FirebaseAdmin = FirebaseAdmin()
  internal var authenticate: AuthenticationFunction<FirebasePrincipal> = {
    throw NotImplementedError("Firebase `validate {...}` function not specified.")
  }

  internal fun validate(block: AuthenticationFunction<FirebasePrincipal>) {
    this.authenticate = block
  }
}