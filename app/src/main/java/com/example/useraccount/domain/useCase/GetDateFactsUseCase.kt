package com.example.useraccount.domain.useCase

import com.example.useraccount.domain.repository.FactsRepository
import javax.inject.Inject

class GetDateFactsUseCase @Inject constructor(val factsRepository: FactsRepository) {
    suspend operator fun invoke(date: String) = factsRepository.getDateFact(date)
}