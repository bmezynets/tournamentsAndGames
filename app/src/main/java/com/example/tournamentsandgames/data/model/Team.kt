package com.example.tournamentsandgames.data.model

data class Team(
    var id: String = "",
    val name: String = "",
    val points: Int = 0,
    val players: List<Player> = listOf(),
)