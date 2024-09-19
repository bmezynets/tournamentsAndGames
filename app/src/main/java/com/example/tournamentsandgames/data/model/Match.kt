package com.example.tournamentsandgames.data.model

data class Match(
    val teamA: Team = Team(),
    val teamB: Team = Team(),
    val scoreA: Int = 0,
    val scoreB: Int = 0
)