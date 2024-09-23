package com.example.tournamentsandgames.data.repository

sealed class FirebaseResult<out T> {
    data class Success<out T>(val data: T) : FirebaseResult<T>()
    data class Error(val exception: Exception) : FirebaseResult<Nothing>()
    object Loading : FirebaseResult<Nothing>()
}
