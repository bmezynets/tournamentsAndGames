package com.example.tournamentsandgames.data.repository

import com.example.tournamentsandgames.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserRepository private constructor() {
    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: UserRepository().also { instance = it }
            }
    }

    // Initialize FirebaseAuth and FirebaseDatabase instances
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    // Function to create a new user in the database
    suspend fun createUser(uid: String, email: String) {
        try {
            val user = User(uid, email)
            database.child(uid).setValue(user).await()
        } catch (e: Exception) {
            throw e
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
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun updateUserName(name: String, surname: String, onComplete: (Boolean, Exception?) -> Unit) {
        val user: FirebaseUser? = auth.currentUser

        user?.let {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("$name $surname")
                .build()

            it.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete(true, null)
                    } else {
                        onComplete(false, task.exception)
                    }
                }
        } ?: run {
            onComplete(false, Exception("User not logged in"))
        }
    }

    // Function to log out the current user
    fun logout() {
        auth.signOut()
    }
}
