package com.example.tournamentsandgames.data.repository

import com.example.tournamentsandgames.data.model.Player
import com.example.tournamentsandgames.data.model.Tournament
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class PlayerRepository private constructor() {
    companion object {
        @Volatile
        private var instance: PlayerRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: PlayerRepository().also { instance = it }
            }
    }
    private val database = FirebaseDatabase.getInstance().getReference("players")

    suspend fun addPlayer(player: Player): FirebaseResult<Unit> {
        return try {
            val playerId = database.push().key ?: throw Exception("Błąd generowania ID gracza")
            database.child(playerId).setValue(player.copy(id = playerId)).await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getPlayers(): FirebaseResult<List<Player>> {
        return try {
            val snapshot = database.get().await()
            val playerList = snapshot.children.mapNotNull {
                it.getValue(Player::class.java)
            }
            FirebaseResult.Success(playerList)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getPlayerByTournamentId(tournamentId: String): FirebaseResult<List<Player>> {
        return try {
            val snapshot = database.orderByChild("tournamentId").equalTo(tournamentId).get().await()
            val playersList = snapshot.children.mapNotNull { it.getValue(Player::class.java) }
            FirebaseResult.Success(playersList)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun getPlayersById(id: String): FirebaseResult<List<Player>> {
        return try {
            val snapshot = database.orderByChild("_id").equalTo(id).get().await()
            val playersList = snapshot.children.mapNotNull { it.getValue(Player::class.java) }
            FirebaseResult.Success(playersList)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun deletePlayer(playerId: String): FirebaseResult<Unit> {
        return try {
            database.child(playerId).removeValue().await()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }
}