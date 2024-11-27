package com.example.tournamentsandgames.ui.tournaments
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.data.repository.TournamentRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TournamentViewModel : ViewModel() {
    private val tournamentRepository = TournamentRepository.getInstance()

    // State for the list of tournaments
    private val _tournamentsState = MutableStateFlow<FirebaseResult<List<Tournament>>>(FirebaseResult.Loading)
    val tournamentsState: StateFlow<FirebaseResult<List<Tournament>>> = _tournamentsState

    // State for adding a tournament
    private val _addTournamentState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val addTournamentState: StateFlow<FirebaseResult<Unit>> = _addTournamentState

    private val _getTournamentByIdState = MutableStateFlow<FirebaseResult<List<Tournament>>>(FirebaseResult.Loading)
    val getTournamentByIdState: StateFlow<FirebaseResult<List<Tournament>>> = _getTournamentByIdState

    private val _state: MutableStateFlow<Tournament?> = MutableStateFlow(null)
    val state: StateFlow<Tournament?> = _state.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _startTournamentState = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val startTournamentState: StateFlow<FirebaseResult<Boolean>> = _startTournamentState

    private val database = FirebaseDatabase.getInstance().getReference("tournaments")

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
            _getTournamentByIdState.value = FirebaseResult.Loading
            val result = tournamentRepository.getTournamentsById(id)
            _getTournamentByIdState.value = result
            Log.d("RESULT:", result.toString())
        }
    }

    fun startTournament(tournamentId: String) {
        viewModelScope.launch {
            _startTournamentState.value = FirebaseResult.Loading
            val result = tournamentRepository.startTournament(tournamentId)
            _startTournamentState.value = result
        }
    }

    fun endTournament(tournamentId: String) {
        viewModelScope.launch {
            _startTournamentState.value = FirebaseResult.Loading
            val result = tournamentRepository.endTournament(tournamentId)
            _startTournamentState.value = result
        }
    }

    fun setCurrentRound(tournamentId: String, round: Int) {
        viewModelScope.launch {
            _startTournamentState.value = FirebaseResult.Loading
            val result = tournamentRepository.setCurrentRound(tournamentId, round)
            _startTournamentState.value = result
        }
    }
}