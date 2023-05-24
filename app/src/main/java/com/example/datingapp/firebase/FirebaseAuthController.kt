package com.example.datingapp.firebase

import com.google.firebase.auth.AuthResult

interface FirebaseAuthController {

    suspend fun isCurrentUserRegistered(email: String, password: String): AuthResult?

    suspend fun isCurrentUserSigned(): Boolean

    fun deleteUser()

    fun logout()

    suspend fun emailExists(email: String): Boolean
}