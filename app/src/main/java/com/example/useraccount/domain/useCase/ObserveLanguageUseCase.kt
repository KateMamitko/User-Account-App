package com.example.useraccount.domain.useCase

import com.example.useraccount.domain.repository.LanguageRepository
import javax.inject.Inject

class ObserveLanguageUseCase @Inject constructor(val languageRepository: LanguageRepository) {

    operator fun invoke() = languageRepository.getLanguage()
}