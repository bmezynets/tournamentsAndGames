package com.example.tournamentsandgames.ui.tournaments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.model.Player
import com.example.tournamentsandgames.data.model.Team
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.ui.auth.AuthViewModel
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.home.ui.theme.tintColor
import com.example.tournamentsandgames.ui.tournaments.ui.theme.TournamentsAndGamesTheme
import kotlinx.coroutines.delay
import java.util.UUID

class CreateTournamentSummaryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    Step3(tournament)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step3(tournament: Tournament?) {
    val activity = LocalContext.current as? ComponentActivity
    val playerViewModel = PlayerViewModel()
    val tournamentViewModel = TournamentViewModel()

    if(tournament === null) {
        Toast.makeText(activity, "Błąd w trakcie dodania gracza! Spróbuj ponownie", Toast.LENGTH_SHORT).show()
        LaunchedEffect(Unit) {
            delay(4000)
            activity?.startActivity(Intent(activity, Home::class.java))
            activity?.finish()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Karta dla użytkownika
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
                    text = "${tournament?.name}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Liczba rund: ${tournament?.rounds}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Podsumowanie",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(tournament!!.teams.count()) { team ->
                    TeamCard(team = tournament.teams[team]) {
                        Toast.makeText(activity, "Clicked on ${tournament.teams[team].name}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    try {
                        tournament!!.teams.forEach { team ->
                            team.players.forEach { player ->
                                player.teamId = team._id
                                playerViewModel.addPlayer(player)
                            }
                        }
                        tournament.ended = false

                        tournamentViewModel.addTournament(tournament)
                        activity?.startActivity(Intent(activity, Home::class.java))
                        activity?.finish()
                    } catch (e: Exception) {
                        Log.e("FirebaseError", "Error fetching data: ${e.message}")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(primaryColor)
            ) {
                Text("Dodaj turniej")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TeamCard(team: Team, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = tintColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = team.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal
            )
        }
    }
}