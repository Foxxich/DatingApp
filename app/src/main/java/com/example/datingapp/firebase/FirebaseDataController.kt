package com.example.datingapp.firebase

import android.net.Uri
import com.example.datingapp.user.UserData
import com.example.datingapp.user.UserDataObserver

interface FirebaseDataController {

    var observers: MutableList<UserDataObserver>

    suspend fun isProfileSetUp(): Boolean

    suspend fun createNewUser(email: String, password: String)

    fun setUserProfileSetUp(userId: String)

    fun getCurrentUserId(): String?

    fun setFirebasePhoto(imageUri: Uri)

    fun setFirebaseUserData(userData: UserData)

    fun getFirebaseUserData(userId: String): UserData?

    suspend fun getFirebaseUserPhoto(userId: String): Uri

    suspend fun getNotInUsersDataList(notShowUsers: MutableList<String>): List<UserData>

    fun addObserver(observer: UserDataObserver)

    fun removeObserver(observer: UserDataObserver)

    fun deleteData(userId: String)

}