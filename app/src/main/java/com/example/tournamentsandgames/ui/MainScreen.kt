package com.example.tournamentsandgames.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tournamentsandgames.ui.auth.LoginScreen
import com.example.tournamentsandgames.ui.home.HomeScreen

@Composable
fun MainScreen() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                // Po udanym zalogowaniu nawiguj na ekran główny
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }  // Usuwa ekran logowania z backstacku
                }
            })
        }

        composable("home") {
            HomeScreen() // Ekran główny aplikacji po zalogowaniu
        }
    }
}