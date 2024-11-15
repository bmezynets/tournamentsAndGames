package com.example.tournamentsandgames.ui.tournaments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.auth.AuthViewModel
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.ui.theme.colorMain
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.home.ui.theme.tintColor
import com.example.tournamentsandgames.ui.tournaments.ui.theme.TournamentsAndGamesTheme
import java.util.UUID

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

    var tournamentName by remember { mutableStateOf("") }
    var numberOfRounds by remember { mutableStateOf("") }
    var isTeamTournament by remember { mutableStateOf(false) }
    var isTwoPlayerTournament by remember { mutableStateOf(false) }

    val activity = LocalContext.current as? ComponentActivity
    val currentUser = AuthViewModel().getCurrentUser()

    // Obsługa widoku w zależności od stanu dodawania turnieju
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

            Spacer(modifier = Modifier.height(80.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Krok 1",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextField(
                    value = tournamentName,
                    onValueChange = { tournamentName = it },
                    label = { Text("Nazwa turnieju") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = tintColor,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        textColor = Color.Black,
                        disabledLabelColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = primaryColor,
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = numberOfRounds,
                    onValueChange = { numberOfRounds = it },
                    label = { Text("Ilość rund") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = tintColor,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        textColor = Color.Black,
                        disabledLabelColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = primaryColor,
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Typ drużyny:",
                        style = TextStyle(
                            fontSize = 20.sp
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isTeamTournament,
                        onCheckedChange = { isChecked ->
                            isTeamTournament = isChecked
                            if (isChecked) {
                                isTwoPlayerTournament = false
                            }
                        },
                        colors = CheckboxDefaults.colors(checkedColor = colorMain)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Wieloosobowa")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isTwoPlayerTournament,
                        onCheckedChange = { isChecked ->
                            isTwoPlayerTournament = isChecked
                            if (isChecked) {
                                isTeamTournament = false
                            }
                        },
                        colors = CheckboxDefaults.colors(checkedColor = colorMain)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Jednoosobowa")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Dodanie nowego turnieju
                        val tournament = Tournament(
                            id = "",
                            _id = UUID.randomUUID().toString(),
                            name = tournamentName,
                            rounds = numberOfRounds.toIntOrNull() ?: 0,
                            createdBy = currentUser!!.uid,
                            ended = false
                        )

                        try {
                            if (isTeamTournament) {
                                val intent = Intent(
                                    activity,
                                    AddTeamActivity::class.java
                                ).putExtra("tournament", tournament)
                                activity!!.startActivity(intent)
                            } else if (isTwoPlayerTournament) {
                                val intent = Intent(
                                    activity,
                                    AddPlayerActivity::class.java
                                ).putExtra("tournament", tournament)
                                activity!!.startActivity(intent)
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Musisz wybrać rodzaj turnieju!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
                    Text("Dalej")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Obsługa różnych stanów dodawania turnieju
                when (addTournamentState) {
                    is FirebaseResult.Success -> {
                        Text("Tournament added successfully!")
                        activity?.finish()
                    }

                    is FirebaseResult.Error -> {
                        val errorMessage =
                            (addTournamentState as FirebaseResult.Error).exception.message
                        Text("FIREBASE ADD TOURNAMENT ERROR: $errorMessage")
                    }

                    else -> Log.d("FIREBASE ADD TOURNAMENT", FirebaseResult.Loading.toString())
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(primaryColor)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .border(2.dp, primaryColor, CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .border(2.dp, primaryColor, CircleShape)
                )

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
