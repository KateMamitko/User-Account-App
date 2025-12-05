package com.example.useraccount.data.network

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    @GET("{number}")
    suspend fun getAgeFact(@Path("number") number: Int): String

    @GET("{date}")
    suspend fun getDateFact(@Path("date") date: String): String

    @GET("random/math")
    suspend fun luckyNumber(): String

}