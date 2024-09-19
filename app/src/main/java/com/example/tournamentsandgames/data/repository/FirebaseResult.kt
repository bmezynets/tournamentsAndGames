package com.example.tournamentsandgames.data.repository

sealed class FirebaseResult<out T> {
    object Loading : FirebaseResult<Nothing>()
    data class Success<out T>(val data: T) : FirebaseResult<T>()
    data class Error(val exception: Exception) : FirebaseResult<Nothing>()
}
