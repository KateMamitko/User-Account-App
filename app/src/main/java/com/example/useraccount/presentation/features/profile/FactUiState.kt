package com.example.useraccount.presentation.features.profile

sealed class FactUiState {
    data class Loading(val text: String = "Loading...") : FactUiState()
    data class Success(val text: String) : FactUiState()
    data class Error(val message: String) : FactUiState()
    data class Initial(val mes: String = "Swipe to select a fact") : FactUiState()
}