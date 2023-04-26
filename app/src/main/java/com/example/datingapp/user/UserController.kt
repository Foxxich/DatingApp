package com.example.datingapp.user

import android.net.Uri

interface UserController {

    fun addUserName(userName: String)

    fun addUserPhoto(userPhoto: Uri)

    fun addUserInterests(userInterests: List<Interest>)

    fun getUserData(): UserData

    fun uploadToDatabase()

}