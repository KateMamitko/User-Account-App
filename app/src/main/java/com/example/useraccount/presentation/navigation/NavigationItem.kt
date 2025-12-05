package com.example.useraccount.presentation.navigation

sealed class NavigationItem(val route: String) {

    data object AuthorizationScreen : NavigationItem("authorization")
    data object ProfileScreen : NavigationItem("profile")
}