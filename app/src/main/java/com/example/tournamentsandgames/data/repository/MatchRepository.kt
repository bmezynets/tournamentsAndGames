package com.example.tournamentsandgames.data.repository

import com.example.tournamentsandgames.data.model.Match
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MatchRepository  private constructor() {
    companion object {
        @Volatile
        private var instance: MatchRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: MatchRepository().also { instance = it }
            }
    }

    private val database = FirebaseDatabase.getInstance().getReference("matches")

    suspend fun addMatch(match: Match): FirebaseResult<Unit> {
        return try {
            val matchId = database.push().key ?: throw Exception("Błąd generowania ID rundy")
            database.child(matchId).setValue(match.copy(id = matchId)).await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getMatches(): FirebaseResult<List<Match>> {
        return try {
            val snapshot = database.get().await()
            val matchList = snapshot.children.mapNotNull {
                it.getValue(Match::class.java)
            }
            FirebaseResult.Success(matchList)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getMatchesByTournamentId(tournamentId: String): FirebaseResult<List<Match>> {
        return try {
            val snapshot = database.orderByChild("tournamentId").equalTo(tournamentId).get().await()
            val matchesList = snapshot.children.mapNotNull { it.getValue(Match::class.java) }
            FirebaseResult.Success(matchesList)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getMatchesById(id: String): FirebaseResult<List<Match>> {
        return try {
            val snapshot = database.orderByChild("_id").equalTo(id).get().await()
            val matches = snapshot.children.mapNotNull { it.getValue(Match::class.java) }
            FirebaseResult.Success(matches)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }
}