package com.example.datingapp

import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseAuthControllerImpl
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class FirebaseAuthControllerTest {

    private var firebaseAuthController: FirebaseAuthController = FirebaseAuthControllerImpl()

    @Test
    fun userIsRegistered() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertNotNull(
                firebaseAuthController.isCurrentUserRegistered(
                    correctEmail,
                    correctPassword
                )?.user
            )
        }
    }

    @Test
    fun userIsNotRegistered() {
        firebaseAuthController.logout()
        Assert.assertThrows(FirebaseAuthInvalidCredentialsException::class.java) {
            runBlocking {
                firebaseAuthController.isCurrentUserRegistered(
                    correctEmail,
                    incorrectPassword
                )?.user
            }
        }
    }

    @Test
    fun emailExists() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertEquals(firebaseAuthController.emailExists(correctEmail), true)
        }
    }

    @Test
    fun emailNotExists() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertEquals(firebaseAuthController.emailExists(incorrectEmail), false)
        }
    }

    @Test
    fun userIsNotSigned() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertEquals(firebaseAuthController.isCurrentUserSigned(), false)
        }
    }

    companion object {
        const val correctEmail = "test1@gmail.com"
        const val correctPassword = "123456"
        const val incorrectEmail = "random@gmail.com"
        const val incorrectPassword = "xdxdxd"
    }
}