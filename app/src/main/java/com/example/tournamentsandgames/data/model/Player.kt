package com.example.tournamentsandgames.data.model

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Player(
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val tournaments: List<String> = listOf(),
    val teamId: String = ""
) : Parcelable