package com.example.datingapp.user

import android.net.Uri
import com.example.datingapp.firebase.FirebaseDataController
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserControllerImpl @Inject constructor(
    private val firebaseDataControllerImpl: FirebaseDataController
) : UserController {

    private lateinit var userName: String
    override var userPhoto: Uri? = null
    override lateinit var userData: UserData
    override var chatId: String = ""
    override lateinit var chatUserData: UserData
    override var notSwipedUsers: MutableMap<UserData, Uri> = mutableMapOf()
    override var matchedWithUsers: MutableMap<UserData, Uri> = mutableMapOf()

    override fun uploadToDatabase(userName: String, interests: List<Interest>) {
        var userId = firebaseDataControllerImpl.getCurrentUserId()
        while (userId == null) {
            userId = firebaseDataControllerImpl.getCurrentUserId()
        }
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

    override fun updateSwipes(userId: String, b: Boolean) {
        userData.swiped[userId] = b
        userData.upload()
    }

    override fun getUserDataFromId(userId: String): UserData {
        runBlocking {
            chatUserData = firebaseDataControllerImpl.getUserDataFromIdFirebase(userId)!!
        }
        return chatUserData
    }

    override fun setUserData() {
        runBlocking {
            userData = firebaseDataControllerImpl.getFirebaseUserData()!!
        }
    }

    override fun setUserPhoto() {
        runBlocking {
            userPhoto = firebaseDataControllerImpl.getFirebaseUserPhoto()
        }
    }

    override fun setNotSwipedUsersData() {
        notSwipedUsers.clear()
        runBlocking {
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
                    notSwipedUsers[it] = it.userId.getPhotoUri()
                }
        }
    }

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

    override fun setMatchedWithUsersData() {
        matchedWithUsers.clear()
        runBlocking {
            firebaseDataControllerImpl.getUsersDataList().forEach {
                if (!it.matchedWith.contains(userData.userId) &&
                    it.swiped.keys.contains(userData.userId) && it.swiped.getValue(userData.userId)
                ) {
                    userData.matchedWith.add(it.userId)
                    userData.upload()

                    it.matchedWith.add(userData.userId)
                    it.upload()
                }
            }
            userData.matchedWith.forEach {
                matchedWithUsers[firebaseDataControllerImpl.getUserDataFromIdFirebase(it)!!] = it.getPhotoUri()
            }
        }
    }

    private suspend fun String.getPhotoUri(): Uri {
        return firebaseDataControllerImpl.getFirebaseOtherUserPhoto(this)
    }

    private fun UserData.upload() {
        firebaseDataControllerImpl.updateFirebaseUserData(this)
    }
}
