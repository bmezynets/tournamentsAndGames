package com.example.tournamentsandgames.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class User(
    val uid: String = "",
    val email: String = "",
    val tournaments: List<String> = listOf()
) : Parcelable