package com.example.tournamentsandgames.ui.auth

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.HomeScreen
import com.example.tournamentsandgames.ui.home.ui.theme.colorMain
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.home.ui.theme.tintColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val viewModel: AuthViewModel = AuthViewModel()
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showModal by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_turniej_app),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 64.dp)
        )

        Text(
            text = "Logowanie",
            style = TextStyle(
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TextField for Email Input
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Login") },
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
        Spacer(modifier = Modifier.height(8.dp))

        // TextField for Password Input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Hasło") },
            visualTransformation = PasswordVisualTransformation(),
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

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth() // Ensures the Column takes up full width
        ) {
            // Login Button
            Button(
                onClick = { viewModel.login(email, password) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(primaryColor),
                modifier = Modifier.width(280.dp)
            ) {
                Text("Zaloguj")
            }

            // Register Button
            Button(
                onClick = {
                    context.startActivity(Intent(context, RegistrationActivity::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(colorMain),
                modifier = Modifier.width(280.dp)
            ) {
                Text("Rejestracja")
            }
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
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }

        when (loginState) {
            is FirebaseResult.Success -> LocalContext.current.startActivity(Intent(LocalContext.current, Home::class.java))
            is FirebaseResult.Error -> showModal = true
            else -> {}
        }
    }
}
