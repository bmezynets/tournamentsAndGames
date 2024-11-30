package com.example.tournamentsandgames.ui.tournamentProcess

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.model.Team
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.ui.theme.colorMain
import com.example.tournamentsandgames.ui.home.ui.theme.darkTint
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.tournaments.TeamViewModel
import com.example.tournamentsandgames.ui.tournaments.TournamentViewModel

class EndedTournamentSummary : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tournamentId = intent.getStringExtra("tournamentId") ?: ""
        setContent {
            TournamentSummary(tournamentId)
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TournamentSummary(tournamentId: String) {
    val tournamentViewModel: TournamentViewModel = viewModel()
    val teamViewModel: TeamViewModel = viewModel()

    val tournamentState by tournamentViewModel.getTournamentByIdState.collectAsState()
    val teamsState by teamViewModel.teamsState.collectAsState()
    val getSortedListState by teamViewModel.getSortedListState.collectAsState()

    var teamsList  by remember { mutableStateOf(mutableListOf<Team>()) }
    var tournament by remember { mutableStateOf(Tournament()) }
    var sortedTeamsList by remember { mutableStateOf(mutableListOf<Team>()) }

    val activity =  LocalContext.current as? ComponentActivity

    LaunchedEffect(tournamentId) {
        tournamentViewModel.getTournamentsById(tournamentId)
        teamViewModel.getTeamByTournamentId(tournamentId)
        teamViewModel.getSortedTeamsByPoints(tournamentId)
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
                when (val result = tournamentState) {
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

                when (val result = teamsState) {
                    is FirebaseResult.Success -> {
                        teamsList = result.data.toMutableList()
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

                when (val result = getSortedListState) {
                    is FirebaseResult.Success -> {
                        sortedTeamsList = result.data.toMutableList()
                        if (sortedTeamsList.size < 2) {
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
                Log.d("TEMS SIZE", "SORTED ${sortedTeamsList.size} DEFAULT ${teamsList.size}")
                if(sortedTeamsList.isNotEmpty()) {
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
                                painter = painterResource(id = R.drawable.winner),
                                contentDescription = "Winner Picture",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                            Text(
                                text = "Zwycięzca",
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = sortedTeamsList[0].name,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Wynik: ${sortedTeamsList[0].points}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    //Nie pokazujemy Zwycięzce bo jest wyswietlony wyzej
                    sortedTeamsList.removeAt(0)

                    LazyColumn (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .heightIn(max = 200.dp)
                    ){
                        itemsIndexed(sortedTeamsList) { idx, team ->
                            SummaryTeamRaw(
                                team = team,
                                place = idx,
                                onClick = {

                                }
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val intent = Intent(activity, Home::class.java)
                            intent.putExtra("reload", true)
                            activity!!.startActivity(intent)
                            activity.finish()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(primaryColor)
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryTeamRaw(team: Team, place: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = darkTint.copy(alpha = 0.8f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(darkTint.copy(alpha = 0.8f))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkTint.copy(alpha = 0.8f)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${place + 2}.",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(0.1f),
                    color = colorMain.copy(alpha = 0.7f)
                )
                Text(
                    text = team.name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(0.4f),
                    color = Color.Black.copy(alpha = 0.7f)
                )
                Text(
                    text = "Punkty: ${team.points}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.3f),
                    textAlign = TextAlign.End,
                    color = Color.Black.copy(alpha = 0.7f)
                )
                Icon(
                    modifier = Modifier.weight(0.2f),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "forward",
                    tint = colorMain
                )
            }
        }
    }
}