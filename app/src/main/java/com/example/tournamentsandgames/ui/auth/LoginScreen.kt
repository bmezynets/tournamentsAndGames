package com.example.tournamentsandgames.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tournamentsandgames.data.repository.FirebaseResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val viewModel: AuthViewModel = AuthViewModel()
    // Collecting the login state from the ViewModel
    val loginState by viewModel.loginState.collectAsState()

    // State variables for email and password inputs
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
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

        // Displaying the login state
        when (loginState) {
            is FirebaseResult.Loading -> CircularProgressIndicator()
            is FirebaseResult.Success -> Text("Login Successful!")
            is FirebaseResult.Error -> Text((loginState as FirebaseResult.Error).exception.localizedMessage ?: "Unknown error")
            else -> {}
        }
    }
}
