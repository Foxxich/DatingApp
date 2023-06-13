package com.example.datingapp.user

import android.net.Uri
import com.example.datingapp.chat.Chat
import com.example.datingapp.chat.Sorting

interface UserController : UserDataObserver {

    var userDataCollection: UserDataCollection
    var sorting: Sorting

    fun setChats()

    fun setNecessaryData()

    fun updateSwipes(userId: String, liked: Boolean)

    fun getUserDataFromId(userId: String): UserData?

    fun updateChats(textMessage: String, chatId: String): Chat

    fun uploadToDatabase(userName: String, interests: List<Interest>, imageUri: Uri)

    fun uploadToDatabase(userDataCollection: UserDataCollection)

    override fun dataChanged(userData: UserData)
}