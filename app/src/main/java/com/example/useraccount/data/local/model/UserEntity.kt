package com.example.useraccount.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("users")
data class UserEntity(
    @PrimaryKey val login: String,
    val photoHeader: String,
    val avatar: String,
    val firstName: String,
    val lastName: String,
    val about: String,
    val age: Int,
    val password: String,
    val uploadData: String
)