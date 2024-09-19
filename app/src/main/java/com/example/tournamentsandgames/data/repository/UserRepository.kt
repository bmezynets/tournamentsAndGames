package com.example.tournamentsandgames.data.repository

import com.example.tournamentsandgames.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    suspend fun createUser(uid: String, email: String) {
        try {
            val user = User(uid, email)
            database.child(uid).setValue(user).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUser(uid: String): User? {
        return try {
            val snapshot = database.child(uid).get().await()
            snapshot.getValue(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUser(user: User) {
        try {
            database.child(user.uid).setValue(user).await()
        } catch (e: Exception) {
            throw e
        }
    }

    fun getCurrentUser() = auth.currentUser

    fun logout() {
        auth.signOut()
    }
}
