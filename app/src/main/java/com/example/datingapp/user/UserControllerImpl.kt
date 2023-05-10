package com.example.datingapp.user

import android.net.Uri
import android.util.Log
import com.example.datingapp.firebase.FirebaseDataController
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserControllerImpl @Inject constructor(
    private val firebaseDataControllerImpl: FirebaseDataController
) : UserController {

    override var userPhoto: Uri? = null
    override lateinit var userData: UserData
    override var chatId: String = ""
    override var notSwipedUsers: MutableMap<UserData, Uri> = mutableMapOf()
    override var matchedWithUsers: MutableMap<UserData, Uri> = mutableMapOf()

    override fun uploadToDatabase(userName: String, interests: List<Interest>) {
        val userId = firebaseDataControllerImpl.getCurrentUserId()!!
        firebaseDataControllerImpl.updateFirebaseUserData(
            UserData(
                userId = userId,
                userName = userName,
                interests = interests,
            )
        )
        firebaseDataControllerImpl.changeFlag(userId)
        userPhoto?.let { firebaseDataControllerImpl.uploadPhoto(it) }
    }

    override fun updateChats(textMessage: String): Chat {
        userData.chats.first { it.userId == chatId }.messagesList.add(
            Message(textMessage, "Me")
        )
        userData.upload()

        val otherUserData = getUserDataFromId(chatId)
        otherUserData.chats.first { it.userId == userData.userId }.messagesList.add(
            Message(textMessage, "You")
        )
        otherUserData.upload()
        return userData.chats.first { it.userId == chatId }
    }

    override fun updateProfile(userData: UserData) {
        firebaseDataControllerImpl.updateFirebaseUserData(userData)
    }

    override fun updateSwipes(userId: String, liked: Boolean) {
        userData.swiped[userId] = liked
        userData.upload()
    }

    override fun getUserDataFromId(userId: String) = firebaseDataControllerImpl.getUserData(userId)!!

    override fun setChats() {
        matchedWithUsers.keys.forEach {
            if (!userData.chats.map { chats -> chats.userId }.contains(it.userId)) {
                userData.chats.add(Chat(userId = it.userId))
                userData.upload()

                val otherUserData = getUserDataFromId(it.userId)
                otherUserData.chats.add(Chat(userId = userData.userId))
                otherUserData.upload()
            }
        }
    }

    override fun setNecessaryData() {
        notSwipedUsers.clear()
        matchedWithUsers.clear()

        runBlocking {
            userData = firebaseDataControllerImpl.getCurrentUserId()
                ?.let { firebaseDataControllerImpl.getUserData(it) }!!

            userPhoto = firebaseDataControllerImpl.getCurrentUserId()
                ?.let { firebaseDataControllerImpl.getFirebaseUserPhoto(it) }!!

            val notShowUsers = mutableListOf<String>()
            notShowUsers.add(userData.userId)
            userData.swiped.keys.forEach {
                notShowUsers.add(it)
            }
            userData.matchedWith.forEach {
                notShowUsers.add(it)
            }
            firebaseDataControllerImpl.getSpecificUsersDataList(notShowUsers)
                .forEach {
                    Log.e("XDD", it.toString())
                    notSwipedUsers[it] = it.userId.getPhotoUri()
                }

            //TODO: test it!
            //1
//            firebaseDataControllerImpl.getUsersDataList().forEach {
//                if (!it.matchedWith.contains(userData.userId) &&
//                    it.swiped.keys.contains(userData.userId) && it.swiped.getValue(userData.userId)
//                ) {
//                    userData.matchedWith.add(it.userId)
//                    userData.upload()
//
//                    it.matchedWith.add(userData.userId)
//                    it.upload()
//                }
//            }
            userData.swiped.keys.forEach {
                val otherUser = firebaseDataControllerImpl.getUserData(it)!!
                if (!otherUser.matchedWith.contains(userData.userId) &&
                    otherUser.swiped.keys.contains(userData.userId) &&
                    otherUser.swiped.getValue(userData.userId)
                ) {
                    userData.matchedWith.add(otherUser.userId)
                    userData.upload()

                    otherUser.matchedWith.add(userData.userId)
                    otherUser.upload()
                }
            }
            //2
            userData.matchedWith.forEach {
                matchedWithUsers[firebaseDataControllerImpl.getUserData(it)!!] = it.getPhotoUri()
            }
        }
    }

    private suspend fun String.getPhotoUri(): Uri {
        return firebaseDataControllerImpl.getFirebaseUserPhoto(this)
    }

    private fun UserData.upload() {
        firebaseDataControllerImpl.updateFirebaseUserData(this)
    }
}
