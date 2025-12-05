package com.example.useraccount.domain.useCase

import com.example.useraccount.domain.repository.LanguageRepository
import javax.inject.Inject

class ChangeLanguageUseCase @Inject constructor(val languageRepository: LanguageRepository) {

    suspend operator fun invoke(language: String) = languageRepository.setLanguage(language)
}