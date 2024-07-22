package com.compscicomputations.client.auth.models

import io.ktor.resources.Resource

@Resource("users")
class Users {
    @Resource("me")
    class Me(val parent: Users = Users())

    @Resource("google")
    class Google(val parent: Users = Users())
}

@Resource("files")
class Files {
    @Resource("images")
    class Images {
        @Resource("{id}")
        class Id(val parent: Images = Images(), val id: Int)

        @Resource("users/{id}")
        class Users(val parent: Images = Images(), val id: String)
    }
}

@Resource("admin")
class Admin {
    @Resource("pins")
    class Pins(val parent: Admin = Admin()) {
        @Resource("{email}")
        class Email(val parent: Pins = Pins(), val email: String)
    }

    @Resource("users")
    class Users {
        @Resource("{id}")
        class Id(val parent: Users = Users(), val id: String)

        @Resource("{email}")
        class Email(val parent: Users = Users(), val email: String)
    }
}