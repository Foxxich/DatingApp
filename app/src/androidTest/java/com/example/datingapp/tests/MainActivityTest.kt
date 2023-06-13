package com.example.datingapp.tests

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.datingapp.activities.ChatActivity
import com.example.datingapp.activities.MainActivity
import com.example.datingapp.chat.Sorting
import com.example.datingapp.data.UserParameters.Companion.CHAT_UID
import com.example.datingapp.data.UserParameters.Companion.CORRECT_EMAIL
import com.example.datingapp.data.UserParameters.Companion.CORRECT_PASSWORD
import com.example.datingapp.data.UserParameters.Companion.CORRECT_UID
import com.example.datingapp.data.Utils
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseAuthControllerImpl
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.firebase.FirebaseDataControllerImpl
import com.example.datingapp.user.UserData
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * The class used to test UI and logic functionalities of the MainActivity
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val firebaseAuthController: FirebaseAuthController = FirebaseAuthControllerImpl()
    private val firebaseDataController: FirebaseDataController = FirebaseDataControllerImpl()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>().apply {
        runBlocking {
            firebaseAuthController.isCurrentUserRegistered(CORRECT_EMAIL, CORRECT_PASSWORD)
        }
    }

    /**
     * The aim of the test is to open every single screen and check if the data was loaded
     * correctly, without any missing information
     */
    @Test
    fun existingUI() {
        clickAndCheckExistence("profile", "profile_screen_tag")
        clickAndCheckExistence("chat", "chat_screen_tag")
        clickAndCheckExistence("home", "home_screen_tag")
        firebaseAuthController.logout()
    }

    /**
     * Additional function to perform the common moves between different screens
     */
    private fun clickAndCheckExistence(screenTag: String, screenElementTag: String) {
        composeTestRule.onNodeWithTag(screenTag, true).performClick()
        composeTestRule.onNodeWithTag(screenElementTag).assertExists()
        Thread.sleep(3000)
    }

    /**
     * The aim is to open the chat and to show the new messages on the bottom of the screen
     */
    @Test
    fun openDescendingChat() {
        openChat(Sorting.DESCENDING)
    }

    /**
     * The aim is to open the chat and to show the new messages on the start of the screen
     */
    @Test
    fun openAscendingChat() {
        openChat(Sorting.ASCENDING)
    }

    /**
     * Function to open chat and check if the the correct sorting was used
     * to show the messages
     */
    private fun openChat(sorting: Sorting) {
        // Choose the way how messages will be sorted and shown
        composeTestRule.activity.userController.sorting = sorting
        // Start monitoring the activities
        val activityMonitor = Utils.getActivityMonitor(ChatActivity::class.java.name)
        Utils.add(activityMonitor)
        // Perform click
        composeTestRule.onNodeWithTag("chat", true).performClick()
        composeTestRule.onNodeWithTag("open_chat_tag").performClick()
        Thread.sleep(5000L)
        // Assert the given values
        Assert.assertEquals(activityMonitor.lastActivity::class, ChatActivity::class)
        Assert.assertEquals(
            (activityMonitor.lastActivity as ChatActivity).sorting,
            sorting.name
        )
        Utils.remove(activityMonitor)
    }

    /**
     * The aim is to do swipe on left, check if the data was updated and the fields was
     * equal to false (it means the profile was disliked)
     */
    @Test
    fun swipeLeft() {
        swipe(-1200f)
        swipe(-1200f)
        swipeDataAssertion(3, false)
    }

    /**
     * The aim is to do swipe on left, check if the data was updated and the fields was
     * equal to true (it means the profile was liked)
     */
    @Test
    fun swipeRight() {
        swipe(800f)
        swipeDataAssertion(2, true)
    }

    /**
     * Function with assertion logic of checking up the correctness
     * of the userData from firebase
     */
    private fun swipeDataAssertion(expected_size: Int, expected_like: Boolean) {
        val userData = firebaseDataController.getFirebaseUserData(CORRECT_UID)!!
        val size = userData.getSize()
        val liked = userData.getLiked()
        userData.removeFromFirebase()
        Assert.assertEquals(expected_size, size)
        Assert.assertEquals(expected_like, liked)
    }

    /**
     * Does physical swipe
     */
    private fun swipe(x: Float) {
        composeTestRule.onNodeWithTag("twyper_tag").performTouchInput {
            swipe(
                start = Offset.Zero,
                end = Offset(x, 0f),
                durationMillis = 1000L
            )
        }
        composeTestRule.onNodeWithTag("twyper_tag").assertExists()
        Thread.sleep(4000L)
    }

    /**
     * Gets the size of list containing the swipes
     */
    private fun UserData.getSize() = this.swiped.size

    /**
     * Gets the number of likes from the swipes
     */
    private fun UserData.getLiked() =
        this.swiped.filterNot { it.key == CHAT_UID }.map { it.value }.first()

    /**
     * Removes the test data from firebase
     */
    private fun UserData.removeFromFirebase() {
        this.swiped = this.swiped.filter {
            it.key == CHAT_UID
        } as MutableMap<String, Boolean>
        firebaseDataController.setFirebaseUserData(this)
    }

}