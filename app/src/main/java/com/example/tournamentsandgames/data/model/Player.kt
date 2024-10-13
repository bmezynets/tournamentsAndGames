package com.example.tournamentsandgames.data.model

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Player(
    val id: String = "",
    val _id: String = "",
    val name: String = "",
    val surname: String = "",
    val tournamentId: String = "",
    var teamId: String = ""
) : Parcelable