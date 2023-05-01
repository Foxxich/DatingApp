package com.example.datingapp.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.datingapp.compose.BottomBar
import com.example.datingapp.compose.ProfilePreview
import com.example.datingapp.firebase.FirebaseController
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.user.UserController
import kotlinx.coroutines.launch

class HomeScreen : ScreenController {

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    override fun Prepare(
        navController: NavHostController,
        firebaseController: FirebaseController,
        context: Context,
        userControllerImpl: UserController
    ) {
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch {
            userControllerImpl.getUserData()
            userControllerImpl.getUserPhoto()
        }
        Scaffold(bottomBar = { BottomBar().Prepare(navController) }
        ) {
            Box(
                modifier = Modifier
                    .background(backgroundColor)
                    .fillMaxSize()
            ) {
                ProfilePreview()
            }
        }
    }

}