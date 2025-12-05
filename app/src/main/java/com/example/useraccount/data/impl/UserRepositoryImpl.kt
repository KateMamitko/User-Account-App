package com.example.useraccount.data.impl

import android.content.Context
import com.example.useraccount.data.local.dao.CurrentUserDao
import com.example.useraccount.data.local.dao.UserDao
import com.example.useraccount.data.local.model.CurrentUserEntity
import com.example.useraccount.data.mapper.toDomain
import com.example.useraccount.data.mapper.toEntity
import com.example.useraccount.data.network.dto.UserDto
import com.example.useraccount.domain.UserData
import com.example.useraccount.domain.repository.UserRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val userDao: UserDao,
    private val currentUserDao: CurrentUserDao,
) : UserRepository {

    override suspend fun loadUserFromFile() {
        val json = context.assets.open("user_example.json")
            .bufferedReader()
            .use { it.readText() }

        val dto = Gson().fromJson(json, UserDto::class.java)
        val entity = dto.toEntity()
        userDao.saveUser(entity)
    }

    override fun getUserFromDb() = userDao.getUsers().map { list ->
        list.map { userEntry ->
            userEntry.toDomain()
        }
    }

    override suspend fun saveCurrentUser(userData: UserData) {
        currentUserDao.saveCurrentUser(
            CurrentUserEntity(
                login = userData.login
            )
        )
    }

    override suspend fun getCurrentUser(): UserData {
        val saved = currentUserDao.getCurrentUser()
            ?: throw IllegalStateException("User not logged in")

        val allUsers = userDao.getUsers().first()

        return allUsers.firstOrNull { it.login == saved.login }
            ?.toDomain()
            ?: throw IllegalStateException("User not found in DB")
    }
}