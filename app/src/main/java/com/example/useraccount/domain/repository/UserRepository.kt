package com.example.useraccount.domain.repository

import com.example.useraccount.domain.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun loadUserFromFile()
    fun getUserFromDb(): Flow<List<UserData>>
    suspend fun saveCurrentUser(userData: UserData)
    suspend fun getCurrentUser(): UserData
}