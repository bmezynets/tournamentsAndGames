package com.example.tournamentsandgames.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tournamentsandgames.data.firebase.FirebaseService
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val firebaseService: FirebaseService = FirebaseService()
    private val _loginState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val loginState: StateFlow<FirebaseResult<Unit>> = _loginState
    private val userRepository: UserRepository = UserRepository()

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

    fun getCurrentUser(): FirebaseUser? {
        return userRepository.getCurrentUser()
    }

    fun isUserLoggedIn(): Boolean {
        return userRepository.isUserLoggedIn()
    }

    fun updateUserName(name: String, surname: String) {
        userRepository.updateUserName(name, surname) { success, exception ->
            if (success) {
                Log.d("USER SUCCESS", "User info updated to: $name $surname")
            } else {
                exception?.let {
                    Log.d("USER ERROR", "User info not updated: ${it.message}")
                }
            }
        }
    }

    fun resetState() {
        _loginState.value = FirebaseResult.Loading
    }
}
