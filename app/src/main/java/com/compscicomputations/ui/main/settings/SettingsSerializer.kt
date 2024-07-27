package com.compscicomputations.ui.main.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.compscicomputations.ui.main.settings.SettingsPreferences.Themes
import java.io.InputStream
import java.io.OutputStream

internal object SettingsSerializer : Serializer<SettingsPreferences> {
    override val defaultValue: SettingsPreferences = SettingsPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingsPreferences {
        try {
            return SettingsPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SettingsPreferences, output: OutputStream) = t.writeTo(output)

    val Themes.text : String
        get() = when(this) {
            Themes.DARK -> "Dark theme"
            Themes.LIGHT -> "Light theme"
            Themes.SYSTEM -> "System default"
            else -> "System default"
        }
}