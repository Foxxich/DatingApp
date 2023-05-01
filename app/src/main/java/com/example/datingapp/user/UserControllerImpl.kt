package com.example.datingapp.user

import android.net.Uri
import com.example.datingapp.firebase.FirebaseController
import com.example.datingapp.utils.FirebaseException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserControllerImpl @Inject constructor(
    private val firebaseControllerImpl: FirebaseController
) : UserController {

    private lateinit var userName: String
    private var userPhoto: Uri? = null
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

    override fun getUserData(): UserData {
        TODO("Not yet implemented")
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