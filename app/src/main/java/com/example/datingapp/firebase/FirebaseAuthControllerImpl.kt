package com.example.datingapp.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthControllerImpl : FirebaseAuthController {

    private val firebaseAuth = Firebase.auth

    override suspend fun isCurrentUserSigned() = firebaseAuth.currentUser != null

    override fun deleteUser() {
        firebaseAuth.currentUser?.delete()
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}