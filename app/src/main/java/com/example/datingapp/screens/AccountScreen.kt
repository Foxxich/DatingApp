package com.example.datingapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.datingapp.compose.BottomBar
import com.example.datingapp.ui.theme.backgroundColor

class AccountScreen: ScreenController {

    @Composable
    override fun Prepare(navController: NavHostController) {
        Scaffold(bottomBar = { BottomBar().Prepare(navController) }
        ) {
            Box(
                modifier = Modifier
                    .background(backgroundColor)
                    .fillMaxSize()
            ) {
                Text("Account Screen")
            }
        }
    }

}