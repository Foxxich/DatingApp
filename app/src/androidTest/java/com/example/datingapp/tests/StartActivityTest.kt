package com.example.datingapp.tests

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.datingapp.R
import com.example.datingapp.activities.SetUpActivity
import com.example.datingapp.activities.SignActivity
import com.example.datingapp.activities.StartActivity
import com.example.datingapp.data.UserParameters.Companion.TEST_EMAIL
import com.example.datingapp.data.Utils
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseAuthControllerImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The class used to test UI and logic functionalities of the StartActivity
 */
@RunWith(AndroidJUnit4::class)
class StartActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<StartActivity>()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val firebaseAuthController: FirebaseAuthController = FirebaseAuthControllerImpl()

    /**
     * Function use to show the correctness of UI schema for the new user
     */
    @Test
    fun newUserLogin() {
        firebaseAuthController.logout()
        val activityMonitor = Utils.getActivityMonitor(SignActivity::class.java.name)
        Utils.add(activityMonitor)
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.heart_image_description))
            .performClick()
        Thread.sleep(2000L)
        assertEquals(activityMonitor.lastActivity::class, SignActivity::class)
        Utils.remove(activityMonitor)
    }

    /**
     * Function use to show the correctness of UI schema for the existing user
     */
    @Test
    fun existingUserLogin() {
        firebaseAuthController.logout()
        runBlocking {
            firebaseAuthController.isCurrentUserRegistered(TEST_EMAIL, "123456")
        }
        val activityMonitor = Utils.getActivityMonitor(SetUpActivity::class.java.name)
        Utils.add(activityMonitor)
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.heart_image_description))
            .performClick()
        Thread.sleep(2000L)
        assertEquals(activityMonitor.lastActivity::class, SetUpActivity::class)
        Utils.remove(activityMonitor)
        firebaseAuthController.logout()
    }

}