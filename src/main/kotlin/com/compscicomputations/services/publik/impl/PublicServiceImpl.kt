package com.compscicomputations.services.publik.impl

import com.compscicomputations.plugins.connectToPostgres
import com.compscicomputations.services.publik.PublicService
import com.compscicomputations.services.publik.models.SourceType
import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.services.publik.models.requests.UpdateOnboardingItem
import com.compscicomputations.services.publik.models.response.OnboardingItem
import com.compscicomputations.utils.*
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

    companion object {
        private fun ResultSet.getOnboardingItem(): OnboardingItem {
            return OnboardingItem(
                id = getInt("id"),
                sourceUrl = getString("source_url"),
                title = getString("title"),
                description = getString("description"),
                sourceType = SourceType.valueOf(getObject("source_type").toString()),
            )
        }
    }

    override suspend fun createOnboardingItem(item: NewOnboardingItem): Unit = dbQuery(conn) {
        update("""
            insert into public.sql.onboarding_items(source_url, title, description, source_type) 
            values (?, ?, ?, ?::public.sql.source_type)
        """.trimIndent()) {
            setString(1, item.sourceUrl)
            setString(2, item.title)
            setString(3, item.description)
            setString(4, item.sourceType.name)
        }
    }

    override suspend fun updateOnboardingItem(id: Int, item: UpdateOnboardingItem): Unit = dbQuery(conn) {
        val current = getOnboardingItem(id) ?: return@dbQuery
        update("""
            update public.sql.onboarding_items
            set source_url = ?,
                title = ?,
                description = ?,
                source_type = ?::public.sql.source_type
            where id = ?
        """.trimIndent()) {
            setString(1, item.sourceUrl ?: current.sourceUrl)
            setString(2, item.title ?: current.title)
            setString(3, item.description ?: current.description)
            setString(4, item.sourceType?.name ?: current.sourceType.name)
            setInt(5, id)
        }
    }

    override suspend fun getOnboardingItem(id: Int): OnboardingItem? = dbQuery(conn) {
        querySingleOrNull("select * from public.sql.onboarding_items where id = ?", { getOnboardingItem() }) {
            setInt(1, id)
        }
    }

    override suspend fun getOnboardingItems(): List<OnboardingItem> = dbQuery(conn) {
        query("select * from public.sql.onboarding_items", { getOnboardingItem() })
    }

    override suspend fun getOnboardingItemsExcept(ids: IntArray): List<OnboardingItem> = dbQuery(conn) {
        query("select * from public.sql.onboarding_items where id not in (${ids.joinToString()})",
            { getOnboardingItem() })
    }

    override suspend fun deleteOnboardingItems(id: Int): Unit = dbQuery(conn) {
        update("delete from public.sql.onboarding_items where id = ?") {
            setInt(1, id)
        }
    }
}