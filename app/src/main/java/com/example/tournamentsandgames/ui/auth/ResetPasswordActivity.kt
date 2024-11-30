package com.example.tournamentsandgames.ui.auth

import android.os.Bundle
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.auth.ui.theme.TournamentsAndGamesTheme
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.home.ui.theme.tintColor

class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TournamentsAndGamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResetPassword()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPassword() {
    val viewModel: AuthViewModel = viewModel()
    val resetState by viewModel.resetPasswordState.collectAsState()

    var email by remember { mutableStateOf("") }
    var showModal by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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
            text = "Resetuj hasło",
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

        Spacer(modifier = Modifier.height(16.dp))

        // Button to Trigger Password Reset
        Button(
            onClick = {
                if (email.isNotBlank()) {
                    viewModel.resetPassword(email)
                } else {
                    errorMessage = "Proszę podać adres e-mail!"
                    showModal = true
                }
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(primaryColor)
        ) {
            Text("Resetuj hasło")
        }

        if (showModal) {
            AlertDialog(
                onDismissRequest = { showModal = false },
                title = { Text(text = if (successMessage.isNotBlank()) "Sukces" else "Błąd") },
                text = { Text(text = if (successMessage.isNotBlank()) successMessage else errorMessage) },
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

        // Observe reset state
        when (resetState) {
            is FirebaseResult.Success -> {
                successMessage = "E-mail resetowania hasła został wysłany!"
                showModal = true
            }

            is FirebaseResult.Error -> {
                errorMessage =
                    viewModel.getFirebaseErrorMessageReset((resetState as FirebaseResult.Error).exception)
                showModal = true
            }

            else -> {}
        }
    }
}