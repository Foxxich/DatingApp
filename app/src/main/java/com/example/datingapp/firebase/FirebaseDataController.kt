package com.example.datingapp.firebase

import android.net.Uri
import com.example.datingapp.user.UserData
import com.google.firebase.auth.AuthResult

interface FirebaseDataController {

    suspend fun isCurrentUserRegistered(email: String, password: String): AuthResult?

    suspend fun isProfileSetUp(): Boolean

    fun getCurrentUserId(): String?

    suspend fun createNewUser(email: String, password: String)

    fun uploadPhoto(imageUri: Uri)

    suspend fun getUsersDataList(): List<UserData>

    fun updateFirebaseUserData(userData: UserData)

    suspend fun getUserData(userId: String): UserData?

    suspend fun getFirebaseUserPhoto(userId: String): Uri

    suspend fun getSpecificUsersDataList(notShowUsers: MutableList<String>): List<UserData>

    fun changeFlag(userId: String)
}