package com.example.tournamentsandgames.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class Tournament(
    var id: String = "",
    var _id: String = "",
    val name: String = "",
    var teams: MutableList<Team> = mutableListOf<Team>(),
    val rounds: Int = 0,
    val matches: List<Match> = listOf(),
    val createdBy: String = "",
    var ended: Boolean = false,
    var started: Boolean = false,
    var currentRound: Int = 0,
    var dateCreated: String = ""
) : Parcelable