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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.model.Player
import com.example.tournamentsandgames.data.model.Team
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.ui.theme.Purple40
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
        val tournamentRaw = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
                    AddTeamScreen(tournamentRaw)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeamScreen(tournamentRaw: Tournament?) {
    val tournament = tournamentRaw
    val activity = LocalContext.current as? ComponentActivity
    var teamName by remember { mutableStateOf("") }
    var playerName by remember { mutableStateOf("") }
    val teamsList = remember { mutableStateListOf<Team>() }
    teamsList.addAll(tournament?.teams ?: listOf())

    var showAddTeamDialog by remember { mutableStateOf(false) }
    var showTeamDialog by remember { mutableStateOf(false) }
    val playersList = remember { mutableStateListOf<String>() }

    val tournamentViewModel:TournamentViewModel = viewModel()
    val playerViewModel:PlayerViewModel = viewModel()
    val teamViewModel:TeamViewModel = viewModel()
    val clickedTeam = remember { mutableStateOf(Team()) }
    val context = LocalContext.current

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
                            text = "${tournament?.name}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                // Open Add Team Dialog
                Button(
                    onClick = { showAddTeamDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(primaryColor)
                ) {
                    Text("Dodaj")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display List of Teams (Cards)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    items(teamsList.count()) { index ->
                        TeamCard1(
                            team = teamsList[index],
                            onClick = {
                                clickedTeam.value = teamsList[index]
                                showTeamDialog = true
                            },
                            onDelete = {
                                teamsList.remove(teamsList[index])
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if(teamsList.size == 1) {
                        Toast.makeText(activity, "W turnieju muśi być więcej niż 1 zespół!", Toast.LENGTH_SHORT).show()
                    } else if (teamsList.isEmpty()) {
                        Toast.makeText(activity, "Nie zostało podano zespołów! Podaj uczęstników i przejdź dalej.", Toast.LENGTH_SHORT).show()
                    } else {
                        tournament!!.teams = teamsList
                        tournamentViewModel.addTournament(tournament)
                        Toast.makeText(activity, "Pomyślno zapisano turniej!", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(activity, Home::class.java))
                        activity!!.finish()
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
        }
    }

    // Add Team Dialog
    if (showAddTeamDialog) {
        Dialog(
            onDismissRequest = { showAddTeamDialog = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Dodaj drużynę",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Team Name Input
                    Text("Nazwa")

                    Spacer(modifier = Modifier.height(6.dp))

                    TextField(
                        value = teamName,
                        onValueChange = { teamName = it },
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
                        shape = RoundedCornerShape(10.dp),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Player Name Input
                    Text("Imię zawodnika")

                    Spacer(modifier = Modifier.height(6.dp))

                    TextField(
                        value = playerName,
                        onValueChange = { playerName = it },
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
                        shape = RoundedCornerShape(10.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Button to Add Player to List
                    Button(
                        onClick = {
                            if (playerName.isNotEmpty()) {
                                playersList.add(playerName)
                                playerName = ""
                            } else {
                                Toast.makeText(activity, "Musisz podać imię!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(primaryColor)

                    ) {
                        Text("Dodaj zawodnika")
                    }

                    // List of Added Players
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .heightIn(max = 200.dp)
                    ) {
                        items(playersList.size) { idx ->
                            PlayersCard(
                                player = playersList[idx],
                                onDelete = {
                                playersList.remove(
                                    playersList[idx]
                                )
                            })
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to Save Team
                    Button(
                        onClick = {
                            if (teamName.isNotEmpty() && playersList.isNotEmpty()) {
                                val newTeam = Team(
                                    id = "",
                                    _id = UUID.randomUUID().toString(),
                                    name = teamName,
                                    points = 0,
                                    players = playersList.map { Player(id = "", _id = UUID.randomUUID().toString(), name = it, surname = "", tournamentId = tournament!!._id) },
                                    tournamentId = tournament!!._id
                                )
                                teamsList.add(newTeam)

                                // Clear fields and close modal
                                try {
                                    val teamPlayers = mutableListOf<Player>()
                                    val teamId = UUID.randomUUID().toString()

                                    playersList.forEach {name ->
                                        val nameFormatted = name.split(" ")

                                        val player  = Player(
                                            id = "",
                                            _id = UUID.randomUUID().toString(),
                                            name = nameFormatted[0] ,
                                            surname = if (nameFormatted.size > 1) nameFormatted[1] else "",
                                            tournamentId = tournament._id,
                                            teamId = teamId
                                        )

                                        teamPlayers.add(player)
                                        playerViewModel.addPlayer(player)
                                    }

                                    val team  = Team (
                                        id = "",
                                        _id = UUID.randomUUID().toString(),
                                        name = teamName,
                                        points = 0,
                                        players = teamPlayers,
                                        tournamentId = tournament._id
                                    )

                                    teamViewModel.addTeam(team)
                                } catch (e: Exception) {
                                    Toast.makeText(activity, "Błąd w trakcie dodania drzużyny!", Toast.LENGTH_SHORT).show()
                                    Log.e("ADD TEAM ERROR", e.message.toString())
                                }

                                teamName = ""
                                playersList.clear()
                                showAddTeamDialog = false
                            } else {
                                Toast.makeText(activity, "Lista zawodników nie może być pusta!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(colorMain)
                    ) {
                        Text("Zapisz Drużynę")
                    }
                }
            }
        }
    }

    // Show Team Dialog
    if (showTeamDialog) {
        Dialog(
            onDismissRequest = { showTeamDialog = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 30.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
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
                                painter = painterResource(id = R.drawable.group),
                                contentDescription = "Group Picture",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = clickedTeam.value.name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display List of Teams (Cards)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        elevation = CardDefaults.cardElevation(1.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ){
                        LazyColumn(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                        ) {
                            items(clickedTeam.value.players.count()) { index ->
                                ShowTeamPlayersCard(
                                    player = clickedTeam.value.players[index],
                                    onDelete = {
                                        playerViewModel.deletePlayer(clickedTeam.value.players[index]._id)

                                        val newTeamPlayers = clickedTeam.value.players.filter { it._id != clickedTeam.value.players[index]._id }

                                        clickedTeam.value = clickedTeam.value.copy(players = newTeamPlayers)
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { showTeamDialog = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(primaryColor)
                        ) {
                            Text("Zamknij")
                        }
                    }
                }
            }
        }
    }
}

// Existing TeamCard1 function
@Composable
fun TeamCard1(team: Team, onClick: () -> Unit,  onDelete: () -> Unit) {
    val activity = LocalContext.current as? ComponentActivity
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
                ),
                modifier = Modifier
                    .weight(9f)
                    .padding(end = 8.dp)
            )

            Box(
                modifier = Modifier
                    .weight(2f)
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Purple40)
            ) {
                IconButton(
                    onClick = {
                        val teamViewModel = TeamViewModel()
                        try {
                            teamViewModel.deleteTeam(team.id)
                            onDelete()
                            Toast.makeText(activity, "Pomyślnie usunięto zespół", Toast.LENGTH_SHORT).show()
                        } catch (ex: Exception) {
                            Toast.makeText(activity, "Błąd w trakcie usuwania. Spróbuj ponownie", Toast.LENGTH_SHORT).show()
                            Log.e("DELETE TEAM ERROR", ex.message.toString())
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = colorMain,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PlayersCard(player: String, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
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
                text = player,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .weight(10f)
                    .padding(end = 8.dp)
            )

            IconButton(
                onClick = {
                    onDelete()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Purple40
                )
            }
        }
    }
}

@Composable
fun ShowTeamPlayersCard(player: Player, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
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
                text = "${player.name} ${player.surname}",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .weight(10f)
                    .padding(end = 8.dp)
            )

            IconButton(
                onClick = {
                    onDelete()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Purple40
                )
            }
        }
    }
}
