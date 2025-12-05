package com.example.useraccount.presentation.features.auth

import com.example.useraccount.domain.UserData


sealed class AuthorizationScreenState {

    data object Idle : AuthorizationScreenState()
    data object Loading : AuthorizationScreenState()
    data class Error(val messageError: String) : AuthorizationScreenState()
    data class Success(val userData: UserData) : AuthorizationScreenState()
}