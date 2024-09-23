package com.example.tournamentsandgames.data.repository

import com.example.tournamentsandgames.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserRepository {

    // Initialize FirebaseAuth and FirebaseDatabase instances
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    // Function to create a new user in the database
    suspend fun createUser(uid: String, email: String) {
        try {
            val user = User(uid, email)
            database.child(uid).setValue(user).await()
        } catch (e: Exception) {
            throw e // Rethrow the exception for handling elsewhere
        }
    }

    // Function to retrieve a user by their UID
    suspend fun getUser(uid: String): User? {
        return try {
            val snapshot = database.child(uid).get().await()
            snapshot.getValue(User::class.java) // Deserialize snapshot to User
        } catch (e: Exception) {
            null // Return null if an error occurs
        }
    }

    // Function to update user information in the database
    suspend fun updateUser(user: User) {
        try {
            database.child(user.uid).setValue(user).await()
        } catch (e: Exception) {
            throw e // Rethrow the exception for handling elsewhere
        }
    }

    // Function to get the currently authenticated user
    fun getCurrentUser() = auth.currentUser

    // Function to log out the current user
    fun logout() {
        auth.signOut()
    }
}
