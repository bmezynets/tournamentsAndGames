package com.example.tournamentsandgames.data.repository

import com.example.tournamentsandgames.data.model.Tournament
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class TournamentRepository {

    private val database = FirebaseDatabase.getInstance().getReference("tournaments")

    // StateFlow to emit tournament results
    private val _tournaments = MutableStateFlow<FirebaseResult<List<Tournament>>>(FirebaseResult.Loading)
    val tournaments: StateFlow<FirebaseResult<List<Tournament>>> get() = _tournaments

    // Method to add a tournament
    suspend fun addTournament(tournament: Tournament) {
        try {
            val tournamentId = database.push().key ?: throw Exception("Error generating tournament ID")
            // Add tournament to the database
            database.child(tournamentId).setValue(tournament.copy(id = tournamentId)).await()
            // Optionally, you can refresh the tournaments list after adding
            getTournaments()
        } catch (e: Exception) {
            _tournaments.value = FirebaseResult.Error(e)
        }
    }

    // Method to fetch tournaments
    suspend fun getTournaments() {
        try {
            // Get all tournaments from the database
            val snapshot = database.get().await()
            val tournamentList = snapshot.children.mapNotNull { it.getValue(Tournament::class.java) }
            _tournaments.value = FirebaseResult.Success(tournamentList)
        } catch (e: Exception) {
            _tournaments.value = FirebaseResult.Error(e)
        }
    }
}
