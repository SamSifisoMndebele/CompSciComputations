package com.compscicomputations.client.utils

import io.ktor.resources.Resource

@Resource("users")
class Users {
    @Resource("me")
    class Me(val parent: Users = Users())

    @Resource("{id}")
    class Id(val parent: Users = Users(), val id: String) {
        @Resource("image")
        class Image(val parent: Id)
    }

    @Resource("google")
    class Google(val parent: Users = Users())
}


@Resource("onboarding")
class Onboarding {
    @Resource("items")
    class Items(val parent: Onboarding = Onboarding()) {
        @Resource("{id}")
        class Id(val parent: Items = Items(), val id: Int)
    }
}