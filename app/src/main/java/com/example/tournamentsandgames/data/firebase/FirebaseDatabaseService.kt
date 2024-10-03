package com.example.tournamentsandgames.data.firebase

import com.example.tournamentsandgames.data.model.Tournament
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseDatabaseService {
    private val database = FirebaseDatabase.getInstance()

    suspend fun addTournament(tournament: Tournament): Boolean {
        return try {
            val tournamentId = database.getReference("tournaments").push().key
                ?: throw Exception("Failed to generate tournament ID")
            tournament.id = tournamentId
            database.getReference("tournaments").child(tournamentId).setValue(tournament).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getTournaments(): List<Tournament> {
        return try {
            val snapshot = database.getReference("tournaments").get().await()
            snapshot.children.mapNotNull { it.getValue(Tournament::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}