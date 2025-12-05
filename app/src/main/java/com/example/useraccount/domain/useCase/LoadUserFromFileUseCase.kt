package com.example.useraccount.domain.useCase

import com.example.useraccount.domain.repository.UserRepository
import javax.inject.Inject

class LoadUserFromFileUseCase @Inject constructor(val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.loadUserFromFile()
}