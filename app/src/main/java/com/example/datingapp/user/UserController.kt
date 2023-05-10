package com.example.datingapp.user

import android.net.Uri

interface UserController {

    var userPhoto: Uri?
    var userData: UserData
    var notSwipedUsers: MutableMap<UserData, Uri>
    var matchedWithUsers: MutableMap<UserData, Uri>

    fun setChats()

    fun setNecessaryData()

    fun updateSwipes(userId: String, liked: Boolean)

    fun getUserDataFromId(userId: String): UserData?

    fun updateChats(textMessage: String, chatId: String): Chat

    fun updateProfile(userData: UserData)

    fun uploadToDatabase(userName: String, interests: List<Interest>)
}