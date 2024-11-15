package com.example.tournamentsandgames.ui.tournaments
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tournamentsandgames.data.model.Match
import com.example.tournamentsandgames.data.model.Team
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.data.repository.TournamentRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    /*suspend fun getTournamentsById(id: String): Tournament? {
        val tournamentQuery = database.orderByChild("_id").equalTo(id)
        _isLoading.value = true

        return suspendCancellableCoroutine { continuation ->
            tournamentQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var foundTournament: Tournament? = null
                    dataSnapshot.children.forEach { tournamentSnapshot ->
                        val _id = tournamentSnapshot.child("_id").getValue(String::class.java)
                        if (_id == id) {
                            val tmtId = tournamentSnapshot.key
                            val tmtName = tournamentSnapshot.child("name").getValue(String::class.java) ?: "Unnamed Tournament"

                            // Deserialize teams
                            val tmtTeams = tournamentSnapshot.child("teams").children.mapNotNull { teamSnapshot ->
                                teamSnapshot.getValue(Team::class.java)
                            }

                            val rounds = tournamentSnapshot.child("rounds").getValue(Int::class.java) ?: 0

                            // Deserialize matches
                            val matches = tournamentSnapshot.child("matches").children.mapNotNull { matchSnapshot ->
                                matchSnapshot.getValue(Match::class.java)
                            }

                            val createdBy = tournamentSnapshot.child("createdBy").getValue(String::class.java) ?: "Unknown Creator"

                            foundTournament = Tournament(
                                id = tmtId ?: "",
                                _id = _id ?: "",
                                name = tmtName,
                                teams = tmtTeams.toMutableList(),
                                rounds = rounds,
                                matches = matches,
                                createdBy = createdBy
                            )
                        }
                    }
                    Log.d("TMNT VM", foundTournament.toString())
                    _isLoading.value = false
                    continuation.resume(foundTournament)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    _isLoading.value = true
                    continuation.resumeWithException(databaseError.toException())
                }
            })
        }
    }*/
}