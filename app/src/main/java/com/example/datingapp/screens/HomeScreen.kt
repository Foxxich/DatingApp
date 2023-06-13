package com.example.datingapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.user.UserController

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(userControllerImpl: UserController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_tag")
            .background(backgroundColor)
    ) {
        ProfilePreview(userControllerImpl)
    }
}