package com.compscicomputations.services.publik

import com.compscicomputations.plugins.connectToPostgres
import com.compscicomputations.services._contrast.PublicServiceContrast
import com.compscicomputations.services.publik.models.requests.NewFeedback
import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.services.publik.models.response.Feedback
import com.compscicomputations.services.publik.models.response.OnboardingItem
import com.compscicomputations.utils.*
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Types

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
                image = getBytes("image"),
            )
        }
        private fun ResultSet.getFeedback(): Feedback {
            return Feedback(
                id = getInt("id"),
                subject = getString("subject"),
                message = getString("message"),
                suggestion = getString("suggestion"),
                image = getBytes("image"),
                userId = getInt("user_id"),
            )
        }
    }

    override suspend fun createOnboardingItem(item: NewOnboardingItem): Unit = dbQuery(conn) {
        update("""
            insert into public.onboarding_items(title, description, image) 
            values (?, ?, ?)
        """.trimIndent()) {
            setString(1, item.title)
            setString(2, item.description)
            setBytes(3, item.image)
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


    suspend fun createFeedback(feedback: NewFeedback): Unit = dbQuery(conn) {
        update("call public.insert_feedback(?, ?, ?, ?, ?::uuid)") {
            setString(1, feedback.subject)
            setString(2, feedback.message)
            setString(3, feedback.suggestion)
            setBytes(4, feedback.image)
            setString(5, feedback.userId)
        }
    }

    suspend fun getFeedbacks(): List<Feedback> = dbQuery(conn) {
        query("select * from public.feedbacks", { getFeedback() })
    }
    suspend fun getFeedback(id: Int): Feedback? = dbQuery(conn) {
        querySingleOrNull("select * from public.feedbacks where id = ?", { getFeedback() }) {
            setInt(1, id)
        }
    }
}