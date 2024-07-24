package com.compscicomputations.services.publik

import com.compscicomputations.plugins.connectToPostgres
import com.compscicomputations.services._contrast.PublicServiceContrast
import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.services.publik.models.response.OnboardingItem
import com.compscicomputations.utils.*
import java.sql.Connection
import java.sql.ResultSet

class PublicService : PublicServiceContrast {
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
                title = getString("title"),
                description = getString("description"),
                imageBytes = getBytes("image_bytes"),
            )
        }
    }

    override suspend fun createOnboardingItem(item: NewOnboardingItem): Unit = dbQuery(conn) {
        update("""
            insert into public.onboarding_items(title, description, image_bytes) 
            values (?, ?, ?)
        """.trimIndent()) {
            setString(1, item.title)
            setString(2, item.description)
            setBytes(3, item.imageBytes)
        }
    }

    override suspend fun getOnboardingItems(except: IntArray?): List<OnboardingItem> = dbQuery(conn) {
        when(except) {
            null -> query("select * from public.onboarding_items", { getOnboardingItem() })
            else -> query("select * from public.onboarding_items where id not in (${except.joinToString()})", { getOnboardingItem() })
        }
    }

    override suspend fun deleteOnboardingItem(id: Int): Unit = dbQuery(conn) {
        update("delete from public.onboarding_items where id = ?") {
            setInt(1, id)
        }
    }
}