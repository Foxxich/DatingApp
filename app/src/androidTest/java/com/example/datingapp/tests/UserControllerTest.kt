package com.example.datingapp.tests

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.datingapp.R
import com.example.datingapp.chat.Chat
import com.example.datingapp.chat.Message
import com.example.datingapp.data.UserParameters
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.firebase.FirebaseDataControllerImpl
import com.example.datingapp.user.UserController
import com.example.datingapp.user.UserControllerImpl
import com.example.datingapp.user.UserData
import com.example.datingapp.user.UserDataCollection
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * The class used to test backend functionality of the user controller
 */
@HiltAndroidTest
class UserControllerTest {

    private var firebaseDataController: FirebaseDataController = FirebaseDataControllerImpl()
    private var userController: UserController = UserControllerImpl(firebaseDataController)
    private lateinit var firebaseUserData: UserData
    private lateinit var firebaseUri: Uri
    private val testUserData = UserParameters.TEST_USER_DATA

    /**
     * Function used to set the basic data of the user
     */
    @Before
    fun prepareTestData() {
        testUserData.matchedWith.add("user1")

        testUserData.swiped["user1"] = true
        testUserData.swiped["user4"] = false
        testUserData.swiped["user5"] = false

        testUserData.matchedWith.add("user1")

        testUserData.chats.add(Chat("user1", mutableListOf(Message("text1text1", "Me"))))

        val context: Context = getApplicationContext()
        userController.uploadToDatabase(
            UserDataCollection(
                userData = testUserData,
                userPhoto = Uri.parse("android.resource://${context.packageName}/${R.raw.user_image}")
            )
        )
        firebaseUserData = userController.getUserDataFromId(testUserData.userId)!!
        firebaseDataController.deleteData(testUserData.userId)
        runBlocking {
            firebaseUri = firebaseDataController.getFirebaseUserPhoto(testUserData.userId)
        }
    }

    /**
     * Function which checks the data is not nullable
     */
    @Test
    fun dataNonNullable() {
        Assert.assertNotNull(firebaseUserData)
        Assert.assertNotNull(firebaseUri)
    }

    /**
     * Function which checks the data is same as the testData
     */
    @Test
    fun dataIsEqual() {
        val context: Context = getApplicationContext()
        Assert.assertEquals(
            firebaseUri.userInfo,
            Uri.parse("android.resource://${context.packageName}/${R.raw.user_image}").userInfo
        )
        Assert.assertEquals(firebaseUserData, testUserData)
    }

    /**
     * Function which checks is the data from firebase contains specific chats
     */
    @Test
    fun chatsWereAdded() {
        Assert.assertNotNull(firebaseUserData.chats)
    }

}