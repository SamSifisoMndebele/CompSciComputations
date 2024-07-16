package com.compscicomputations.authentication

import com.compscicomputations.authentication.google.GoogleAuth
import com.compscicomputations.authentication.google.GoogleToken
import io.ktor.server.auth.*


internal class AuthConfig(name: String?) : AuthenticationProvider.Config(name) {
  internal var googleAuth = GoogleAuth()
  internal fun realm(isAdmin: Boolean) = if (isAdmin) "Access server as admin user." else "Access server as user."
  internal var authenticate: AuthenticationFunction<GoogleToken> = {
    throw NotImplementedError("Auth `validate {...}` function not specified.")
  }

  internal fun validate(block: AuthenticationFunction<GoogleToken>) {
    this.authenticate = block
  }
}