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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.model.Player
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.ui.auth.AuthViewModel
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.home.ui.theme.tintColor
import com.example.tournamentsandgames.ui.tournaments.ui.theme.TournamentsAndGamesTheme
import kotlinx.coroutines.delay

class AddPlayerActivity : ComponentActivity() {
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
                    Step2(tournament)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2(tournament: Tournament?) {
    val activity = LocalContext.current as? ComponentActivity
    val playerViewModel = PlayerViewModel()

    var firstPlayerName by remember { mutableStateOf("") }
    var firstPlayerSurname by remember { mutableStateOf("") }
    var secondPlayerName by remember { mutableStateOf("") }
    var secondPlayerSurname by remember { mutableStateOf("") }

    if(tournament === null) {
        Toast.makeText(activity, "Błąd w trakcie dodania gracza! Spróbuj ponownie", Toast.LENGTH_SHORT).show()
        LaunchedEffect(Unit) {
            delay(4000)
            activity?.startActivity(Intent(activity, Home::class.java))
            activity?.finish()
        }
    }

    val currentUser = AuthViewModel().getCurrentUser()

    Column(
        modifier = Modifier.fillMaxSize(),
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
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Krok 2",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Imię:",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = firstPlayerName,
                    onValueChange = { firstPlayerName = it },
                    label = { Text("Imię") },
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nazwisko:",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = firstPlayerSurname,
                    onValueChange = { firstPlayerSurname = it },
                    label = { Text("Nazwisko") },
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val player = Player(
                        id = "",
                        name = firstPlayerName,
                        surname = firstPlayerSurname,
                        tournamentId = tournament!!._id
                    )

                    try {
                        playerViewModel.addPlayer(player)
                        activity!!.startActivity(Intent(activity, Home::class.java))
                        /*if (isTeamTournament) {
                            val intent = Intent(activity, AddTeamActivity::class.java).putExtra("tournament", tournament)
                            activity!!.startActivity(intent)
                        }
                        else if (isTwoPlayerTournament){
                            val intent = Intent(activity, AddPlayerActivity::class.java).putExtra("tournament", tournament)
                            activity!!.startActivity(intent)
                        } else {
                            Toast.makeText(activity, "Musisz wybrać rodzaj turnieju!", Toast.LENGTH_SHORT).show()
                        }*/
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
            /*when (addTournamentState) {
                is FirebaseResult.Success -> {
                    Text("Tournament added successfully!")
                    activity?.finish()
                }
                is FirebaseResult.Error -> {
                    val errorMessage = (addTournamentState as FirebaseResult.Error).exception.message
                    Text("FIREBASE ADD TOURNAMENT ERROR: $errorMessage")
                }
                else -> Log.d("FIREBASE ADD TOURNAMENT", FirebaseResult.Loading.toString())
            }*/
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
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
                    .background(primaryColor)
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