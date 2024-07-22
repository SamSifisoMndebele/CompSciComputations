package com.compscicomputations.client.auth.models

import io.ktor.resources.Resource

@Resource("users")
class Users(val limit: Int = 501) {
    @Resource("me")
    class Me(val parent: Users = Users())

    @Resource("google")
    class Google(val parent: Users = Users())

    @Resource("{id}")
    class Id(val parent: Users = Users(), val id: String)

    @Resource("{email}")
    class Email(val parent: Users = Users(), val email: String)
}

@Resource("files")
class Files {
    @Resource("users")
    class Users(val parent: Files = Files()) {
        @Resource("images")
        class Images(val parent: Users = Users()) {
            @Resource("{id}")
            class Id(val parent: Images = Images(), val id: Int)
        }
    }
}

@Resource("admins")
class Admins {
    @Resource("me")
    class Me(val parent: Admins = Admins())

    @Resource("{id}")
    class Id(val parent: Admins = Admins(), val id: String)

    @Resource("pins")
    class Pins(val parent: Admins = Admins()) {
        @Resource("{email}")
        class Email(val parent: Pins = Pins(), val email: String)
    }
}

@Resource("students")
class Students {
    @Resource("me")
    class Me(val parent: Students = Students())

    @Resource("{id}")
    class Id(val parent: Students = Students(), val id: String)
}