package com.example.tournamentsandgames.data.repository

import com.example.tournamentsandgames.data.firebase.FirebaseDatabaseService
import com.example.tournamentsandgames.data.model.Tournament
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

class TournamentRepository {
    private val database = FirebaseDatabase.getInstance().getReference("tournaments")

    suspend fun addTournament(tournament: Tournament): FirebaseResult<Unit> {
        return try {
            val tournamentId = database.push().key ?: throw Exception("Błąd generowania ID turnieju")
            database.child(tournamentId).setValue(tournament.copy(id = tournamentId)).await()
            FirebaseResult.Success(Unit)  // Emit success here
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getTournaments(): FirebaseResult<List<Tournament>> {
        return try {
            val snapshot = database.get().await()
            val tournamentList = snapshot.children.mapNotNull {
                it.getValue(Tournament::class.java)
            }
            FirebaseResult.Success(tournamentList)  // Emit success
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getTournamentsByUserId(userId: String): FirebaseResult<List<Tournament>> {
        return try {
            val snapshot = database.orderByChild("createdBy").equalTo(userId).get().await()
            val tournamentList = snapshot.children.mapNotNull { it.getValue(Tournament::class.java) }
            FirebaseResult.Success(tournamentList)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getTournamentsById(id: String): FirebaseResult<List<Tournament>> {
        return try {
            val snapshot = database.orderByChild("_id").equalTo(id).get().await()
            val tournamentList = snapshot.children.mapNotNull { it.getValue(Tournament::class.java) }
            FirebaseResult.Success(tournamentList)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }
}
