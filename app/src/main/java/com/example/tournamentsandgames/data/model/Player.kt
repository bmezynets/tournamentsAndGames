package com.example.tournamentsandgames.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class Player(
    val id: String = "",
    val _id: String = "",
    val name: String = "",
    val surname: String = "",
    val tournamentId: String = "",
    var teamId: String = ""
) : Parcelable