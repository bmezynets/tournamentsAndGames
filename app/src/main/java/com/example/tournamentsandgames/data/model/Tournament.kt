package com.example.tournamentsandgames.data.model

data class Tournament(
    val id: String = "",
    val name: String = "",
    val teams: List<Team> = listOf(),
    val rounds: Int = 0,
    val matches: List<Match> = listOf()
)