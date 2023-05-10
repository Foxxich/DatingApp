package com.example.datingapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.screens.ChatScreen
import com.example.datingapp.screens.HomeScreen
import com.example.datingapp.screens.ProfileScreen
import com.example.datingapp.user.UserController

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    userControllerImpl: UserController,
    firebaseDataController: FirebaseDataController,
    context: Context,
    firebaseAuthController: FirebaseAuthController
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(userControllerImpl)
        }
        composable(route = BottomBarScreen.Chat.route) {
            ChatScreen(context, userControllerImpl)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(context, userControllerImpl, firebaseDataController, firebaseAuthController)
        }
    }
}