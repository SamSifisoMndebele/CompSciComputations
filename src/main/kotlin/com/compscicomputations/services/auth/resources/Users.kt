package com.compscicomputations.services.auth.resources

import com.compscicomputations.services.auth.models.Usertype
import io.ktor.resources.*
import kotlinx.serialization.SerialName

@Resource("users")
class Users {
    @Resource("me")
    class Me(val parent: Users = Users())

    @Resource("{uid}")
    class Uid(val parent: Users = Users(), val uid: String)
}