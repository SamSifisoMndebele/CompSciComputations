package com.compscicomputations.core.ktor_client.publik.models

import io.ktor.resources.*

@Resource("onboarding")
class Onboarding {
    @Resource("items")
    class Items(val parent: Onboarding = Onboarding()) {
        @Resource("{id}")
        class Id(val parent: Items = Items(), val id: Int)
    }
}
