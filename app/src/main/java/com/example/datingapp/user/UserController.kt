package com.example.datingapp.user

import android.net.Uri

interface UserController {

    var userPhoto: Uri?
    var userData: UserData
    var notSwipedUsers: MutableMap<UserData, Uri>
    var matchedWithUsers: MutableMap<UserData, Uri>
    var chatId: String
    var chatUserData: UserData

    fun setUserData()

    fun setUserPhoto()

    fun setChats()

    fun setNotSwipedUsersData()

    fun setMatchedWithUsersData()

    fun updateSwipes(userId: String, b: Boolean)

    fun getUserDataFromId(userId: String): UserData?
    fun updateChats(textMessage: String): Chat

    fun uploadToDatabase(userName: String, interests: List<Interest>)
}