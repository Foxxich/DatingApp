package com.example.datingapp.firebase

import android.net.Uri
import com.example.datingapp.user.UserData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface FirebaseController {

    suspend fun isCurrentUserRegistered(email: String, password: String): AuthResult?

    suspend fun isCurrentUserSigned(): Boolean

    fun getCurrentUserId(): String?

    suspend fun createNewUser(email: String, password: String)

    fun deleteUser()

    fun logout()

    fun uploadUserAccount(userData: UserData)

    fun uploadMatch()

    fun uploadPhoto(imageUri: Uri)

}