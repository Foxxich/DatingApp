package com.example.datingapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.datingapp.activities.MainActivity
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseAuthControllerImpl
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val firebaseAuthController: FirebaseAuthController = FirebaseAuthControllerImpl()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>().apply {
        runBlocking {
            firebaseAuthController.isCurrentUserRegistered(email, password)
        }
    }

    @Test
    fun existingUI() {
        composeTestRule.onNodeWithTag("profile_tag").assertExists()
        composeTestRule.onNodeWithTag("home", true).assertExists()
        firebaseAuthController.logout()
    }

    companion object {
        private const val email = "user7@gmail.com"
        private const val password = "123456"
    }

}