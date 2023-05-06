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

    override fun getUserData() {
        runBlocking {
            userData = firebaseControllerImpl.getFirebaseUserData()!!
        }
    }

    override fun getUserPhoto() {
        runBlocking {
            userPhoto = firebaseControllerImpl.getFirebaseUserPhoto()
        }
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
}