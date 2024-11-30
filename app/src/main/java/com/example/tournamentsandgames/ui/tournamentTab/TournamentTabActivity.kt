package com.example.tournamentsandgames.ui.tournamentTab

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.auth.AuthViewModel
import com.example.tournamentsandgames.ui.home.HomeCard
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.tournamentProcess.EndedTournamentSummary
import com.example.tournamentsandgames.ui.tournaments.AddTournamentActivity
import com.example.tournamentsandgames.ui.tournaments.TournamentDescriptionActivity
import com.example.tournamentsandgames.ui.tournaments.TournamentViewModel

@Composable
fun TournamentTabMainPage() {
    val authViewModel: AuthViewModel = viewModel()
    val tournamentViewModel: TournamentViewModel = viewModel()
    val currentUser = authViewModel.getCurrentUser()
    val context = LocalContext.current
    val activity = LocalContext.current as? ComponentActivity

    val tournamentsState by tournamentViewModel.tournamentsState.collectAsState()

    LaunchedEffect(Unit) {
        if (currentUser != null) {
            tournamentViewModel.getTournamentsByUserId(currentUser.uid)
        } else {
            tournamentViewModel.getTournaments()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Twoje turnieje:",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    context.startActivity(Intent(context, AddTournamentActivity::class.java))
                    activity?.finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(primaryColor)
            ) {
                Text("Dodaj nowy turniej")
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (val result = tournamentsState) {
                is FirebaseResult.Loading -> {
                    CircularProgressIndicator()
                }
                is FirebaseResult.Success -> {
                    val tournaments = result.data.reversed()
                    if (tournaments.isNotEmpty()) {
                        LazyColumn {
                            items(tournaments) { tournament ->
                                HomeCard(tournament = tournament) {
                                    val intent =
                                        if(!tournament.ended)
                                            Intent(context, TournamentDescriptionActivity::class.java)
                                        else
                                            Intent(context, EndedTournamentSummary::class.java)
                                    intent.putExtra("tournamentId", tournament._id)
                                    intent.putExtra("tournament", tournament)
                                    context.startActivity(intent)
                                }
                            }
                        }
                    } else {
                        Text("Nie masz jeszcze turniejów.")
                    }
                }
                is FirebaseResult.Error -> {
                    Text("Błąd: ${result.exception.message}")
                }
            }
        }
    }
}