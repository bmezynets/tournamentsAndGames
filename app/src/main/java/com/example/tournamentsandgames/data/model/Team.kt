package com.example.tournamentsandgames.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team(
    var id: String = "",
    val name: String = "",
    val points: Int = 0,
    val players: List<Player> = listOf(),
) : Parcelable