package com.example.tournamentsandgames.data.firebase

import com.example.tournamentsandgames.data.repository.FirebaseResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseService {
    private val auth = FirebaseAuth.getInstance()

    suspend fun signIn(email: String, password: String): FirebaseResult<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            FirebaseResult.Success(Unit) // Return success result with Unit
        } catch (e: Exception) {
            FirebaseResult.Error(e) // Return error result
        }
    }

    suspend fun signUp(email: String, password: String): FirebaseResult<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            FirebaseResult.Success(Unit) // Return success result with Unit
        } catch (e: Exception) {
            FirebaseResult.Error(e) // Return error result
        }
    }
}
