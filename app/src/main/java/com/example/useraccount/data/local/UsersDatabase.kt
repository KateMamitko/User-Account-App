package com.example.useraccount.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.useraccount.data.local.dao.CurrentUserDao
import com.example.useraccount.data.local.dao.UserDao
import com.example.useraccount.data.local.model.CurrentUserEntity
import com.example.useraccount.data.local.model.UserEntity

@Database(entities = [UserEntity::class, CurrentUserEntity::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun currentUserDao(): CurrentUserDao
}