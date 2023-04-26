package com.example.datingapp.firebase

import com.example.datingapp.user.UserData
import com.google.firebase.auth.AuthResult

interface FirebaseController {

    suspend fun isCurrentUserRegistered(email: String, password: String): AuthResult?

    fun isCurrentUserSigned(): Boolean

    fun createNewUser(email: String, password: String)

    fun deleteUser()

    fun logout()

    fun getUserId(): String?

    fun uploadUserAccount(userData: UserData)

    fun uploadMatch()

}