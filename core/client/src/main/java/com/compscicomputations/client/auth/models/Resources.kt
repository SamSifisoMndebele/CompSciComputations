package com.compscicomputations.client.auth.models

import io.ktor.resources.Resource

@Resource("users")
class Users(val limit: Int = 10) {
    @Resource("me")
    class Me(val parent: Users = Users())

    @Resource("google")
    class Google(val parent: Users = Users())

    @Resource("{id}")
    class Id(val parent: Users = Users(), val id: String)

    @Resource("{email}")
    class Email(val parent: Users = Users(), val email: String)
}