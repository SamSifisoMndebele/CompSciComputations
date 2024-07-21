package com.compscicomputations.services.publik.models.requests

import com.compscicomputations.services.publik.models.SourceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewOnboardingItem(
    @SerialName("source_url")
    val sourceUrl: String,
    val title: String,
    val description: String? = null,
    @SerialName("source_type")
    val sourceType: SourceType = SourceType.IMAGE,
)

//    id int primary key generated always as identity not null,
//    source_url text not null,
//    title text not null,
//    description text,
//    source_type public.sql.source_type not null