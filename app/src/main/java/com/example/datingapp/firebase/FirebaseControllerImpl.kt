package com.example.datingapp.firebase

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseControllerImpl : FirebaseController {

    private val firebaseAuth: FirebaseAuth = Firebase.auth
    override var isUserAuthorized: Boolean = false

    override suspend fun isCurrentUserRegistered(email: String, password: String): AuthResult? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override fun isCurrentUserSigned(): Boolean = firebaseAuth.currentUser != null

    override fun createNewUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.i("IS_FIREBASE_USER_CREATE_SUCCESS", it.isSuccessful.toString())
            }
    }

    override fun deleteUser() {
        firebaseAuth.currentUser?.delete()
    }

    override fun logout() {
        Firebase.auth.signOut()
    }

}