package com.example.datingapp.user

import android.net.Uri

interface UserController {

    var userPhoto: Uri?
    var userData: UserData
    var notSwipedUsers: MutableMap<UserData, Uri>
    var matchedWithUsers: MutableMap<UserData, Uri>
    var chatId: String
    var chatUserData: UserData

    fun addUserName(userName: String)

    fun addUserPhoto(userPhoto: Uri)

    fun addUserInterests(userInterests: List<Interest>)

    fun setUserData()

    fun setUserPhoto()

    fun setChats()

    fun setNotSwipedUsersData()

    fun setMatchedWithUsersData()

    fun uploadToDatabase()

    fun updateChats()

    fun updateSwipes(userId: String, b: Boolean)

    fun getUserDataFromId(userId: String): UserData?

}