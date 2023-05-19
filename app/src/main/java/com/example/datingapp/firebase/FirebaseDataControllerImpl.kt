package com.example.datingapp.firebase

import android.net.Uri
import android.util.Log
import com.example.datingapp.user.UserData
import com.example.datingapp.user.UserDataObserver
import com.example.datingapp.utils.CommonSettings.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class FirebaseDataControllerImpl : FirebaseDataController {

    private val firebaseAuth = Firebase.auth
    private val database = Firebase.firestore
    private val storageRef = Firebase.storage.reference
    override var observers = mutableListOf<UserDataObserver>()

    override suspend fun isProfileSetUp(): Boolean {
        try {
            return database.collection("users_settings")
                .document(firebaseAuth.currentUser?.uid ?: return false)
                .get().await().get("isRegistrationFinished") as Boolean
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun createNewUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.i("IS_FIREBASE_USER_CREATE_SUCCESS", it.isSuccessful.toString())
                database.collection("users_settings")
                    .document(
                        it.result.user!!.uid
                    )
                    .set(mutableMapOf("isRegistrationFinished" to false))
                    .addOnSuccessListener {
                        Log.e(TAG, "UserSettings successfully uploaded!")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error adding UserSettings", e)
                    }
            }
    }

    override fun getCurrentUserId() = firebaseAuth.currentUser?.uid

    override fun setFirebasePhoto(imageUri: Uri) {
        getCurrentUserId()?.let { storageRef.child(it).putFile(imageUri) }
    }

    override suspend fun getFirebaseUserPhoto(userId: String): Uri {
        return storageRef.child(userId).downloadUrl.await()
    }

    override fun setFirebaseUserData(userData: UserData) {
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

    override fun getFirebaseUserData(userId: String): UserData? {
        val data: UserData?
        runBlocking {
            data = database.collection("users")
                .document(userId)
                .get()
                .await().toObject<UserData>()
        }
        return data
    }

    override suspend fun getNotInUsersDataList(notShowUsers: MutableList<String>): List<UserData> {
        val filteredList: MutableList<UserData> = mutableListOf()
        try {
            notShowUsers.chunked(9).forEach { _ ->
                database
                    .collection("users")
                    .whereNotIn("userId", notShowUsers)
                    .get()
                    .await()
                    .toObjects<UserData>().forEach {
                        filteredList.add(it)
                    }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return filteredList
    }

    override fun setUserProfileSetUp(userId: String) {
        database.collection("users_settings")
            .document(userId)
            .set(mutableMapOf("isRegistrationFinished" to true))
            .addOnSuccessListener {
                Log.e(TAG, "UserSettings successfully uploaded!")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding UserSettings", e)
            }
    }

    override fun addObserver(observer: UserDataObserver) {
        observers.add(observer)
        listenToNew()
    }

    override fun removeObserver(observer: UserDataObserver) {
        observers.remove(observer)
    }

    override fun deleteData(userId: String) {
        database.collection("users")
            .document(userId)
            .delete()
    }

    private fun listenToNew() {
        getCurrentUserId()?.let {
            database
                .collection("users")
                .document(it).addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val newMyObject = snapshot.toObject<UserData>()
                        if (newMyObject != null) {
                            updateMyObject(newMyObject)
                        }
                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
        }
    }

    private fun updateMyObject(newMyObject: UserData) {
        for (observer in observers) {
            observer.dataChanged(newMyObject)
        }
    }
}