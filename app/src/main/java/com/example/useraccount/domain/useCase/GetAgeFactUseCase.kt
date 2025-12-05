package com.example.useraccount.domain.useCase

import com.example.useraccount.domain.repository.FactsRepository
import javax.inject.Inject

class GetAgeFactUseCase @Inject constructor(val factsRepository: FactsRepository) {

    suspend operator fun invoke(number: Int) = factsRepository.getAgeFact(number)
}