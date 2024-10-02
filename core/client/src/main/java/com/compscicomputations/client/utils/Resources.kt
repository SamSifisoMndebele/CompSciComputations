package com.compscicomputations.client.utils

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
    @Resource("id/{id}")
    class Id(val parent: Feedback = Feedback(), val id: Int)
    @Resource("email/{email}")
    class Email(val parent: Feedback = Feedback(), val email: String)
}