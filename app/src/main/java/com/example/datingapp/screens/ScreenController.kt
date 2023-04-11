package com.example.datingapp.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface ScreenController {

    @Composable
    fun Prepare(navController: NavHostController)

}