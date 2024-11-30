package com.example.tournamentsandgames.ui.auth

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tournamentsandgames.R
import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.example.tournamentsandgames.ui.home.Home
import com.example.tournamentsandgames.ui.home.ui.theme.colorMain
import com.example.tournamentsandgames.ui.home.ui.theme.primaryColor
import com.example.tournamentsandgames.ui.home.ui.theme.tintColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val viewModel: AuthViewModel = viewModel()
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
            modifier = Modifier.fillMaxWidth()
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

            // Text for Password Reset
            Text(
                text = "Zapomniałeś hasło?",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = colorMain,
                modifier = Modifier.clickable{
                    context.startActivity(Intent(context, ResetPasswordActivity::class.java))
                }
            )
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
