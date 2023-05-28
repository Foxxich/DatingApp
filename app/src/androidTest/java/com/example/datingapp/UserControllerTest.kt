package com.example.datingapp

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.datingapp.chat.Chat
import com.example.datingapp.chat.Message
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.firebase.FirebaseDataControllerImpl
import com.example.datingapp.user.UserController
import com.example.datingapp.user.UserControllerImpl
import com.example.datingapp.user.UserData
import com.example.datingapp.user.UserDataCollection
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test


@HiltAndroidTest
class UserControllerTest {

    private var firebaseDataController: FirebaseDataController = FirebaseDataControllerImpl()
    private var userController: UserController = UserControllerImpl(firebaseDataController)
    private var testUserData = UserData(
        userId = "testId",
        userName = "testName",
        quantityUserMatchedWith = 0
    )
    private lateinit var firebaseUserData: UserData
    private lateinit var firebaseUri: Uri

    @Before
    fun prepareTestData() {
        testUserData.matchedWith.add("user1")

        testUserData.swiped["user1"] = true
        testUserData.swiped["user4"] = false
        testUserData.swiped["user5"] = false

        testUserData.matchedWith.add("user1")

        testUserData.chats.add(Chat("user1", mutableListOf(Message("text1text1", "Me"))))

        val context: Context = getApplicationContext()
        userController.uploadToDatabase(UserDataCollection(userData = testUserData, userPhoto = Uri.parse("android.resource://${context.packageName}/${R.raw.user_image}")) )
        firebaseUserData = userController.getUserDataFromId(testUserData.userId)!!
        runBlocking {
            firebaseUri = firebaseDataController.getFirebaseUserPhoto(testUserData.userId)
        }
    }

    @Test
    fun dataNonNullable() {
        Assert.assertNotNull(firebaseUserData)
        Assert.assertNotNull(firebaseUri)
    }

    @Test
    fun dataIsEqual() {
        val context: Context = getApplicationContext()
        Assert.assertEquals(firebaseUri.userInfo, Uri.parse("android.resource://${context.packageName}/${R.raw.user_image}").userInfo)
        Assert.assertEquals(firebaseUserData, testUserData)
    }

    @Test
    fun chatsWereAdded() {
        Assert.assertNotNull(userController.getUserDataFromId(testUserData.userId)!!.chats)
    }

    @Test
    fun chatsWereUpdated() {
        Assert.assertNotNull(userController.getUserDataFromId(testUserData.userId)!!.chats.filter { it.userId == "user1" })
    }

    @Test
    fun swipesWereAdded() {
        Assert.assertNotNull(userController.getUserDataFromId(testUserData.userId)!!.swiped["user1"])
    }

    @After
    fun deleteData() {
        firebaseDataController.deleteData(testUserData.userId)
    }
}