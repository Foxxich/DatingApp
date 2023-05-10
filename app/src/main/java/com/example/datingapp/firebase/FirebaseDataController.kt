package com.example.datingapp.firebase

import android.net.Uri
import com.example.datingapp.user.UserData
import com.google.firebase.auth.AuthResult

interface FirebaseDataController {

    suspend fun isCurrentUserRegistered(email: String, password: String): AuthResult?

    suspend fun isProfileSetUp(): Boolean

    fun getCurrentUserId(): String?

    suspend fun createNewUser(email: String, password: String)

    fun uploadUserAccount(userData: UserData)

    fun uploadPhoto(imageUri: Uri)

    suspend fun getFirebaseUserData(): UserData?

    suspend fun getFirebaseUserPhoto(): Uri?

    suspend fun getFirebaseOtherUserPhoto(userId: String): Uri

    suspend fun getUsersDataList(): List<UserData>

    fun updateFirebaseUserData(userData: UserData)

    suspend fun getUserDataFromIdFirebase(userId: String): UserData?

    suspend fun getSpecificUsersDataList(notShowUsers: MutableList<String>): List<UserData>
}