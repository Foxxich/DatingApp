package com.example.datingapp.tests

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.datingapp.activities.MainActivity
import com.example.datingapp.activities.SignActivity
import com.example.datingapp.data.UserParameters.Companion.CORRECT_EMAIL
import com.example.datingapp.data.UserParameters.Companion.CORRECT_PASSWORD
import com.example.datingapp.data.Utils
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
 * The class used to test UI and logic functionalities of the SignActivity
 */
@RunWith(AndroidJUnit4::class)
class SignActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SignActivity>()

    /*
     * Function used to check if the there are input fields for email and password
     */
    @Test
    fun correctTextInput() {
        composeTestRule.onNodeWithTag("email_tag").performTextInput(CORRECT_EMAIL)
        composeTestRule.onNodeWithTag("email_tag").assert(hasText(CORRECT_EMAIL))
        composeTestRule.onNodeWithTag("password_tag").assertExists()
    }

    /*
     * Function used to check if the application will allow logging to user
     * with correct email and password which exist in database
     */
    @Test
    fun mainActivityOpens() {
        composeTestRule.onNodeWithTag("email_tag").performTextInput(CORRECT_EMAIL)
        composeTestRule.onNodeWithTag("password_tag").performTextInput(CORRECT_PASSWORD)
        val activityMonitor = Utils.getActivityMonitor(MainActivity::class.java.name)
        Utils.add(activityMonitor)
        composeTestRule.onNodeWithTag("login_button_tag").performClick()
        Thread.sleep(10000L)
        Assert.assertEquals(activityMonitor.lastActivity::class, MainActivity::class)
        Utils.remove(activityMonitor)
    }

}