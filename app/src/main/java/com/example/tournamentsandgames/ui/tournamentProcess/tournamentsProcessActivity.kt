package com.example.tournamentsandgames.ui.tournamentProcess

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matchsandgames.ui.matchProcess.MatchViewModel
import com.example.tournamentsandgames.data.model.Match
import com.example.tournamentsandgames.data.model.Team
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.tournaments.TeamViewModel
import com.example.tournamentsandgames.ui.tournaments.TournamentViewModel
import java.util.UUID

class TournamentsProcessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tournamentId = intent.getStringExtra("tournamentId") ?: ""
        setContent {
            TournamentRoundsScreen(tournamentId)
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TournamentRoundsScreen(tournamentId: String) {
    val tournamentViewModel: TournamentViewModel = viewModel()
    val matchViewModel: MatchViewModel = viewModel()
    val teamViewModel: TeamViewModel = viewModel()

    val tournamentState by tournamentViewModel.getTournamentByIdState.collectAsState()
    val teamsState by teamViewModel.teamsState.collectAsState()

    val roundResults by remember { mutableStateOf(mutableListOf<Match>()) }

    LaunchedEffect(tournamentId) {
        tournamentViewModel.getTournamentsById(tournamentId)
        teamViewModel.getTeamByTournamentId(tournamentId)
    }

    when (val result = tournamentState) {
        is FirebaseResult.Success -> {
            val tournament = result.data[0]

            if (tournament.teams.size < 2) {
                Text("Nie wystarczająca ilość zespołow!")
                return
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tournament.rounds) { roundNr ->
                        RoundRow(
                            roundNr = roundNr + 1,
                            teams = tournament.teams,
                            onMatchUpdated = { match ->
                                roundResults.add(match)
                            },
                            tournamentId = tournamentId
                        )
                    }
                }

                Button(
                    onClick = {
                        roundResults.forEach { match ->
                            matchViewModel.addMatch(match)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Dodaj wyniki")
                }
            }
        }

        is FirebaseResult.Loading -> {
            CircularProgressIndicator()
        }

        is FirebaseResult.Error -> {
            Text("Error: ${result.exception.message}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundRow(roundNr: Int, teams: List<Team>, onMatchUpdated: (Match) -> Unit, tournamentId: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text(
            text = "Round $roundNr",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        for (i in teams.indices step 2) {
            if (i + 1 < teams.size) {
                val teamA = teams[i]
                val teamB = teams[i + 1]

                var pointsA by remember { mutableStateOf("") }
                var pointsB by remember { mutableStateOf("") }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = teamA.name)
                        OutlinedTextField(
                            value = pointsA,
                            onValueChange = { pointsA = it },
                            label = { Text("Wynik") },
                            singleLine = true
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = teamB.name)
                        OutlinedTextField(
                            value = pointsB,
                            onValueChange = { pointsB = it },
                            label = { Text("Wynik") },
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val match = Match(
                    _id = UUID.randomUUID().toString(),
                    tournamentId = tournamentId,
                    roundNr = roundNr,
                    teamA = teamA,
                    teamB = teamB,
                    pointsTeamA = pointsA.toIntOrNull() ?: 0,
                    pointsTeamB = pointsB.toIntOrNull() ?: 0
                )

                onMatchUpdated(match)
            }
        }
    }
}
