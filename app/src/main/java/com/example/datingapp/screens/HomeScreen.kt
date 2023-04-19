package com.example.datingapp.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.datingapp.compose.BottomBar
import com.example.datingapp.compose.ProfilePreview
import com.example.datingapp.firebase.FirebaseController
import com.example.datingapp.ui.theme.backgroundColor

class HomeScreen: ScreenController {

    @Composable
    override fun Prepare(
        navController: NavHostController,
        firebaseController: FirebaseController,
        context: Context
    ) {
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