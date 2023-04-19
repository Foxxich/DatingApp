package com.example.datingapp.firebase

interface FirebaseController {

    var isUserAuthorized: Boolean

    fun isCurrentUserRegistered(email: String, password: String)

    fun isCurrentUserSigned(): Boolean

    fun createNewUser(email: String, password: String)

    fun deleteUser()

    fun logout()

}