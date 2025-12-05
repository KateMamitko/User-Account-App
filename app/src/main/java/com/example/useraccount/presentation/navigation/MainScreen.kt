package com.example.useraccount.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.useraccount.domain.UserData
import com.example.useraccount.presentation.common.viewModel.LanguageViewModel
import com.example.useraccount.presentation.features.auth.AuthorizationContent
import com.example.useraccount.presentation.features.profile.ProfileContent
import com.example.useraccount.presentation.theme.UserAccountTheme
import com.example.useraccount.presentation.common.viewModel.ThemeViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val languageViewModel: LanguageViewModel = hiltViewModel()
    val systemTheme = isSystemInDarkTheme()

    val isDark by themeViewModel.isDark.collectAsState()
    val currentLanguage by languageViewModel.observeLanguage.collectAsState()

    LaunchedEffect(Unit) { themeViewModel.initTheme(systemTheme) }

        UserAccountTheme(darkTheme = isDark) {
            AppNavGraph(
                navController, authorizationContent = {
                    AuthorizationContent(
                        onToggleTheme = { themeViewModel.toggleTheme(it) },
                        openProfileContext = { user ->
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("user", user)
                            navController.navigate(NavigationItem.ProfileScreen.route)
                        },
                        isDark = isDark,
                        currentLanguage = currentLanguage,
                        onLanguageChange = {
                            languageViewModel.changeLanguage(it)
                        }
                    )

                },
                profileContent = {
                    val user = navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<UserData>("user")
                    if (user != null)
                        ProfileContent(userData = user, onBackClick = {
                            navController.navigate(NavigationItem.AuthorizationScreen.route)
                        }, currentLanguage = currentLanguage)
                })

        }
}