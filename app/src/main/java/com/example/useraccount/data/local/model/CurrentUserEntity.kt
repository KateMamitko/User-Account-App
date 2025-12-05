package com.example.useraccount.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "current_user")
data class CurrentUserEntity(
    @PrimaryKey val id: Int = 0,
    val login: String
)