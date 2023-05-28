package com.example.datingapp

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.firebase.FirebaseDataControllerImpl
import com.example.datingapp.user.UserController
import com.example.datingapp.user.UserControllerImpl
import com.example.datingapp.user.UserData
import dagger.hilt.android.testing.HiltAndroidTest
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

    @Before
    fun prepareTestData() {
        testUserData.matchedWith.add("user1")
        testUserData.matchedWith.add("user2")
        testUserData.matchedWith.add("user3")

        testUserData.swiped["user1"] = true
        testUserData.swiped["user2"] = true
        testUserData.swiped["user3"] = true
        testUserData.swiped["user4"] = false
        testUserData.swiped["user5"] = true
    }

    @Test
    fun uploadTestData() {
        userController.updateProfile(testUserData)
    }

    @Test
    fun testDataWasAdded() {
        val context: Context = getApplicationContext()
        val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.user_image}")
        val firebaseUserData = userController.getUserDataFromId(testUserData.userId)
        firebaseDataController.deleteData(testUserData.userId)
        Thread.sleep(2000)
        Assert.assertNotNull(firebaseUserData)
        Assert.assertEquals(firebaseUserData, testUserData)
    }
}