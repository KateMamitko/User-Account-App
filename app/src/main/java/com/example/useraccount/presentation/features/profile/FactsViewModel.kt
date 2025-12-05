package com.example.useraccount.presentation.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useraccount.domain.useCase.GetAgeFactUseCase
import com.example.useraccount.domain.useCase.GetCurrentUserUseCase
import com.example.useraccount.domain.useCase.GetDateFactsUseCase
import com.example.useraccount.domain.useCase.GetLuckyNumberFactUseCase
import com.example.useraccount.domain.useCase.GetTodayDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactsViewModel @Inject constructor(
    val getAgeFactUseCase: GetAgeFactUseCase,
    val getDateFactsUseCase: GetDateFactsUseCase,
    val getLuckyNumberFactUseCase: GetLuckyNumberFactUseCase,
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val getTodayDateUseCase: GetTodayDateUseCase
) : ViewModel() {

    private val _fact = MutableStateFlow<FactUiState>(FactUiState.Initial())
    val fact = _fact.asStateFlow()

    private val _luckyFact = MutableStateFlow<LuckyFactState>(LuckyFactState.Close)
    val luckyFact = _luckyFact.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()


    private var loadJob: Job? = null
    private var loadJob2: Job? = null

    fun refreshFact(category: FactCategory) {
        loadJob2?.cancel()

        loadJob2 = viewModelScope.launch {
            _isRefreshing.value = true
            _fact.value = FactUiState.Loading()
            _isRefreshing.value = false

            val result = safeCall(
                onError = { e ->
                    _fact.value = FactUiState.Error("Error: ${e.message}")
                }
            ) {
                requestFact(category)
            }

            result?.let {
                if (isActive) {
                    _fact.value = FactUiState.Success(it)
                }
            }


        }
    }

    fun closeFactsDialog() {
        _luckyFact.value = LuckyFactState.Close
    }

    private suspend fun requestFact(category: FactCategory): String {
        return when (category) {
            FactCategory.Age -> {
                val user = getCurrentUserUseCase()
                getAgeFactUseCase(user.age)
            }

            FactCategory.Date -> {
                val date = getTodayDateUseCase()
                getDateFactsUseCase(date)
            }

            FactCategory.Initial -> "Swipe to select a fact"
        }
    }

    fun showLuckyFacts() {
        viewModelScope.launch {
            _luckyFact.value = LuckyFactState.Loading
            val result = safeCall(
                onError = { e ->
                    _luckyFact.value = LuckyFactState.Error("Error: ${e.message}")
                }
            ) {
                getLuckyNumberFactUseCase()
            }
            result?.let {
                _luckyFact.value = LuckyFactState.Show(it)
            }

        }
    }

    fun loadFact(category: FactCategory) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (category == FactCategory.Initial) {
                _fact.value = FactUiState.Success("Swipe to select a fact")
                return@launch
            }
            _fact.value = FactUiState.Loading()
            val result = safeCall(
                onError = { e ->
                    _fact.value = FactUiState.Error("Error: ${e.message}")
                }
            ) {
                requestFact(category)
            }
            result?.let {
                _fact.value = FactUiState.Success(it)
            }
        }
    }


    suspend fun safeCall(
        onError: (Throwable) -> Unit = {},
        block: suspend () -> String
    ): String? {
        return try {
            block()
        } catch (e: Exception) {
            onError(e)
            null
        }
    }
}