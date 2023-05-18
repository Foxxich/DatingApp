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

    override var userPhoto: Uri? = null
    override lateinit var userData: UserData
    override var notSwipedUsersUri: MutableMap<UserData, Uri> = mutableMapOf()
    override var matchedWithUsersUri: MutableMap<UserData, Uri> = mutableMapOf()
    override lateinit var userCollection: UserCollection
    private val observers = mutableListOf<UserDataObserver>()

    override fun uploadToDatabase(userName: String, interests: List<Interest>) {
        val userId = firebaseDataControllerImpl.getCurrentUserId()!!
        firebaseDataControllerImpl.setFirebaseUserData(
            UserData(
                userId = userId,
                userName = userName,
                interests = interests,
            )
        )
        firebaseDataControllerImpl.setUserProfileSetUp(userId)
        userPhoto?.let { firebaseDataControllerImpl.setFirebasePhoto(it) }
    }

    override fun updateChats(textMessage: String, chatId: String): Chat {
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
        firebaseDataControllerImpl.setFirebaseUserData(userData)
    }

    override fun updateSwipes(userId: String, liked: Boolean) {
        userData.swiped[userId] = liked
        userData.upload()
    }

    override fun getUserDataFromId(userId: String) =
        firebaseDataControllerImpl.getFirebaseUserData(userId)!!

    override fun setChats() {
        matchedWithUsersUri.keys.forEach {
            if (!userData.chats.map { chats -> chats.userId }.contains(it.userId)) {
                userData.chats.add(Chat(userId = it.userId))
                userData.upload()

                val otherUserData = getUserDataFromId(it.userId)
                otherUserData.chats.add(Chat(userId = userData.userId))
                otherUserData.upload()
            }
        }
    }

    override fun setMyObject(myObject: UserData) {
        this.userData = myObject
        for (observer in observers) {
            observer.dataChanged(myObject)
        }
    }

    override fun dataChanged(userData: UserData) {
        setMyObject(userData)
    }

    override fun setNecessaryData() {
        notSwipedUsersUri.clear()
        matchedWithUsersUri.clear()

        runBlocking {
            userData = firebaseDataControllerImpl.getCurrentUserId()
                ?.let { firebaseDataControllerImpl.getFirebaseUserData(it) }!!

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
            firebaseDataControllerImpl.getNotInUsersDataList(notShowUsers)
                .forEach {
                    notSwipedUsersUri[it] = it.userId.getPhotoUri()
                }

            userData.swiped.keys.forEach {
                val otherUser = firebaseDataControllerImpl.getFirebaseUserData(it)!!
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
            userData.matchedWith.forEach {
                matchedWithUsersUri[firebaseDataControllerImpl.getFirebaseUserData(it)!!] =
                    it.getPhotoUri()
            }
        }
    }

    private suspend fun String.getPhotoUri(): Uri {
        return firebaseDataControllerImpl.getFirebaseUserPhoto(this)
    }

    private fun UserData.upload() {
        firebaseDataControllerImpl.setFirebaseUserData(this)
    }
}
