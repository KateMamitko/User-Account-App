package com.example.useraccount.presentation.features.profile

sealed class FactCategory(val title: String) {
    data object Age : FactCategory("Fact about your age")
    data object Date : FactCategory("Fact about today's date")
    data object Initial : FactCategory("Facts about numbers")

    companion object {
        val all = listOf(Initial, Age, Date)
    }
}