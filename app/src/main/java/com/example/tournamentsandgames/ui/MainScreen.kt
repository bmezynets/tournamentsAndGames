package com.example.tournamentsandgames.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tournaments & Games") },
                actions = {
                    IconButton(onClick = { /* Handle logout action */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Logout")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Welcome to the Tournaments & Games App!", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("tournaments") }, // Navigate to Tournament Screen
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "View Tournaments")
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("profile") }, // Navigate to Profile Screen
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "View Profile")
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* Additional actions can be added here */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Other Features")
                }
            }
        }
    )
}
