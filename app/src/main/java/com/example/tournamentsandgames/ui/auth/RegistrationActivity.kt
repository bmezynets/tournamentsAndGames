package com.example.tournamentsandgames.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.model.User
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.auth.ui.theme.TournamentsAndGamesTheme
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.home.ui.theme.tintColor

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TournamentsAndGamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegistrationScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen() {
    val viewModel: AuthViewModel = AuthViewModel()
    val registrationState by viewModel.registrationState.collectAsState()

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showModal by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
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
            text = "Rejestracja",
            style = TextStyle(
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Imię") },
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

        TextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Nazwisko") },
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

        // TextField for Email Input
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
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

        Spacer(modifier = Modifier.height(8.dp))

        // TextField for Confirm Password Input
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Potwierdź Hasło") },
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

        // Button to Trigger Registration
        Button(
            onClick = {
                if (password == confirmPassword) {
                    viewModel.register(email, password, name, surname)
                } else {
                    errorMessage = "Hasła muszą być takie same!"
                    showModal = true
                }
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(primaryColor)
        ) {
            Text("Zarejestruj")
        }

        if (showModal) {
            AlertDialog(
                onDismissRequest = { showModal = false },
                title = { Text(text = "Nieudana rejestracja") },
                text = { Text(text = errorMessage) },
                confirmButton = {
                    TextButton(
                        onClick = { showModal = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }

        // Observe registration state
        when (registrationState) {
            is FirebaseResult.Success -> {
                Toast.makeText(LocalContext.current, "Rejestracja zakończona sukcesem!", Toast.LENGTH_SHORT).show()
                val activity = LocalContext.current as Activity
                activity.finish()
            }
            is FirebaseResult.Error -> {
                errorMessage = viewModel.getFirebaseErrorMessage((registrationState as FirebaseResult.Error).exception)
                showModal = true
            }
            else -> {}
        }
    }
}