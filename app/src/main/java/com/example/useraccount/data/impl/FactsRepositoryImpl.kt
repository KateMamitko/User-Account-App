package com.example.useraccount.data.impl

import com.example.useraccount.data.network.ApiService
import com.example.useraccount.domain.repository.FactsRepository
import javax.inject.Inject

class FactsRepositoryImpl @Inject constructor(val apiService: ApiService) : FactsRepository {

    override suspend fun getAgeFact(number: Int): String {
        return apiService.getAgeFact(number)
    }

    override suspend fun getDateFact(date: String): String {
        return apiService.getDateFact(date)
    }

    override suspend fun getLuckyNumberFact(): String {
        return apiService.luckyNumber()
    }
}