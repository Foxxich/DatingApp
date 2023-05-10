package com.example.datingapp.firebase

import android.net.Uri
import android.util.Log
import com.example.datingapp.user.Chat
import com.example.datingapp.user.Message
import com.example.datingapp.user.UserData
import com.example.datingapp.utils.CommonSettings.TAG
import com.example.datingapp.utils.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class FirebaseControllerImpl : FirebaseController {

    private val firebaseAuth = Firebase.auth
    private val database = Firebase.firestore
    private val storageRef = Firebase.storage.reference
    private val userSettings = mutableMapOf<String, Boolean>()

    override suspend fun isCurrentUserRegistered(email: String, password: String): AuthResult? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun isCurrentUserSigned(): Boolean {
        return firebaseAuth.currentUser != null && database.collection("users_settings")
            .document(firebaseAuth.currentUser?.uid ?: throw FirebaseException("UID is null"))
            .get().await().get("isRegistrationFinished") as Boolean
    }

    override suspend fun createNewUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.i("IS_FIREBASE_USER_CREATE_SUCCESS", it.isSuccessful.toString())
                userSettings["isRegistrationFinished"] = false
                database.collection("users_settings")
                    .document(
                        firebaseAuth.currentUser?.uid ?: throw FirebaseException("UID is null")
                    )
                    .set(userSettings)
                    .addOnSuccessListener {
                        Log.e(TAG, "UserSettings successfully uploaded!")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error adding UserSettings", e)
                    }
            }
    }

    override fun deleteUser() {
        firebaseAuth.currentUser?.delete()
    }

    override fun logout() {
        Firebase.auth.signOut()
    }

    override fun getCurrentUserId() = firebaseAuth.currentUser?.uid

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

        userSettings["isRegistrationFinished"] = true

        database.collection("users_settings")
            .document(firebaseAuth.currentUser?.uid ?: throw FirebaseException("UID is null"))
            .set(userSettings)
            .addOnSuccessListener {
                Log.e(TAG, "UserSettings successfully uploaded!")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding UserSettings", e)
            }
    }

    override fun uploadPhoto(imageUri: Uri) {
        getCurrentUserId()?.let { storageRef.child(it).putFile(imageUri) }
    }

    override suspend fun getFirebaseUserData(): UserData? {
        return database.collection("users")
            .document(firebaseAuth.currentUser?.uid ?: throw FirebaseException("UID is null"))
            .get()
            .await().toObject<UserData>()
    }

    override suspend fun getFirebaseUserPhoto(): Uri? {
        return firebaseAuth.currentUser?.let { storageRef.child(it.uid).downloadUrl.await() }
    }

    override suspend fun getFirebaseOtherUserPhoto(userId: String): Uri {
        return storageRef.child(userId).downloadUrl.await()
    }

    override suspend fun getUsersDataList(): List<UserData> {
        return database.collection("users").get().await().toObjects()
    }

    override fun updateFirebaseUserData(userData: UserData) {
        database.collection("users")
            .document(userData.userId)
            .set(userData)
            .addOnSuccessListener {
                Log.e(TAG, "UserData successfully uploaded!")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
            }
    }

    override suspend fun getUserDataFromIdFirebase(userId: String): UserData? {
        return database.collection("users")
            .document(userId)
            .get()
            .await().toObject<UserData>()
    }

    override suspend fun getSpecificUsersDataList(notShowUsers: MutableList<String>): List<UserData> {
        return database
            .collection("users")
            .whereNotIn("userId", notShowUsers)
            .get()
            .await()
            .toObjects()
    }

}