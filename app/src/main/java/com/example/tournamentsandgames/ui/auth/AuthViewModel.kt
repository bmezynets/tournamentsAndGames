package com.example.tournamentsandgames.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tournamentsandgames.data.firebase.FirebaseService
import com.example.tournamentsandgames.data.repository.FirebaseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val firebaseService: FirebaseService = FirebaseService()
    private val _loginState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val loginState: StateFlow<FirebaseResult<Unit>> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = FirebaseResult.Loading
        viewModelScope.launch {
            _loginState.value = firebaseService.signIn(email, password)
        }
    }

    fun signUp(email: String, password: String) {
        _loginState.value = FirebaseResult.Loading
        viewModelScope.launch {
            _loginState.value = firebaseService.signUp(email, password)
        }
    }

    fun resetState() {
        _loginState.value = FirebaseResult.Loading // Reset or change according to your logic
    }
}
