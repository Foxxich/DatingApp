package com.example.datingapp

import android.app.Instrumentation
import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.datingapp.activities.SetUpActivity
import com.example.datingapp.activities.SignActivity
import com.example.datingapp.activities.StartActivity
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseAuthControllerImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<StartActivity>()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val firebaseAuthController: FirebaseAuthController = FirebaseAuthControllerImpl()

    @Test
    fun newUserLogin() {
        val activityMonitor =
            Instrumentation.ActivityMonitor(SignActivity::class.java.name, null, false)
        InstrumentationRegistry.getInstrumentation().addMonitor(activityMonitor)
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.heart_image_description))
            .performClick()
        Thread.sleep(2000)
        assertEquals(activityMonitor.lastActivity::class, SignActivity::class)
        InstrumentationRegistry.getInstrumentation().removeMonitor(activityMonitor)
    }

    @Test
    fun oldUserLogin() {
        runBlocking {
            firebaseAuthController.isCurrentUserRegistered("test1@gmail.com", "123456")
        }
        val activityMonitor =
            Instrumentation.ActivityMonitor(SetUpActivity::class.java.name, null, false)
        InstrumentationRegistry.getInstrumentation().addMonitor(activityMonitor)
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.heart_image_description))
            .performClick()
        Thread.sleep(2000)
        assertEquals(activityMonitor.lastActivity::class, SetUpActivity::class)
        InstrumentationRegistry.getInstrumentation().removeMonitor(activityMonitor)
        firebaseAuthController.logout()
    }

}