package com.example.datingapp.user

import android.net.Uri
import com.example.datingapp.chat.Chat

interface UserController : UserDataObserver {

    var userDataCollection: UserDataCollection

    fun setChats()

    fun setNecessaryData()

    fun updateSwipes(userId: String, liked: Boolean)

    fun getUserDataFromId(userId: String): UserData?

    fun updateChats(textMessage: String, chatId: String): Chat

    fun updateProfile(userData: UserData)

    fun uploadToDatabase(userName: String, interests: List<Interest>, imageUri: Uri)

    override fun dataChanged(userData: UserData)
}