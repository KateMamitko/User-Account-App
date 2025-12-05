package com.example.useraccount.presentation.features.profile

sealed class LuckyFactState {
    data class Show(val textFact: String) : LuckyFactState()
    data object Close : LuckyFactState()
    data class Error(val textError: String) : LuckyFactState()
    data object Loading : LuckyFactState()
}