package com.example.datingapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.datingapp.firebase.FirebaseController
import com.example.datingapp.screens.ChatScreen
import com.example.datingapp.screens.HomeScreen
import com.example.datingapp.screens.ProfileScreen
import com.example.datingapp.user.UserController

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    userControllerImpl: UserController,
    firebaseController: FirebaseController,
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(userControllerImpl)
        }
        composable(route = BottomBarScreen.Report.route) {
            ChatScreen(context, userControllerImpl)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(context, userControllerImpl, firebaseController)
        }
    }
}