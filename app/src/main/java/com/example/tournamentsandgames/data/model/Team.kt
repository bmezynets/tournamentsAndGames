package com.example.tournamentsandgames.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class Team(
    var id: String = "",
    var _id: String = "",
    val name: String = "",
    val points: Int = 0,
    val players: List<Player> = listOf(),
    val tournamentId: String = ""
) : Parcelable