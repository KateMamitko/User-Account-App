package com.example.useraccount.domain.repository

import kotlinx.coroutines.flow.Flow

interface LanguageRepository {

    fun getLanguage() : Flow<String>
    suspend fun setLanguage(lang: String)
}