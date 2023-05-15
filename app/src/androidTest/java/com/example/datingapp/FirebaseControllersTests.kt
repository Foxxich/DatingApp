package com.example.datingapp

import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseAuthControllerImpl
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class FirebaseControllersTests {

    private var firebaseAuthController: FirebaseAuthController = FirebaseAuthControllerImpl()

    @Test
    fun userIsRegistered() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertNotNull(firebaseAuthController.isCurrentUserRegistered("test6@gmail.com", "123456"))
        }
    }

    @Test
    fun userIsNotSigned() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertEquals(firebaseAuthController.isCurrentUserSigned(), false)
        }
    }
}