package com.example.datingapp.firebase

import android.net.Uri
import com.example.datingapp.user.Message
import com.example.datingapp.user.UserData
import com.google.firebase.auth.AuthResult

interface FirebaseController {

    suspend fun isCurrentUserRegistered(email: String, password: String): AuthResult?

    suspend fun isCurrentUserSigned(): Boolean

    fun getCurrentUserId(): String?

    suspend fun createNewUser(email: String, password: String)

    fun deleteUser()

    fun logout()

    fun uploadUserAccount(userData: UserData)

    fun uploadPhoto(imageUri: Uri)

    suspend fun getFirebaseUserData(): UserData?

    suspend fun getFirebaseUserPhoto(): Uri?

    suspend fun getFirebaseOtherUserPhoto(userId: String): Uri

    suspend fun getUsersDataList(): List<UserData>

    fun updateFirebaseUserData(userData: UserData)

    suspend fun getUserDataFromIdFirebase(userId: String): UserData?
}