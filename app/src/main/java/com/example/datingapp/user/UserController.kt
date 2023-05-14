package com.example.datingapp.user

import android.net.Uri
import com.example.datingapp.UserDataObserver

interface UserController : UserDataObserver {

    var userPhoto: Uri?
    var userData: UserData
    var notSwipedUsersUri: MutableMap<UserData, Uri>
    var matchedWithUsersUri: MutableMap<UserData, Uri>
    var userCollection: UserCollection

    fun setChats()

    fun setNecessaryData()

    fun updateSwipes(userId: String, liked: Boolean)

    fun getUserDataFromId(userId: String): UserData?

    fun updateChats(textMessage: String, chatId: String): Chat

    fun updateProfile(userData: UserData)

    fun uploadToDatabase(userName: String, interests: List<Interest>)

    fun setMyObject(myObject: UserData) {
    }

    override fun dataChanged(userData: UserData) {}
}