package com.example.tournamentsandgames.ui.tournaments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.auth.AuthViewModel
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.tournaments.ui.theme.TournamentsAndGamesTheme

class AddTournamentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TournamentsAndGamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddTournamentScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTournamentScreen() {
    val tournamentViewModel: TournamentViewModel = TournamentViewModel()
    val addTournamentState by tournamentViewModel.addTournamentState.collectAsState()

    // Pamiętanie wartości wpisanych przez użytkownika
    var tournamentName by remember { mutableStateOf("") }
    var numberOfRounds by remember { mutableStateOf("") }
    val activity = LocalContext.current as? ComponentActivity
    val currentUser = AuthViewModel().getCurrentUser()
    // Obsługa widoku w zależności od stanu dodawania turnieju
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = tournamentName,
            onValueChange = { tournamentName = it },
            label = { Text("Tournament Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = numberOfRounds,
            onValueChange = { numberOfRounds = it },
            label = { Text("Number of Rounds") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Dodanie nowego turnieju
                val tournament = Tournament(
                    id = "",
                    name = tournamentName,
                    rounds = numberOfRounds.toIntOrNull() ?: 0,
                    createdBy = currentUser!!.uid
                )

                try {
                    tournamentViewModel.addTournament(tournament)
                    activity!!.startActivity(Intent(activity, Home::class.java))
                } catch (e: Exception) {
                    Log.e("FirebaseError", "Error fetching data: ${e.message}")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Add Tournament")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Obsługa różnych stanów dodawania turnieju
        when (addTournamentState) {
            is FirebaseResult.Success -> {
                Text("Tournament added successfully!")
                activity?.finish()
            }
            is FirebaseResult.Error -> {
                val errorMessage = (addTournamentState as FirebaseResult.Error).exception.message
                Text("FIREBASE ADD TOURNAMENT ERROR: $errorMessage")
            }
            else -> Log.d("FIREBASE ADD TOURNAMENT", FirebaseResult.Loading.toString())
        }
    }
}