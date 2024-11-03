package com.example.tournamentsandgames.data.repository

import com.example.tournamentsandgames.data.model.Team
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class TeamRepository {
    private val database = FirebaseDatabase.getInstance().getReference("teams")

    suspend fun addTeam(team: Team): FirebaseResult<Unit> {
        return try {
            val teamId = database.push().key ?: throw Exception("Błąd generowania ID gracza")
            database.child(teamId).setValue(team.copy(id = teamId)).await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getTeams(): FirebaseResult<List<Team>> {
        return try {
            val snapshot = database.get().await()
            val teamList = snapshot.children.mapNotNull {
                it.getValue(Team::class.java)
            }
            FirebaseResult.Success(teamList)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getTeamByTournamentId(tournamentId: String): FirebaseResult<List<Team>> {
        return try {
            val snapshot = database.orderByChild("tournamentId").equalTo(tournamentId).get().await()
            val teamsList = snapshot.children.mapNotNull { it.getValue(Team::class.java) }
            FirebaseResult.Success(teamsList)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getTeamsById(id: String): FirebaseResult<List<Team>> {
        return try {
            val snapshot = database.orderByChild("_id").equalTo(id).get().await()
            val teamsList = snapshot.children.mapNotNull { it.getValue(Team::class.java) }
            FirebaseResult.Success(teamsList)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun deleteTeam(teamId: String): FirebaseResult<Unit> {
        return try {
            database.child(teamId).removeValue().await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun updateTeamScore(teamId: String, newScore: Int): FirebaseResult<String> {
        return try {
            database.orderByChild("_id").equalTo(teamId).get().await().children.forEach { teamSnapshot ->
                teamSnapshot.child("points").ref.setValue(newScore).await()
            }
            val tournamentId = database.orderByChild("_id").equalTo(teamId).get().await().children
                .firstOrNull()?.child("tournamentId")?.getValue(String::class.java) ?: ""
            FirebaseResult.Success(tournamentId)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }
}