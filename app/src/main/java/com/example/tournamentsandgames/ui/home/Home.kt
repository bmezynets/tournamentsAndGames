package com.example.tournamentsandgames.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.ui.auth.AuthViewModel
import com.example.tournamentsandgames.ui.home.ui.theme.Pink40
import com.example.tournamentsandgames.ui.home.ui.theme.Purple80
import com.example.tournamentsandgames.ui.home.ui.theme.TournamentsAndGamesTheme
import com.example.tournamentsandgames.ui.profile.EditPersonalData
import com.example.tournamentsandgames.ui.theme.Pink80
import com.example.tournamentsandgames.ui.tournaments.AddTournamentActivity
import com.example.tournamentsandgames.ui.tournaments.TournamentViewModel
import com.google.firebase.auth.FirebaseUser

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TournamentsAndGamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val authViewModel: AuthViewModel = AuthViewModel()
    val tournamentViewModel = TournamentViewModel()
    val currentUser = authViewModel.getCurrentUser()
    val isLogged = authViewModel.isUserLoggedIn()
    val context = LocalContext.current
    val activity = LocalContext.current as? ComponentActivity

    val tournamentsState by remember { mutableStateOf(emptyList<Tournament>()) }

    // Pobierz listę turniejów
    LaunchedEffect(Unit) {
        tournamentViewModel.getTournaments()

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Karta dla użytkownika
        if(isLogged && !currentUser?.displayName.isNullOrEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${currentUser?.displayName}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "${currentUser?.email}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Masz niekompletne dane!")
                Button(
                    onClick = {
                        context.startActivity(Intent(context, EditPersonalData::class.java))
                        activity?.finish()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 25.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(Purple80)
                ) {
                    Text("Aktualizuj")
                }
            }
        }

        // Nowa karta dla turniejów
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        // Logika do dodawania nowego turnieju
                        context.startActivity(Intent(context, AddTournamentActivity::class.java))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Dodaj nowy turniej")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Twoje Turnieje:",
                    style = MaterialTheme.typography.headlineSmall
                )

                // Lista turniejów
                if (tournamentsState.isNotEmpty()) {
                    tournamentsState.forEach { tournament ->
                        Text(
                            text = tournament.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                } else {
                    Text("Nie masz jeszcze turniejów.")
                }
            }
        }
    }
}