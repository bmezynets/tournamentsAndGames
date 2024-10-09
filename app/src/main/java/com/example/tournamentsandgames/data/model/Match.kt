package com.example.tournamentsandgames.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Match(
    val id: String = "",
    val tournamentId: String = "",
    val roundNr: Number = 0,
    val teamA: Team = Team(),
    val teamB: Team = Team(),
    val pointsTeamA: Number = 0,
    val pointsTeamB: Number = 0
) : Parcelable