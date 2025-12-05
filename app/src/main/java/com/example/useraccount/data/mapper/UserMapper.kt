package com.example.useraccount.data.mapper

import com.example.useraccount.data.local.model.UserEntity
import com.example.useraccount.data.network.dto.UserDto
import com.example.useraccount.domain.UserData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun UserDto.toEntity(): UserEntity {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault())
    val formattedDate = LocalDateTime.now().format(formatter)

    return UserEntity(
        photoHeader = photo_header,
        avatar = avatar,
        firstName = first_name,
        lastName = last_name,
        about = about,
        age = age,
        login = login,
        password = password,
        uploadData = formattedDate
    )
}

fun UserEntity.toDomain() = UserData(
    photo_header = photoHeader,
    avatar = avatar,
    name = "$firstName $lastName",
    about = about,
    age = age,
    login = login,
    password = password,
    dateOfUpload = uploadData
)


