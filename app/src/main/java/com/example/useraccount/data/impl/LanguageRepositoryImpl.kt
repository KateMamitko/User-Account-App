package com.example.useraccount.data.impl


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.useraccount.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor( private val dataStore: DataStore<Preferences>) :
    LanguageRepository {
    private val LANGUAGE_KEY = stringPreferencesKey("app_language")

    override fun getLanguage(): Flow<String> = dataStore.data.map { prefs ->
        prefs[LANGUAGE_KEY] ?: "en"
    }

    override suspend fun setLanguage(lang: String) {
        dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = lang
        }
    }
}