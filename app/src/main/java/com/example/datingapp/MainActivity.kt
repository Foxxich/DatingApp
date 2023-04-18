package com.example.datingapp

import android.os.Bundle
import android.util.Log
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
import com.example.datingapp.screens.AccountScreen
import com.example.datingapp.screens.HomeScreen
import com.example.datingapp.screens.MessagesScreen
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.user.UserController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userControllerImpl: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavigationView()
                    Log.e("XDDD", userControllerImpl.getData())
                }
            }
        }
    }
}

@Composable
fun NavigationView() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen().Prepare(navController)
        }
        composable("messages") {
            MessagesScreen().Prepare(navController)
        }
        composable("account") {
            AccountScreen().Prepare(navController)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DatingAppTheme {

    }
}