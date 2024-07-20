package com.compscicomputations.client.publik.data.source.remote

import com.compscicomputations.client.publik.data.source.local.OnboardingItem
import com.compscicomputations.client.publik.models.SourceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewOnboardingItem(
    @SerialName("source_url")
    val sourceUrl: String,
    val title: String,
    val description: String?,
    @SerialName("source_type")
    val sourceType: SourceType,
) {
//    val asOnboardingItem: OnboardingItem
//        get() = OnboardingItem(
//            id = 0,
//            sourceUrl = sourceUrl,
//            title = title,
//            description = description,
//            sourceType = sourceType
//        )
}

//    id int primary key generated always as identity not null,
//    source_url text not null,
//    title text not null,
//    description text,
//    source_type public.source_type not null