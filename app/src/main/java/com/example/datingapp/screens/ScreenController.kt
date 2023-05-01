package com.example.datingapp.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.datingapp.firebase.FirebaseController
import com.example.datingapp.user.UserController

interface ScreenController {

    @Composable
    fun Prepare(
        navController: NavHostController,
        firebaseController: FirebaseController,
        context: Context,
        userControllerImpl: UserController
    )

}