package com.example.useraccount.domain.useCase

import com.example.useraccount.domain.repository.UserRepository
import javax.inject.Inject

class GetUserFromDbUseCase @Inject constructor(val userRepository: UserRepository) {
    operator fun invoke() = userRepository.getUserFromDb()
}