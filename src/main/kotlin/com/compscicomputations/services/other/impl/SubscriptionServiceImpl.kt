package com.compscicomputations.services.other.impl

import com.compscicomputations.plugins.connectToPostgres
import com.compscicomputations.services.other.SubscriptionService
import com.compscicomputations.services.other.models.Subscription
import com.compscicomputations.utils.dbQuery
import java.sql.Connection

internal class SubscriptionServiceImpl : SubscriptionService {

    private val conn: Connection by lazy { connectToPostgres() }

    override suspend fun subscribe(subscription: Subscription): Unit = dbQuery(conn) {
//        Subscriptions.insert {
//            it[email] = subscription.email
//            it[suggestion] = subscription.suggestion
//        }
        TODO("Not yet implemented.")
    }

    override suspend fun getSubscriptions(): List<Subscription>? = dbQuery(conn) {
//        Subscriptions.selectAll()
//            .sortedBy { Subscriptions.subscribedAt }
//            .ifEmpty { null }
//            ?.map {
//                Subscription(
//                    it[Subscriptions.email],
//                    it[Subscriptions.suggestion],
//                    it[Subscriptions.subscribedAt].asString
//                )
//            }
        TODO("Not yet implemented.")
    }
}