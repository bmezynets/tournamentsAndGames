package com.example.matchsandgames.ui.matchProcess

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tournamentsandgames.data.model.Match
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.data.repository.MatchRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MatchViewModel : ViewModel() {
    private val matchRepository = MatchRepository.getInstance()

    private val _matchesState = MutableStateFlow<FirebaseResult<List<Match>>>(
        FirebaseResult.Loading)
    val matchesState: StateFlow<FirebaseResult<List<Match>>> = _matchesState

    private val _addMatchState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val addMatchState: StateFlow<FirebaseResult<Unit>> = _addMatchState

    private val _getMatchByIdState = MutableStateFlow<FirebaseResult<List<Match>>>(
        FirebaseResult.Loading)
    val getMatchByIdState: StateFlow<FirebaseResult<List<Match>>> = _getMatchByIdState

    private val _state: MutableStateFlow<Match?> = MutableStateFlow(null)
    val state: StateFlow<Match?> = _state.asStateFlow()

    private val database = FirebaseDatabase.getInstance().getReference("matchs")

    fun addMatch(match: Match) {
        viewModelScope.launch {
            val result = matchRepository.addMatch(match)
            _addMatchState.value = result
        }
    }

    fun getMatches() {
        viewModelScope.launch {
            _matchesState.value = matchRepository.getMatches()
        }
    }

    fun getMatchesByUserId(tournamentId: String) {
        viewModelScope.launch {
            _matchesState.value = FirebaseResult.Loading
            val result = matchRepository.getMatchesByTournamentId(tournamentId)
            _matchesState.value = result
        }
    }

    fun getMatchesById(id: String) {
        viewModelScope.launch {
            _getMatchByIdState.value = FirebaseResult.Loading
            val result = matchRepository.getMatchesById(id)
            _getMatchByIdState.value = result
        }
    }
}