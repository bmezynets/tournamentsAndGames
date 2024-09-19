package com.example.tournamentsandgames.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tournamentsandgames.data.firebase.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val firebaseService: FirebaseService) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = AuthState.Loading
        viewModelScope.launch {
            firebaseService.signIn(email, password) { success, message ->
                if (success) {
                    _loginState.value = AuthState.Success
                } else {
                    _loginState.value = AuthState.Error(message ?: "Unknown error")
                }
            }
        }
    }

    fun signUp(email: String, password: String) {
        _loginState.value = AuthState.Loading
        viewModelScope.launch {
            firebaseService.signUp(email, password) { success, message ->
                if (success) {
                    _loginState.value = AuthState.Success
                } else {
                    _loginState.value = AuthState.Error(message ?: "Unknown error")
                }
            }
        }
    }

    fun resetState() {
        _loginState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
