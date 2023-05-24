package com.example.datingapp.firebase

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseAuthControllerImpl : FirebaseAuthController {

    private val firebaseAuth = Firebase.auth

    override suspend fun isCurrentUserRegistered(email: String, password: String): AuthResult? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun isCurrentUserSigned() = firebaseAuth.currentUser != null

    override fun deleteUser() {
        firebaseAuth.currentUser?.delete()
    }

    override fun logout() {
        firebaseAuth.signOut()
        firebaseAuth.currentUser?.delete()
    }

    override suspend fun emailExists(email: String): Boolean {
        val sign = firebaseAuth.fetchSignInMethodsForEmail(email).await().signInMethods
        return !sign.isNullOrEmpty()
    }
}