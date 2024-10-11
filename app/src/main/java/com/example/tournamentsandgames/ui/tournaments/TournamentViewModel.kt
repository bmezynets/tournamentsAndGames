package com.example.tournamentsandgames.ui.tournaments
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TournamentViewModel : ViewModel() {
    private val tournamentRepository = TournamentRepository()

    // State for the list of tournaments
    private val _tournamentsState = MutableStateFlow<FirebaseResult<List<Tournament>>>(FirebaseResult.Loading)
    val tournamentsState: StateFlow<FirebaseResult<List<Tournament>>> = _tournamentsState

    // State for adding a tournament
    private val _addTournamentState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val addTournamentState: StateFlow<FirebaseResult<Unit>> = _addTournamentState

    fun addTournament(tournament: Tournament) {
        viewModelScope.launch {
            val result = tournamentRepository.addTournament(tournament)
            _addTournamentState.value = result
        }
    }

    fun getTournaments() {
        viewModelScope.launch {
            _tournamentsState.value = tournamentRepository.getTournaments()
        }
    }

    fun getTournamentsByUserId(userId: String) {
        viewModelScope.launch {
            _tournamentsState.value = FirebaseResult.Loading
            val result = tournamentRepository.getTournamentsByUserId(userId)
            _tournamentsState.value = result
        }
    }

    fun getTournamentsById(id: String) {
        viewModelScope.launch {
            _tournamentsState.value = FirebaseResult.Loading
            val result = tournamentRepository.getTournamentsById(id)
            _tournamentsState.value = result
        }
    }
}