package com.compscicomputations.services.publik.models.response

import com.compscicomputations.services.publik.models.SourceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingItem(
    val id: Int,
    @SerialName("source_url")
    val sourceUrl: String,
    val title: String,
    val description: String?,
    @SerialName("source_type")
    val sourceType: SourceType,
)

//    id int primary key generated always as identity not null,
//    source_url text not null,
//    title text not null,
//    description text,
//    source_type public.sql.source_type not null