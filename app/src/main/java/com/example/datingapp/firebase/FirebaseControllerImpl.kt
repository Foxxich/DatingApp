package com.example.datingapp.firebase

import android.util.Log
import com.example.datingapp.user.UserData
import com.example.datingapp.utils.CommonSettings.TAG
import com.example.datingapp.utils.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseControllerImpl : FirebaseController {

    private val firebaseAuth: FirebaseAuth = Firebase.auth
    private val database = Firebase.firestore

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

    override fun getCurrentUser() = firebaseAuth.currentUser

    override fun uploadUserAccount(userData: UserData) {
        database.collection("users")
            .document(firebaseAuth.currentUser?.uid ?: throw FirebaseException("UID is null"))
            .set(userData)
            .addOnSuccessListener {
                Log.e(TAG, "UserData successfully uploaded!")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
            }
    }

    override fun uploadMatch() {
        TODO("Not yet implemented")
    }

}