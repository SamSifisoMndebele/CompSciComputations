package com.compscicomputations.services.auth.models

import io.ktor.resources.*

@Resource("users")
class Users(val limit: Int = 10) {
    @Resource("me")
    class Me(val parent: Users = Users())

    @Resource("{uid}")
    class Uid(val parent: Users = Users(), val uid: String)
}

@Resource("admins")
class Admins {
    @Resource("me")
    class Me(val parent: Admins = Admins())

    @Resource("{uid}")
    class Uid(val parent: Admins = Admins(), val uid: String)

    @Resource("/pins")
    class Pins(val parent: Admins = Admins()) {
        @Resource("{email}")
        class Email(val parent: Pins = Pins(), val email: String)
    }
}

@Resource("students")
class Students {
    @Resource("me")
    class Me(val parent: Students = Students())

    @Resource("{uid}")
    class Uid(val parent: Students = Students(), val uid: String)
}