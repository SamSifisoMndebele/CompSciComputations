package com.compscicomputations.plugins

import io.ktor.resources.Resource

@Resource("users")
class Users {
    @Resource("me")
    class Me(val parent: Users = Users())

    @Resource("{id}")
    class Id(val parent: Users = Users(), val id: String)

    @Resource("password-reset")
    class PasswordReset(val parent: Users = Users()) {
        @Resource("{email}")
        class Email(val parent: PasswordReset = PasswordReset(), val email: String)
    }

    @Resource("email/{email}")
    class Email(val parent: Users = Users(), val email: String) {
        @Resource("otp")
        class Otp(val parent: Email)

    }

    @Resource("delete")
    class Delete(val parent: Users = Users())
}


@Resource("onboarding")
class Onboarding {
    @Resource("items")
    class Items(val parent: Onboarding = Onboarding()) {
        @Resource("{id}")
        class Id(val parent: Items = Items(), val id: Int)
        @Resource("except")
        class Except(val parent: Items = Items())
    }
}


@Resource("feedback")
class Feedback {
    @Resource("image/{id}")
    class Image(val parent: Feedback = Feedback(), val id: Int)
    @Resource("id/{id}")
    class Id(val parent: Feedback = Feedback(), val id: Int)
    @Resource("email/{email}")
    class Email(val parent: Feedback = Feedback(), val email: String)
}


//@Resource("admin")
//class Admin {
//    @Resource("pins")
//    class Pins(val parent: Admin = Admin()) {
//        @Resource("{email}")
//        class Email(val parent: Pins = Pins(), val email: String)
//    }
//
//    @Resource("users")
//    class Users {
//        @Resource("{id}")
//        class Id(val parent: Users = Users(), val id: Int)
//
//        @Resource("{email}")
//        class Email(val parent: Users = Users(), val email: String)
//    }
//}