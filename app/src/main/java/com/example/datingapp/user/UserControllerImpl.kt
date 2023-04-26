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
    private lateinit var userPhoto: Uri
    private var interests = mutableListOf<Interest>()

    override fun addUserName(userName: String) {
        this.userName = userName
    }

    override fun addUserPhoto(userPhoto: Uri) {
        this.userPhoto = userPhoto
    }

    override fun addUserInterests(userInterests: List<Interest>) {
        this.interests = userInterests as MutableList<Interest>
    }

    override fun getUserData(): UserData {
        TODO("Not yet implemented")
    }

    override fun uploadToDatabase() {
        val userId = firebaseControllerImpl.getUserId() ?: throw FirebaseException("UID do not exist!")
        firebaseControllerImpl.uploadUserAccount(
            UserData(
                userId = userId,
                userName = this.userName,
                interests = this.interests,
            )
        )
    }
}