package com.example.tournamentsandgames.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val tournaments: List<String> = listOf()
)