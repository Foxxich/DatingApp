package com.example.datingapp.tests

import com.example.datingapp.data.UserParameters.Companion.CORRECT_EMAIL
import com.example.datingapp.data.UserParameters.Companion.CORRECT_PASSWORD
import com.example.datingapp.data.UserParameters.Companion.INCORRECT_EMAIL
import com.example.datingapp.data.UserParameters.Companion.INCORRECT_PASSWORD
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseAuthControllerImpl
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

/**
 * The class used to test backend functionality of firebase authentication
 */
@HiltAndroidTest
class FirebaseAuthControllerTest {

    private var firebaseAuthController: FirebaseAuthController = FirebaseAuthControllerImpl()

    /**
     * Function testing the case when user is registered
     */
    @Test
    fun userIsRegistered() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertNotNull(
                firebaseAuthController.isCurrentUserRegistered(
                    CORRECT_EMAIL,
                    CORRECT_PASSWORD
                )?.user
            )
        }
    }

    /**
     * Function testing the case when user is not registered, the result of the
     * execution should be thrown FirebaseAuthInvalidCredentialsException
     */
    @Test
    fun userIsNotRegistered() {
        firebaseAuthController.logout()
        Assert.assertThrows(FirebaseAuthInvalidCredentialsException::class.java) {
            runBlocking {
                firebaseAuthController.isCurrentUserRegistered(
                    CORRECT_EMAIL,
                    INCORRECT_PASSWORD
                )?.user
            }
        }
    }

    /**
     * Function testing the case when user email exists in firebase list of accounts
     */
    @Test
    fun emailExists() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertEquals(firebaseAuthController.emailExists(CORRECT_EMAIL), true)
        }
    }

    /**
     * Function testing the case when user email does not exist in firebase list of accounts
     */
    @Test
    fun emailNotExists() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertEquals(firebaseAuthController.emailExists(INCORRECT_EMAIL), false)
        }
    }

    /**
     * Function testing the case when user is not signed to the application
     */
    @Test
    fun userIsNotSigned() {
        firebaseAuthController.logout()
        runBlocking {
            Assert.assertEquals(firebaseAuthController.isCurrentUserSigned(), false)
        }
    }

}