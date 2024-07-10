package com.compscicomputations.services.other

import com.compscicomputations.services.other.models.Subscription

internal interface SubscriptionService {
    suspend fun subscribe(subscription: Subscription)
    suspend fun getSubscriptions(): List<Subscription>?
}