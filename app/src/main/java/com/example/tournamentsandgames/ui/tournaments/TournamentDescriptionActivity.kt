package com.example.tournamentsandgames.ui.tournaments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.model.Team
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.HomeCard
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.tournaments.ui.theme.TournamentsAndGamesTheme
import kotlinx.coroutines.delay

class TournamentDescriptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tournamentId = intent.getStringExtra("tournamentId") ?: ""
        val tournament = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("tournament", Tournament::class.java) as Tournament
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("tournament") as Tournament?
        }
        setContent {
            TournamentsAndGamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowDescription(tournamentId)
                }
            }
        }
    }
}

@Composable
fun ShowDescription(tournamentId: String) {
    val tournamentViewModel: TournamentViewModel = viewModel()
    val teamViewModel: TeamViewModel = viewModel()

    val tournamentState by tournamentViewModel.getTournamentByIdState.collectAsState()
    val teamsState by teamViewModel.teamsState.collectAsState()

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
            ){
                when (val tournamentResult = tournamentState) {
                    is FirebaseResult.Loading -> {
                        CircularProgressIndicator()
                    }

                    is FirebaseResult.Success -> {
                        val tournaments = tournamentResult.data
                        if (tournaments.isNotEmpty()) {
                            val tournament = tournaments[0]
                            DisplayTournamentDetails(
                                tournament,
                                teamsState,
                                tournamentViewModel,
                                teamViewModel
                            )
                        } else {
                            Text("Nie masz jeszcze turniejów.")
                        }
                    }

                    is FirebaseResult.Error -> {
                        Text("Błąd: ${tournamentResult.exception.message}")
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayTournamentDetails(
    tournament: Tournament,
    teamsState: FirebaseResult<List<Team>>,
    tournamentViewModel: TournamentViewModel,
    teamViewModel: TeamViewModel
) {
    val context = LocalContext.current
    var startGameText by remember { mutableStateOf("Rozpocznij grę") }
    var isGameStarted by remember { mutableStateOf(tournament.matches.isNotEmpty()) }
    var isGameEnded by remember { mutableStateOf(tournament.matches.size >= tournament.rounds) }

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

                if (!isGameEnded) {
                    Button(
                        onClick = { isGameStarted = !isGameStarted },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(primaryColor)
                    ) {
                        Text(startGameText)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                val teams = tournament.teams
                if (teams.isNotEmpty()) {
                    LazyColumn {
                        items(teams) { team ->
                            TeamCardWithScoreInput(
                                team = team,
                                isGameStarted = isGameStarted,
                                onScoreChange = { newScore ->
                                    teamViewModel.updateTeamScore(team.id, newScore)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /*Button(
                onClick = { isGameEnded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(primaryColor)
            ) {
                Text("Zakończ turniej")
            }*/
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamCardWithScoreInput(
    team: Team,
    isGameStarted: Boolean,
    onScoreChange: (Int) -> Unit
) {
    var score by remember { mutableStateOf(team.points) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = team.name, style = MaterialTheme.typography.headlineSmall)
            if (isGameStarted) {
                OutlinedTextField(
                    value = score.toString(),
                    onValueChange = { newScore ->
                        val updatedScore = newScore.toIntOrNull() ?: score
                        score = updatedScore
                        onScoreChange(updatedScore)
                    },
                    label = { Text("Wynik") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

