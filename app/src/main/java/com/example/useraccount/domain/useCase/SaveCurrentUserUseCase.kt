package com.example.useraccount.domain.useCase

import com.example.useraccount.domain.UserData
import com.example.useraccount.domain.repository.UserRepository
import javax.inject.Inject

class SaveCurrentUserUseCase @Inject constructor(val userRepository: UserRepository) {
    suspend operator fun invoke(userData: UserData) = userRepository.saveCurrentUser(userData)
}