package com.example.datingapp.user

import android.net.Uri

interface UserController {

    var userPhoto: Uri?
    var userData: UserData
    var notSwipedUsers: MutableMap<UserData, Uri>
    var matchedWithUsers: MutableMap<UserData, Uri>

    fun addUserName(userName: String)

    fun addUserPhoto(userPhoto: Uri)

    fun addUserInterests(userInterests: List<Interest>)

    fun setUserData()

    fun setUserPhoto()

    fun setNotSwipedUsersData()

    fun setMatchedWithUsersData()

    fun uploadToDatabase()

    fun update(userId: String, b: Boolean)

}