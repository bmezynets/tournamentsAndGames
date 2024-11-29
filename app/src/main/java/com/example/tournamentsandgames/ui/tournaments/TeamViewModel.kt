package com.example.tournamentsandgames.ui.tournaments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tournamentsandgames.data.model.Team
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.data.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeamViewModel: ViewModel() {
    private val teamRepository = TeamRepository.getInstance()

    private val _teamsState = MutableStateFlow<FirebaseResult<List<Team>>>(FirebaseResult.Loading)
    val teamsState: StateFlow<FirebaseResult<List<Team>>> = _teamsState

    private val _addTeamState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val addTeamState: StateFlow<FirebaseResult<Unit>> = _addTeamState

    private val _deleteTeamState = MutableStateFlow<FirebaseResult<Unit>>(FirebaseResult.Loading)
    val deleteTeamState: StateFlow<FirebaseResult<Unit>> = _deleteTeamState

    private val _getSortedListState = MutableStateFlow<FirebaseResult<List<Team>>>(FirebaseResult.Loading)
    val getSortedListState: StateFlow<FirebaseResult<List<Team>>> = _getSortedListState

    fun addTeam(team: Team) {
        viewModelScope.launch {
            val result = teamRepository.addTeam(team)
            _addTeamState.value = result
        }
    }

    fun getTeams() {
        viewModelScope.launch {
            _teamsState.value = teamRepository.getTeams()
        }
    }

    fun getTeamByTournamentId(tournamentId: String) {
        viewModelScope.launch {
            _teamsState.value = FirebaseResult.Loading
            val result = teamRepository.getTeamByTournamentId(tournamentId)
            _teamsState.value = result
        }
    }

    fun getTeamsById(id: String) {
        viewModelScope.launch {
            _teamsState.value = FirebaseResult.Loading
            val result = teamRepository.getTeamsById(id)
            _teamsState.value = result
        }
    }

    fun deleteTeam(teamId: String) {
        viewModelScope.launch {
            _deleteTeamState.value = FirebaseResult.Loading
            val result = teamRepository.deleteTeam(teamId)
            _deleteTeamState.value = result
        }
    }

    fun updateTeamScore(teamId: String, newScore: Int) {
        viewModelScope.launch {
            val result = teamRepository.updateTeamScore(teamId, newScore)

            if (result is FirebaseResult.Success) {
                val tournamentId = result.data
                getTeamByTournamentId(tournamentId)
            }
        }
    }

    fun getSortedTeamsByPoints(tournamentId: String) {
        viewModelScope.launch {
            _getSortedListState.value = FirebaseResult.Loading
            val result = teamRepository.getSortedTeamsByPoints(tournamentId)
            _getSortedListState.value = result
        }
    }
}