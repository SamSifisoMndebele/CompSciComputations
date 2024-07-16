package com.compscicomputations.authentication

import com.compscicomputations.authentication.google.GoogleAuth
import com.compscicomputations.services.auth.models.response.User
import io.ktor.server.auth.*


internal class AuthConfig(name: String?) : AuthenticationProvider.Config(name) {
  internal var googleAuth = GoogleAuth()
  internal fun realm(isAdmin: Boolean) = if (isAdmin) "Access server as admin user." else "Access server as user."
  internal var authenticate: AuthenticationFunction<User> = {
    throw NotImplementedError("Auth `validate {...}` function not specified.")
  }

  internal fun validate(block: AuthenticationFunction<User>) {
    this.authenticate = block
  }
}