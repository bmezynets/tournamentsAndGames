package com.example.tournamentsandgames.ui.auth

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val viewModel: AuthViewModel = AuthViewModel()
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showModal by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TextField for Email Input
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // TextField for Password Input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Button to Trigger Login
        Button(onClick = { viewModel.login(email, password) }) {
            Text("Login")
        }

        if (showModal) {
            AlertDialog(
                onDismissRequest = {
                    showModal = false
                },
                title = {
                    Text(text = "Nieudane logowanie")
                },
                text = {
                    Text(text = "Logowanie się nie powiodło. Proszę sprawdzić podany email i/lub hasło.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showModal = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }

        when (loginState) {
            is FirebaseResult.Loading -> CircularProgressIndicator()
            is FirebaseResult.Success -> LocalContext.current.startActivity(Intent(LocalContext.current, Home::class.java))
            is FirebaseResult.Error -> showModal = true
            else -> {}
        }
    }
}
