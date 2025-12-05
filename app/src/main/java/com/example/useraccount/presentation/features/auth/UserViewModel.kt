package com.example.useraccount.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.useraccount.domain.useCase.GetUserFromDbUseCase
import com.example.useraccount.domain.useCase.LoadUserFromFileUseCase
import com.example.useraccount.domain.useCase.SaveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val getUserFromDbUseCase: GetUserFromDbUseCase,
    val loadUserFromFileUseCase: LoadUserFromFileUseCase,
    val saveCurrentUserUseCase: SaveCurrentUserUseCase
) : ViewModel() {

    val userData = getUserFromDbUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.Eagerly,
            emptyList()
        )

    private val _authState =
        MutableStateFlow<AuthorizationScreenState>(AuthorizationScreenState.Idle)
    val authState = _authState.asStateFlow()


    fun tryAgain() {
        viewModelScope.launch {
            _authState.value = AuthorizationScreenState.Idle
        }
    }

    init {
        viewModelScope.launch {
            val data = getUserFromDbUseCase().first()
            if (data.isEmpty()) {
                loadUserFromFileUseCase()
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthorizationScreenState.Loading
            if (username.isEmpty()) {
                _authState.value =
                    AuthorizationScreenState.Error("Please enter your name and try again")

            } else if (password.isEmpty()) {
                _authState.value =
                    AuthorizationScreenState.Error("Please enter password and try again")
            } else {
                val users = userData.value
                val user = users.find { it.login == username && it.password == password }

                if (user != null) {
                    saveCurrentUserUseCase(user)
                    _authState.value = AuthorizationScreenState.Success(user)
                } else {
                    _authState.value = AuthorizationScreenState.Error("User not found. Try again")
                }
            }
        }
    }
}