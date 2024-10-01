package com.compscicomputations.services.publik

import com.compscicomputations.plugins.databaseConnection
import com.compscicomputations.services._contrast.PublicServiceContrast
import com.compscicomputations.services.publik.models.requests.NewFeedback
import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.services.publik.models.response.Feedback
import com.compscicomputations.services.publik.models.response.OnboardingItem
import com.compscicomputations.utils.*
import com.compscicomputations.utils.Image.Companion.asImage
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Types

class PublicService : PublicServiceContrast {
    private var conn = databaseConnection()

    companion object {
        private fun ResultSet.getOnboardingItem() = OnboardingItem(
            id = getInt("id"),
            title = getString("title"),
            description = getString("description"),
            image = getBytes("image").asImage,
        )
        private fun ResultSet.getFeedback() = Feedback(
            id = getInt("id"),
            subject = getString("subject"),
            message = getString("message"),
            suggestion = getString("suggestion"),
            image = getBytes("image").asImage,
            userEmail = getString("user_email"),
            createdAt = getTimestamp("created_at").toString(),
            responseMessage = getString("response_message"),
            responseImage = getBytes("response_image").asImage,
            respondedAt = getTimestamp("responded_at")?.toString(),
            respondedByEmail = getString("responded_by_email"),
        )
    }

    override suspend fun createOnboardingItem(item: NewOnboardingItem): Unit = dbQuery(conn) {
        update("""
            insert into public.onboarding_items(title, description, image) 
            values (?, ?, ?)
        """.trimIndent()) {
            setString(1, item.title.trim())
            setString(2, item.description?.trim())
            setBytes(3, item.image?.bytes)
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



    suspend fun createFeedback(feedback: NewFeedback): Int = dbQuery(conn) {
        querySingle("select public.insert_feedback(?, ?, ?, ?, ?) as id", { getInt("id")}) {
            setString(1, feedback.subject.trim())
            setString(2, feedback.message.trim())
            setString(3, feedback.suggestion.trim())
            setBytes(4, feedback.image?.bytes)
            setString(5, feedback.userEmail?.trim())
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

    suspend fun getFeedbackImage(id: Int): ByteArray? = dbQuery(conn) {
        querySingleOrNull("select image from public.feedbacks where id = ?", { getBytes("image") }) {
            setInt(1, id)
        }
    }

    suspend fun getMyFeedbacks(email: String): List<Feedback> = dbQuery(conn) {
        query("select * from public.get_my_feedbacks(?)", { getFeedback() }) {
            setString(1, email.trim())
        }
    }
}