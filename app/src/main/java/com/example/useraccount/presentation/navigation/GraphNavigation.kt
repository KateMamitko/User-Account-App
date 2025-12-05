package com.example.useraccount.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authorizationContent: @Composable () -> Unit,
    profileContent: @Composable () -> Unit
) {

    NavHost(navController, startDestination = NavigationItem.AuthorizationScreen.route) {
        composable(NavigationItem.AuthorizationScreen.route) { authorizationContent() }
        composable(NavigationItem.ProfileScreen.route) { profileContent() }
    }
}