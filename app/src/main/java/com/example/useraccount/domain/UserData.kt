package com.example.useraccount.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val photo_header: String,
    val avatar: String,
    val name: String,
    val about: String,
    val age: Int,
    val login: String,
    val password: String,
    val dateOfUpload: String
) : Parcelable
