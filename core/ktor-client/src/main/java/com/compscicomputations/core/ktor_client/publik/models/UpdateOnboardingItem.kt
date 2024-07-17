package com.compscicomputations.core.ktor_client.publik.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateOnboardingItem(
    @SerialName("source_url")
    val sourceUrl: String? = null,
    val title: String? = null,
    val description: String? = null,
    val type: SourceType? = null,
)

//    id int primary key generated always as identity not null,
//    source_url text not null,
//    title text not null,
//    description text,
//    type public.source_type not null