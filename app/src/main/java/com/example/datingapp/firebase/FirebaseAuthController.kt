package com.example.datingapp.firebase

interface FirebaseAuthController {

    suspend fun isCurrentUserSigned(): Boolean

    fun deleteUser()

    fun logout()
}