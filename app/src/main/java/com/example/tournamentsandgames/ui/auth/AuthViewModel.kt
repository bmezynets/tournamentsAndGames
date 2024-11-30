package com.example.tournamentsandgames.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tournamentsandgames.data.firebase.FirebaseService
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val firebaseService = FirebaseService()
    private val userRepository = UserRepository.getInstance()

    // State for login
    private val _loginState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val loginState: StateFlow<FirebaseResult<Unit>> = _loginState

    // State for registration
    private val _registrationState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val registrationState: StateFlow<FirebaseResult<Unit>> = _registrationState

    private val _resetPasswordState = MutableStateFlow<FirebaseResult<Unit>?>(FirebaseResult.Loading)
    val resetPasswordState: StateFlow<FirebaseResult<Unit>?> = _resetPasswordState

    // Login function
    fun login(email: String, password: String) {
        _loginState.value = FirebaseResult.Loading
        viewModelScope.launch {
            _loginState.value = firebaseService.signIn(email, password)
        }
    }

    // Registration function
    fun register(email: String, password: String, name: String?, surname: String?) {
        _registrationState.value = FirebaseResult.Loading
        viewModelScope.launch {
            // Sign up the user
            val registrationResult = firebaseService.signUp(email, password)

            if (registrationResult is FirebaseResult.Success && name!!.isNotBlank() && surname!!.isNotBlank()) {
                // Call updateUserName and use callback to handle result
                updateUserName(name, surname)
                _registrationState.value = registrationResult
            } else {
                // If registration failed, update state to show error
                _registrationState.value = registrationResult
            }
        }
    }

    // Other utility functions
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

    // Reset state function for login and registration
    fun resetLoginState() {
        _loginState.value = FirebaseResult.Loading
    }

    fun resetRegistrationState() {
        _registrationState.value = FirebaseResult.Loading
    }

    suspend fun resetPassword(email: String) {
        try {
            _resetPasswordState.value = FirebaseResult.Loading

            FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()

            _resetPasswordState.value = FirebaseResult.Success(Unit)

            delay(1500)
            _resetPasswordState.value = null
        } catch (e: Exception) {
            _resetPasswordState.value = FirebaseResult.Error(e)
        }
    }

    fun getFirebaseErrorMessageReset(exception: Exception): String {
        return when (exception) {
            is FirebaseAuthInvalidUserException -> "Nie znaleziono użytkownika z tym adresem e-mail."
            is FirebaseAuthInvalidCredentialsException -> "Nieprawidłowy adres e-mail."
            else -> exception.message ?: "Wystąpił nieznany błąd."
        }
    }

    fun getFirebaseErrorMessage(exception: Exception): String {
        return when ((exception as? FirebaseAuthException)?.errorCode) {
            "ERROR_INVALID_EMAIL" -> "Nieprawidłowy format adresu e-mail."
            "ERROR_WRONG_PASSWORD" -> "Błędne hasło. Spróbuj ponownie."
            "ERROR_USER_NOT_FOUND" -> "Nie znaleziono użytkownika z tym adresem e-mail."
            "ERROR_USER_DISABLED" -> "Konto użytkownika zostało zablokowane."
            "ERROR_EMAIL_ALREADY_IN_USE" -> "Ten adres e-mail jest już używany."
            "ERROR_WEAK_PASSWORD" -> "Hasło jest zbyt słabe. Hasło muśi zawierać minimum 6 znaków."
            else -> "Wystąpił nieznany błąd. Spróbuj ponownie."
        }
    }
}
