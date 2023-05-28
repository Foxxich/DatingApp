package com.example.datingapp

import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseAuthControllerImpl
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class FirebaseAuthControllerTests {

    private var firebaseAuthController: FirebaseAuthController = FirebaseAuthControllerImpl()

    @Test
    fun userIsRegistered() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertNotNull(firebaseAuthController.isCurrentUserRegistered("test1@gmail.com", "123456")?.user)
        }
    }

    @Test
    fun userIsNotRegistered() {
        firebaseAuthController.logout()
        Assert.assertThrows(FirebaseAuthInvalidCredentialsException::class.java) {
            runBlocking {
                firebaseAuthController.isCurrentUserRegistered(
                    "test1@gmail.com",
                    "111111"
                )?.user
            }
        }
    }

    @Test
    fun emailExists() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertEquals(firebaseAuthController.emailExists("test1@gmail.com"), true)
        }
    }

    @Test
    fun emailNotExists() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertEquals(firebaseAuthController.emailExists("random@gmail.com"), false)
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