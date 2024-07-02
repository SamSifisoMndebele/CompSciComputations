package com.compscicomputations.core.ktor_client.di

import com.compscicomputations.core.ktor_client.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
//    @Provides
//    @Singleton
//    fun provideSupabaseClient(): SupabaseClient = createSupabaseClient(
//        supabaseUrl = BuildConfig.SUPABASE_URL,
//        supabaseKey = BuildConfig.SUPABASE_KEY
//    ) {
//        install(Postgrest)
//        install(Storage)
//        install(Realtime)
//    }
//
//    @Provides
//    @Singleton
//    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest = client.postgrest
//
//    @Provides
//    @Singleton
//    fun provideSupabaseRealtime(client: SupabaseClient): Realtime = client.realtime
//
//    @Provides
//    @Singleton
//    fun provideSupabaseStorage(client: SupabaseClient): Storage = client.storage

}