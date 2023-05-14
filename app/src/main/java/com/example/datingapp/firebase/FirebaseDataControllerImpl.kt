package com.example.datingapp.firebase

import android.net.Uri
import android.util.Log
import com.example.datingapp.UserDataObserver
import com.example.datingapp.user.UserData
import com.example.datingapp.utils.CommonSettings.TAG
import com.example.datingapp.utils.FirebaseException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirebaseDataControllerImpl : FirebaseDataController {

    private val firebaseAuth = Firebase.auth
    private val database = Firebase.firestore
    private val storageRef = Firebase.storage.reference

    override suspend fun isProfileSetUp(): Boolean {
        return database.collection("users_settings")
            .document(firebaseAuth.currentUser?.uid ?: throw FirebaseException("UID is null"))
            .get().await().get("isRegistrationFinished") as Boolean
    }

    override suspend fun createNewUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.i("IS_FIREBASE_USER_CREATE_SUCCESS", it.isSuccessful.toString())
                database.collection("users_settings")
                    .document(
                        firebaseAuth.currentUser?.uid ?: throw FirebaseException("UID is null")
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

    override fun uploadPhoto(imageUri: Uri) {
        getCurrentUserId()?.let { storageRef.child(it).putFile(imageUri) }
    }

    override suspend fun getFirebaseUserPhoto(userId: String): Uri {
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

    override fun getUserData(userId: String): UserData? {
        val data: UserData?
        runBlocking {
            data = database.collection("users")
                .document(userId)
                .get()
                .await().toObject<UserData>()
        }
        return data
    }

    override suspend fun getSpecificUsersDataList(notShowUsers: MutableList<String>): List<UserData> {
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

    override fun changeFlag(userId: String) {
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

    override var observers = mutableListOf<UserDataObserver>()

    override fun addObserver(observer: UserDataObserver) {
        observers.add(observer)
        listenToNew()
    }

    override fun removeObserver(observer: UserDataObserver) {
        observers.remove(observer)
    }

    override fun updateMyObject(newMyObject: UserData) {
        for (observer in observers) {
            observer.dataChanged(newMyObject)
        }
    }

    override fun listenToNew() {
        getCurrentUserId()?.let {
            database
                .collection("users")
                .document(it).addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("XDDD", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("XDDD", "Current data: ${snapshot.data}")
                        val newMyObject = snapshot.toObject<UserData>()
                        if (newMyObject != null) {
                            updateMyObject(newMyObject)
                        }
                    } else {
                        Log.d("XDDD", "Current data: null")
                    }
                }
        }
    }

}