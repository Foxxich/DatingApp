package com.example.datingapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datingapp.firebase.FirebaseController
import com.example.datingapp.screens.AccountScreen
import com.example.datingapp.screens.HomeScreen
import com.example.datingapp.screens.MessagesScreen
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.user.UserController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userControllerImpl: UserController

    @Inject
    lateinit var firebaseController: FirebaseController

    @ApplicationContext
    @Inject
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavigationView(firebaseController, context)
                }
            }
        }
    }
}

@Composable
fun NavigationView(firebaseController: FirebaseController, context: Context) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen().Prepare(navController, firebaseController, context)
        }
        composable("messages") {
            MessagesScreen().Prepare(navController, firebaseController, context)
        }
        composable("account") {
            AccountScreen().Prepare(navController, firebaseController, context)
        }
    }
}