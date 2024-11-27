package com.example.tournamentsandgames.ui.tournamentProcess

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matchsandgames.ui.matchProcess.MatchViewModel
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.model.Match
import com.example.tournamentsandgames.data.model.Team
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.tournaments.TeamCardWithScoreInput
import com.example.tournamentsandgames.ui.tournaments.TeamViewModel
import com.example.tournamentsandgames.ui.tournaments.TournamentViewModel
import com.google.firebase.ktx.Firebase
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
    val teamViewModel: TeamViewModel = viewModel()
    val matchViewModel: MatchViewModel = viewModel()

    val tournamentState by tournamentViewModel.getTournamentByIdState.collectAsState()
    val teamsState by teamViewModel.teamsState.collectAsState()

    var currentRound by remember { mutableIntStateOf(0) }
    var inputPoints by remember { mutableStateOf(mutableMapOf<String, Int>()) }
    var teamsList  by remember { mutableStateOf(listOf<Team>()) }
    var tournament by remember { mutableStateOf(Tournament()) }

    LaunchedEffect(tournamentId) {
        tournamentViewModel.getTournamentsById(tournamentId)
        teamViewModel.getTeamByTournamentId(tournamentId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when(val result = tournamentState) {
                    is FirebaseResult.Success -> {
                        tournament = result.data[0]

                        if (tournament.currentRound == 0) {
                            tournamentViewModel.setCurrentRound(tournamentId, 1)
                            tournament.currentRound = 1
                        }
                    }
                    is FirebaseResult.Loading -> {
                        CircularProgressIndicator()
                    }

                    is FirebaseResult.Error -> {
                        Text("Error: ${result.exception.message}")
                    }
                }

                when(val result = teamsState) {
                    is FirebaseResult.Success -> {
                        teamsList = result.data
                        Log.d("TEAM SIZE", teamsList.size.toString())
                        if (teamsList.size < 2) {
                            Text("Nie wystarczająca ilość zespołów!")
                            return
                        }
                    }
                    is FirebaseResult.Loading -> {
                        CircularProgressIndicator()
                    }

                    is FirebaseResult.Error -> {
                        Text("Error: ${result.exception.message}")
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.trophy),
                            contentDescription = "Tournament Picture",
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = tournament.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                if (!tournament.ended) {
                    Text(
                        text = "Runda ${tournament.currentRound}",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (teamsList.isNotEmpty()) {
                        LazyColumn {
                            items(teamsList) { team ->
                                TeamPointsRow(
                                    team = team,
                                    pointsMap = inputPoints,
                                    onPointsUpdated = { newScore ->
                                        //teamViewModel.updateTeamScore(team.id, newScore)
                                        inputPoints[team._id] = newScore
                                    }
                                )
                            }
                        }
                    }
                    Log.d("CURRENT ROUND", "${tournament.currentRound} ${tournament.currentRound < tournament.rounds - 1 || tournament.currentRound == 1}")
                    if (tournament.currentRound < tournament.rounds - 1 || tournament.currentRound == 1) {
                        Button(
                            onClick = {
                                teamsList.forEach { team ->
                                    val points = inputPoints[team._id] ?: 0
                                    teamViewModel.updateTeamScore(team._id, points)
                                }

                                inputPoints.clear()
                                tournament.currentRound += 1
                                tournamentViewModel.setCurrentRound(tournamentId, tournament.currentRound)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Next Round")
                        }
                    } else {
                        Button(
                            onClick = {
                                tournamentViewModel.endTournament(tournamentId)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("End Tournament")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    /*when (val result = tournamentState) {
        is FirebaseResult.Success -> {
            val tournament = result.data[0]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = tournament.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!tournament.started) {
                    Button(
                        onClick = { tournamentViewModel.startTournament(tournamentId) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Start Tournament")
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tournament.teams.size) { team ->
                            TeamRow(team = tournament.teams[team])
                        }
                    }
                } else {
                    Text(
                        text = "Round ${currentRound + 1}",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(teamsList.size) { idx ->
                            TeamPointsRow(team = teamsList[idx], pointsMap = inputPoints) { updatedPoints ->
                                inputPoints += mapOf(tournament._id to updatedPoints)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (currentRound < tournament.rounds - 1) {
                        Button(
                            onClick = {
                                teamsList.forEach { team ->
                                    teamViewModel.updateTeamScore(team._id, inputPoints.get(0))
                                }

                                inputPoints.clear()
                                currentRound += 1
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Next Round")
                        }
                    } else {
                        Button(
                            onClick = {
                                teamViewModel.endTournament(tournament)
                                // Navigate to summary activity
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("End Tournament")
                        }
                    }
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

@Composable
fun TeamRow(team: Team) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = team.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Players: ${team.players.size}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Total Points: ${team.points}", style = MaterialTheme.typography.bodyMedium)
        }
    }
*/
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamPointsRow(
    team: Team,
    pointsMap: Map<String, Int>,
    onPointsUpdated: (Int) -> Unit
) {
    val points = pointsMap[team._id]?.toString() ?: ""
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = team.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        OutlinedTextField(
            value = pointsMap[team._id]?.toString() ?: "",
            placeholder = { Text(pointsMap[team._id]?.toString() ?: "0") },
            onValueChange = { input ->
                val points = input.toIntOrNull() ?: 0
                onPointsUpdated(points)
            },
            label = { Text("Points") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
    }
}

