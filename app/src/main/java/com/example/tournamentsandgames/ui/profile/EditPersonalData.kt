package com.example.tournamentsandgames.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tournamentsandgames.data.repository.UserRepository
import com.example.tournamentsandgames.ui.auth.AuthViewModel
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.profile.ui.theme.ui.theme.TournamentsAndGamesTheme

class EditPersonalData : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TournamentsAndGamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UpdateUserScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserScreen() {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel = AuthViewModel()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Name Input Field
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Surname Input Field
        TextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Enter Surname") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Save Button
        Button(
            onClick = {
                authViewModel.updateUserName(name, surname)
                context.startActivity(Intent(context, Home::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    TournamentsAndGamesTheme {
        UpdateUserScreen()
    }
}