package com.example.tournamentsandgames.ui.tournaments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tournamentsandgames.data.model.Player
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.data.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel: ViewModel() {
    private val playerRepository = PlayerRepository()

    private val _playersState = MutableStateFlow<FirebaseResult<List<Player>>>(FirebaseResult.Loading)
    val playersState: StateFlow<FirebaseResult<List<Player>>> = _playersState

    private val _addPlayerState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val addPlayerState: StateFlow<FirebaseResult<Unit>> = _addPlayerState

    private val _deletePlayerState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val deletePlayerState: StateFlow<FirebaseResult<Unit>> = _deletePlayerState

    fun addPlayer(player: Player) {
        viewModelScope.launch {
            val result = playerRepository.addPlayer(player)
            _addPlayerState.value = result
        }
    }

    fun getPlayers() {
        viewModelScope.launch {
            _playersState.value = playerRepository.getPlayers()
        }
    }

    fun getPlayerByTournamentId(tournamentId: String) {
        viewModelScope.launch {
            _playersState.value = FirebaseResult.Loading
            val result = playerRepository.getPlayerByTournamentId(tournamentId)
            _playersState.value = result
        }
    }

    fun getPlayersById(id: String) {
        viewModelScope.launch {
            _playersState.value = FirebaseResult.Loading
            val result = playerRepository.getPlayersById(id)
            _playersState.value = result
        }
    }

    fun deletePlayer(playerId: String) {
        viewModelScope.launch {
            _deletePlayerState.value = FirebaseResult.Loading
            val result = playerRepository.deletePlayer(playerId)
            _deletePlayerState.value = result
        }
    }
}