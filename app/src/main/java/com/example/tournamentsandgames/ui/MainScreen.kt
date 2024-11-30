package com.example.tournamentsandgames.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Logout")
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
