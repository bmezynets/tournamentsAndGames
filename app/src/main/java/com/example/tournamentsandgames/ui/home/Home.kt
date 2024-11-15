package com.example.tournamentsandgames.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.model.Team
import com.example.tournamentsandgames.data.model.Tournament
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.auth.AuthViewModel
import com.example.tournamentsandgames.ui.home.ui.theme.Pink40
import com.example.tournamentsandgames.ui.home.ui.theme.Purple80
import com.example.tournamentsandgames.ui.home.ui.theme.TournamentsAndGamesTheme
import com.example.tournamentsandgames.ui.home.ui.theme.colorMain
import com.example.tournamentsandgames.ui.home.ui.theme.darkTint
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.home.ui.theme.tintColor
import com.example.tournamentsandgames.ui.profile.EditPersonalData
import com.example.tournamentsandgames.ui.theme.Pink80
import com.example.tournamentsandgames.ui.tournaments.AddTournamentActivity
import com.example.tournamentsandgames.ui.tournaments.TeamCard
import com.example.tournamentsandgames.ui.tournaments.TournamentDescriptionActivity
import com.example.tournamentsandgames.ui.tournaments.TournamentViewModel

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TournamentsAndGamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val tabs = listOf("Home", "Tournaments", "Profile")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(tab) },
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.Home, contentDescription = "Start")
                                1 -> Icon(Icons.Filled.Star, contentDescription = "Turnieje")
                                2 -> Icon(Icons.Default.Person, contentDescription = "Profil")
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTabIndex) {
                0 -> HomeContent()
                1 -> TournamentsContent()
                2 -> ProfileContent()
            }
        }
    }
}

@Composable
fun HomeContent() {
    HomeScreen()
}

@Composable
fun TournamentsContent() {
    val tournamentViewModel = TournamentViewModel()
    val tournamentsState by tournamentViewModel.tournamentsState.collectAsState()

    LaunchedEffect(Unit) {
        tournamentViewModel.getTournaments()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tournieje",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (val result = tournamentsState) {
            is FirebaseResult.Loading -> CircularProgressIndicator()
            is FirebaseResult.Success -> {
                val tournaments = result.data
                if (tournaments.isNotEmpty()) {
                    LazyColumn {
                        items(tournaments) { tournament ->
                            HomeCard(tournament = tournament) {
                                // Handle Tournament Click
                            }
                        }
                    }
                } else {
                    Text("No Tournaments Found")
                }
            }
            is FirebaseResult.Error -> {
                Text("Error: ${result.exception.message}")
            }
        }
    }
}

@Composable
fun ProfileContent() {
    val authViewModel = AuthViewModel()
    val currentUser = authViewModel.getCurrentUser()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentUser != null) {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Name: ${currentUser.displayName}")
            Text(text = "Email: ${currentUser.email}")
        } else {
            Text("No User Data Available")
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, EditPersonalData::class.java))
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Edit Profile")
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
        if (isLogged && !currentUser?.displayName.isNullOrEmpty()) {
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

        Spacer(modifier = Modifier.height(16.dp))

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

                Text(
                    text = "Twoje turnieje:",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                when (val result = tournamentsState) {
                    is FirebaseResult.Loading -> {
                        CircularProgressIndicator()
                    }
                    is FirebaseResult.Success -> {
                        val tournaments = result.data
                        if (tournaments.isNotEmpty()) {
                            LazyColumn {
                                items(tournaments) { tournament ->
                                    HomeCard(tournament = tournament) {
                                        val intent = Intent(context, TournamentDescriptionActivity::class.java)
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
}

@Composable
fun HomeCard(tournament: Tournament, onClick: () -> Unit) {
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
                    text = tournament.name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(0.5f),
                    color = Color.Black.copy(alpha = 0.7f)
                )
                Text(
                    text = "Rundy: ${tournament.rounds}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.3f),
                    textAlign = TextAlign.End,
                    color = Color.Black.copy(alpha = 0.7f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "forward",
                    tint = colorMain
                )
            }
        }
    }
}