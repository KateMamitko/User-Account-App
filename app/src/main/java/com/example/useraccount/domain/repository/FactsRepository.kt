package com.example.useraccount.domain.repository

interface FactsRepository {

    suspend fun getAgeFact(number: Int): String
    suspend fun getDateFact(date: String): String
    suspend fun getLuckyNumberFact(): String
}
