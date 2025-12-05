package com.example.useraccount.data.network.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserDto(
    val photo_header: String,
    val avatar: String,
    val first_name: String,
    val last_name: String,
    val about: String,
    val age: Int,
    val login: String,
    val password: String
) : Parcelable