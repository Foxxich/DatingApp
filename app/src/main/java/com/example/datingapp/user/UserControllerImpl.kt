package com.example.datingapp.user

import android.net.Uri
import com.example.datingapp.firebase.FirebaseController
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserControllerImpl @Inject constructor(
    private val firebaseControllerImpl: FirebaseController
) : UserController {

    private lateinit var userName: String
    override var userPhoto: Uri? = null
    override lateinit var userData: UserData
    override var chatId: String = ""
    override lateinit var chatUserData: UserData
    override var notSwipedUsers: MutableMap<UserData, Uri> = mutableMapOf()
    override var matchedWithUsers: MutableMap<UserData, Uri> = mutableMapOf()
    private var interests = mutableListOf<Interest>()

    override fun addUserName(userName: String) {
        this.userName = userName
    }

    override fun addUserPhoto(userPhoto: Uri) {
        this.userPhoto = userPhoto
        firebaseControllerImpl.uploadPhoto(userPhoto)
    }

    override fun addUserInterests(userInterests: List<Interest>) {
        this.interests = userInterests as MutableList<Interest>
    }

    override fun uploadToDatabase() {
        var userId = firebaseControllerImpl.getCurrentUserId()
        while (userId == null) {
            userId = firebaseControllerImpl.getCurrentUserId()
        }
        firebaseControllerImpl.uploadUserAccount(
            UserData(
                userId = userId,
                userName = this.userName,
                interests = this.interests,
            )
        )
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
            chatUserData = firebaseControllerImpl.getUserDataFromIdFirebase(userId)!!
        }
        return chatUserData
    }

    override fun setUserData() {
        runBlocking {
            userData = firebaseControllerImpl.getFirebaseUserData()!!
        }
    }

    override fun setUserPhoto() {
        runBlocking {
            userPhoto = firebaseControllerImpl.getFirebaseUserPhoto()
        }
    }

    override fun setNotSwipedUsersData() {
        notSwipedUsers.clear()
        runBlocking {
            firebaseControllerImpl.getUsersDataList().filter {
                it.userId != firebaseControllerImpl.getCurrentUserId() &&
                        !userData.swiped.keys.contains(it.userId) &&
                        !userData.matchedWith.contains(it.userId)
            }
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
            firebaseControllerImpl.getUsersDataList().forEach {
                if (!it.matchedWith.contains(userData.userId) &&
                    it.swiped.keys.contains(userData.userId) && it.swiped.getValue(userData.userId)
                ) {
                    userData.matchedWith.add(it.userId)
                    userData.upload()

                    it.matchedWith.add(userData.userId)
                    it.upload()
                }
            }
            firebaseControllerImpl.getUsersDataList().filter {
                it.userId != firebaseControllerImpl.getCurrentUserId() &&
                        userData.matchedWith.contains(it.userId)
            }
                .forEach {
                    matchedWithUsers[it] = it.userId.getPhotoUri()
                }
        }
    }

    private suspend fun String.getPhotoUri(): Uri {
        return firebaseControllerImpl.getFirebaseOtherUserPhoto(this)
    }

    private fun UserData.upload() {
        firebaseControllerImpl.updateFirebaseUserData(this)
    }
}
