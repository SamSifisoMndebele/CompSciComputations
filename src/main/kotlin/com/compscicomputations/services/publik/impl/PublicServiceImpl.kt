package com.compscicomputations.services.publik.impl

import com.compscicomputations.plugins.connectToPostgres
import com.compscicomputations.services.publik.PublicService
import com.compscicomputations.services.publik.models.SourceType
import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.services.publik.models.requests.UpdateOnboardingItem
import com.compscicomputations.services.publik.models.response.OnboardingItem
import com.compscicomputations.utils.dbQuery
import com.compscicomputations.utils.executeQuery
import com.compscicomputations.utils.executeUpdate
import java.sql.Connection
import java.sql.ResultSet

class PublicServiceImpl : PublicService {

    private var connection: Connection? = null
    private val conn : Connection
        get() {
            if (connection == null || connection!!.isClosed) {
                connection = connectToPostgres()
            }
            return connection!!
        }

    override suspend fun createOnboardingItem(item: NewOnboardingItem) = dbQuery(conn) {
        executeUpdate("""
            insert into public.onboarding_items(source_url, title, description, type) 
            values (?, ?, ?, ?::public.source_type)
        """.trimIndent()) {
            setString(1, item.sourceUrl)
            setString(2, item.title)
            setString(3, item.description)
            setString(4, item.type.name)
        }
    }

    override suspend fun updateOnboardingItem(id: Int, item: UpdateOnboardingItem) = dbQuery(conn) {
        val current = getOnboardingItem(id) ?: return@dbQuery
        executeUpdate("""
            update public.onboarding_items
            set source_url = ?,
                title = ?,
                description = ?,
                type = ?::public.source_type
            where id = ?
        """.trimIndent()) {
            setString(1, item.sourceUrl ?: current.sourceUrl)
            setString(2, item.title ?: current.title)
            setString(3, item.description ?: current.description)
            setString(4, item.type?.name ?: current.type.name)
            setInt(5, id)
        }
    }

    private fun ResultSet.toOnboardingItem(): OnboardingItem {
        return OnboardingItem(
            id = getInt("id"),
            sourceUrl = getString("source_url"),
            title = getString("title"),
            description = getString("description"),
            type = SourceType.valueOf(getObject("type").toString()),
        )
    }

    override suspend fun getOnboardingItem(id: Int): OnboardingItem? = dbQuery(conn) {
        executeQuery("select * from public.onboarding_items where id = ?", {
            setInt(1, id)
        }) {
            it.toOnboardingItem()
        }?.singleOrNull()
    }

    override suspend fun getOnboardingItems(): List<OnboardingItem> = dbQuery(conn) {
        executeQuery("select * from public.onboarding_items") {
            it.toOnboardingItem()
        } ?: emptyList()
    }

    override suspend fun deleteOnboardingItems(id: Int) = dbQuery(conn) {
        executeUpdate("delete from public.onboarding_items where id = ?") {
            setInt(1, id)
        }
    }
}