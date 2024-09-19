package com.example.tournamentsandgames.data.repository

import com.example.tournamentsandgames.data.model.Tournament
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

class TournamentRepository {
    private val database = FirebaseDatabase.getInstance().getReference("tournaments")

    private val _tournaments = MutableStateFlow<FirebaseResult<List<Tournament>>>(FirebaseResult.Loading)
    val tournaments: Flow<FirebaseResult<List<Tournament>>> = _tournaments

    suspend fun addTournament(tournament: Tournament) {
        try {
            val tournamentId = database.push().key ?: throw Exception("Błąd generowania ID turnieju")
            database.child(tournamentId).setValue(tournament.copy(id = tournamentId)).await()
        } catch (e: Exception) {
            _tournaments.value = FirebaseResult.Error(e)
        }
    }

    suspend fun getTournaments() {
        try {
            val snapshot = database.get().await()
            val tournamentList = snapshot.children.mapNotNull {
                it.getValue(Tournament::class.java)
            }
            _tournaments.value = FirebaseResult.Success(tournamentList)
        } catch (e: Exception) {
            _tournaments.value = FirebaseResult.Error(e)
        }
    }
}
