package com.example.datingapp.user

import android.net.Uri
import com.example.datingapp.chat.Chat
import com.example.datingapp.chat.Message
import com.example.datingapp.firebase.FirebaseDataController
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserControllerImpl @Inject constructor(
    private val firebaseDataControllerImpl: FirebaseDataController
) : UserController {

    override lateinit var userDataCollection: UserDataCollection
    private val observers = mutableListOf<UserDataObserver>()

    override fun uploadToDatabase(userName: String, interests: List<Interest>, imageUri: Uri) {
        val userId = firebaseDataControllerImpl.getCurrentUserId()!!
        val userData = UserData(
            userId = userId,
            userName = userName,
            interests = interests,
        )
        firebaseDataControllerImpl.setFirebaseUserData(
            userData
        )
        userDataCollection = UserDataCollection(
            userData = userData,
            userPhoto = imageUri
        )
        firebaseDataControllerImpl.setUserProfileSetUp(userId)
        userDataCollection.userPhoto?.let { firebaseDataControllerImpl.setFirebasePhoto(it) }
    }

    override fun uploadToDatabase(userDataCollection: UserDataCollection) {
        val userId = userDataCollection.userData.userId
        val userData = userDataCollection.userData
        firebaseDataControllerImpl.setFirebaseUserData(
            userData
        )
        firebaseDataControllerImpl.setUserProfileSetUp(userId)
        userDataCollection.userPhoto?.let { firebaseDataControllerImpl.setFirebasePhoto(it) }
    }

    override fun updateChats(textMessage: String, chatId: String): Chat {
        userDataCollection.userData.chats.first { it.userId == chatId }.messagesList.add(
            Message(textMessage, "Me")
        )
        userDataCollection.userData.upload()

        val otherUserData = getUserDataFromId(chatId)
        otherUserData.chats.first { it.userId == userDataCollection.userData.userId }.messagesList.add(
            Message(textMessage, "You")
        )
        otherUserData.upload()

        return userDataCollection.userData.chats.first { it.userId == chatId }
    }

    override fun updateSwipes(userId: String, liked: Boolean) {
        userDataCollection.userData.swiped[userId] = liked
        userDataCollection.userData.upload()
    }

    override fun getUserDataFromId(userId: String) =
        firebaseDataControllerImpl.getFirebaseUserData(userId)!!

    override fun setChats() {
        userDataCollection.matchedWithUsersUri.keys.forEach {
            if (!userDataCollection.userData.chats.map { chats -> chats.userId }
                    .contains(it.userId)) {
                userDataCollection.userData.chats.add(Chat(userId = it.userId))
                userDataCollection.userData.upload()

                val otherUserData = getUserDataFromId(it.userId)
                otherUserData.chats.add(Chat(userId = userDataCollection.userData.userId))
                otherUserData.upload()
            }
        }
    }

    override fun dataChanged(userData: UserData) {
        this.userDataCollection.userData = userData
        for (observer in observers) {
            observer.dataChanged(userData)
        }
    }

    override fun setNecessaryData() {
        runBlocking {

            userDataCollection = UserDataCollection(
                userData = firebaseDataControllerImpl.getCurrentUserId()
                    ?.let { firebaseDataControllerImpl.getFirebaseUserData(it) }!!,
                userPhoto = firebaseDataControllerImpl.getCurrentUserId()
                    ?.let { firebaseDataControllerImpl.getFirebaseUserPhoto(it) }
            )

            userDataCollection.notSwipedUsersUri.clear()
            userDataCollection.matchedWithUsersUri.clear()

            val notShowUsers = mutableListOf<String>()
            notShowUsers.add(userDataCollection.userData.userId)
            userDataCollection.userData.swiped.keys.forEach {
                notShowUsers.add(it)
            }
            userDataCollection.userData.matchedWith.forEach {
                notShowUsers.add(it)
            }
            firebaseDataControllerImpl.getNotInUsersDataList(notShowUsers)
                .forEach {
                    userDataCollection.notSwipedUsersUri[it] = it.userId.getPhotoUri()
                }

            userDataCollection.userData.swiped.keys.forEach {
                val otherUser = firebaseDataControllerImpl.getFirebaseUserData(it)!!
                if (!otherUser.matchedWith.contains(userDataCollection.userData.userId) &&
                    otherUser.swiped.keys.contains(userDataCollection.userData.userId) &&
                    otherUser.swiped.getValue(userDataCollection.userData.userId)
                ) {
                    userDataCollection.userData.matchedWith.add(otherUser.userId)
                    userDataCollection.userData.upload()

                    otherUser.matchedWith.add(userDataCollection.userData.userId)
                    otherUser.upload()
                }
            }
            userDataCollection.userData.matchedWith.forEach {
                userDataCollection.matchedWithUsersUri[firebaseDataControllerImpl.getFirebaseUserData(
                    it
                )!!] =
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
