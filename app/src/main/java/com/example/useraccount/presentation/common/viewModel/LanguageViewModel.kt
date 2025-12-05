package com.example.useraccount.presentation.common.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useraccount.domain.useCase.ChangeLanguageUseCase
import com.example.useraccount.domain.useCase.ObserveLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val changeLanguageUseCase: ChangeLanguageUseCase,
    observeLanguageUseCase: ObserveLanguageUseCase,
) :
    ViewModel() {

    val observeLanguage = observeLanguageUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = "uk"
    )

    fun changeLanguage(lang: String) {
        viewModelScope.launch {
            changeLanguageUseCase(lang)
        }
    }
}