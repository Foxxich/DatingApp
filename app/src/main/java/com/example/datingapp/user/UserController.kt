package com.example.datingapp.user

import android.net.Uri

interface UserController {

    var userPhoto: Uri?
    var userData: UserData

    fun addUserName(userName: String)

    fun addUserPhoto(userPhoto: Uri)

    fun addUserInterests(userInterests: List<Interest>)

    fun getUserData()

    fun getUserPhoto()

    fun uploadToDatabase()

}