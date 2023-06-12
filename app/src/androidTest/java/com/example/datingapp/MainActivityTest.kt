package com.example.datingapp

import android.app.Instrumentation
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.datingapp.activities.ChatActivity
import com.example.datingapp.activities.MainActivity
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseAuthControllerImpl
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.firebase.FirebaseDataControllerImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val firebaseAuthController: FirebaseAuthController = FirebaseAuthControllerImpl()
    private val firebaseDataController: FirebaseDataController = FirebaseDataControllerImpl()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>().apply {
        runBlocking {
            firebaseAuthController.isCurrentUserRegistered(email, password)
        }
    }

    @Test
    fun existingUI() {
        composeTestRule.onNodeWithTag("profile_tag").assertExists()
        composeTestRule.onNodeWithTag("profile", true).performClick()
        composeTestRule.onNodeWithTag("profile_screen_tag").assertExists()
        Thread.sleep(3000)
        composeTestRule.onNodeWithTag("chat", true).performClick()
        composeTestRule.onNodeWithTag("chat_screen_tag").assertExists()
        Thread.sleep(3000)
        composeTestRule.onNodeWithTag("home", true).performClick()
        composeTestRule.onNodeWithTag("home_screen_tag").assertExists()
        Thread.sleep(3000)
        firebaseAuthController.logout()
    }

    @Test
    fun openChatActivity() {
        val activityMonitor =
            Instrumentation.ActivityMonitor(ChatActivity::class.java.name, null, false)
        InstrumentationRegistry.getInstrumentation().addMonitor(activityMonitor)
        composeTestRule.onNodeWithTag("chat", true).performClick()
        composeTestRule.onNodeWithTag("open_chat_tag").performClick()
        Thread.sleep(2000)
        Assert.assertEquals(activityMonitor.lastActivity::class, ChatActivity::class)
        InstrumentationRegistry.getInstrumentation().removeMonitor(activityMonitor)
    }

    @Test
    fun swipeLeft() {
        composeTestRule.onNodeWithTag("twyper_tag").performTouchInput {
            swipe(
                start = Offset.Zero,
                end = Offset(-800f, 0f),
                durationMillis = 1000
            )
        }
        composeTestRule.onNodeWithTag("twyper_tag").assertExists()
        Thread.sleep(4000L)

        val userData = firebaseDataController.getFirebaseUserData("4GiuevU4c3dJXJaL2GzrY6aQEBn2")!!
        val size = userData.swiped.size
        userData.swiped = userData.swiped.filter {
            it.key == "gl0FJ4Sv3fbHYpoTCa8U8UHMrxQ2"
        } as MutableMap<String, Boolean>
        firebaseDataController.setFirebaseUserData(userData)
        Assert.assertEquals(2, size)
    }

    @Test
    fun swipeRight() {
        composeTestRule.onNodeWithTag("twyper_tag").performTouchInput {
            swipe(
                start = Offset.Zero,
                end = Offset(800f, 0f),
                durationMillis = 1000
            )
        }
        composeTestRule.onNodeWithTag("twyper_tag").assertExists()
        Thread.sleep(4000L)

        val userData = firebaseDataController.getFirebaseUserData("4GiuevU4c3dJXJaL2GzrY6aQEBn2")!!
        val size = userData.swiped.size
        userData.swiped = userData.swiped.filter {
            it.key == "gl0FJ4Sv3fbHYpoTCa8U8UHMrxQ2"
        } as MutableMap<String, Boolean>
        firebaseDataController.setFirebaseUserData(userData)

        Assert.assertEquals(2, size)
    }

    companion object {
        private const val email = "user7@gmail.com"
        private const val password = "123456"
    }
}