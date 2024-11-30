package com.example.tournamentsandgames.data.repository

import com.example.tournamentsandgames.data.model.Tournament
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class TournamentRepository private constructor() {
    companion object {
        @Volatile
        private var instance: TournamentRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: TournamentRepository().also { instance = it }
            }
    }

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
            val tournaments = snapshot.children.mapNotNull { it.getValue(Tournament::class.java) }
            FirebaseResult.Success(tournaments)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun startTournament(tournamentId: String): FirebaseResult<Boolean> {
        return try {
            val querySnapshot = database.orderByChild("_id").equalTo(tournamentId).get().await()

            if (querySnapshot.exists() && querySnapshot.childrenCount > 0) {
                val tournamentSnapshot = querySnapshot.children.firstOrNull()

                if (tournamentSnapshot != null) {
                    tournamentSnapshot.ref.child("started").setValue(true).await()
                    FirebaseResult.Success(true)
                } else {
                    throw Exception("Tournament not found")
                }
            } else {
                throw Exception("Tournament not found")
            }
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }


    suspend fun endTournament(tournamentId: String): FirebaseResult<Boolean> {
        return try {
            val querySnapshot = database.orderByChild("_id").equalTo(tournamentId).get().await()

            if (querySnapshot.exists() && querySnapshot.childrenCount > 0) {
                val tournamentSnapshot = querySnapshot.children.firstOrNull()

                if (tournamentSnapshot != null) {
                    tournamentSnapshot.ref.child("ended").setValue(true).await()
                    FirebaseResult.Success(true)
                } else {
                    throw Exception("Tournament not found")
                }
            } else {
                throw Exception("Tournament not found")
            }
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun setCurrentRound(tournamentId: String, round: Int): FirebaseResult<Boolean> {
        return try {
            val querySnapshot = database.orderByChild("_id").equalTo(tournamentId).get().await()

            if (querySnapshot.exists() && querySnapshot.childrenCount > 0) {
                val tournamentSnapshot = querySnapshot.children.firstOrNull()

                if (tournamentSnapshot != null) {
                    tournamentSnapshot.ref.child("currentRound").setValue(round).await()
                    FirebaseResult.Success(true)
                } else {
                    throw Exception("Tournament not found")
                }
            } else {
                throw Exception("Tournament not found")
            }
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }
}
