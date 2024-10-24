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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.example.tournamentsandgames.data.model.Team
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.ui.auth.AuthViewModel
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.ui.theme.colorMain
import com.example.tournamentsandgames.ui.home.ui.theme.darkTint
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.home.ui.theme.tintColor
import com.example.tournamentsandgames.ui.tournaments.ui.theme.TournamentsAndGamesTheme
import kotlinx.coroutines.delay
import java.util.UUID

class AddTeamActivity : ComponentActivity() {
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
                    AddTeamScreen(tournament)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeamScreen(tournament: Tournament?) {
    val activity = LocalContext.current as? ComponentActivity
    var teamName by remember { mutableStateOf("") }
    var teamsList by remember { mutableStateOf(tournament?.teams ?: mutableListOf()) }

    if (tournament == null) {
        Toast.makeText(activity, "Błąd w trakcie dodania drużyny! Spróbuj ponownie", Toast.LENGTH_SHORT).show()
        LaunchedEffect(Unit) {
            delay(4000)
            activity?.startActivity(Intent(activity, Home::class.java))
            activity?.finish()
        }
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
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Dodaj Drużynę",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = teamName,
                            onValueChange = { teamName = it },
                            label = { Text("Nazwa drużyny") },
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

                    // Add Button
                    Button(
                        onClick = {
                            if (teamName.isNotEmpty()) {
                                val newTeam = Team(
                                    id = "",
                                    _id = UUID.randomUUID().toString(),
                                    name = teamName,
                                    points = 0
                                )
                                teamsList.add(newTeam)
                                teamName = ""
                            } else {
                                Toast.makeText(activity, "Musisz podać nazwę drużyny!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(primaryColor)
                    ) {
                        Text("Dodaj")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display List of Teams (Cards)
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(teamsList.count()) { team ->
                    TeamCard1(team = teamsList[team]) {
                        // Handle the team click event
                        Toast.makeText(activity, "Clicked on ${teamsList[team].name}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun TeamCard1(team: Team, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = darkTint.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = team.name,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "forward",
                tint = colorMain
            )
        }
    }
}
