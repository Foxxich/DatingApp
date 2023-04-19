package com.example.datingapp.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseControllerImpl: FirebaseController {

    private val firebaseAuth: FirebaseAuth = Firebase.auth
    override var isUserAuthorized: Boolean = false

    override fun isCurrentUserRegistered(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isUserAuthorized = task.isSuccessful
                Log.i("IS_USER_REGISTERED", isUserAuthorized.toString())
            }
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