package com.example.tournamentsandgames.data.model

data class Player(
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val tournaments: List<String> = listOf(),
    val teamId: String = ""
)