package com.example.datingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datingapp.compose.TwyperPreview
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.actionColor
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.whiteColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavigationView()
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
            HomeScreen(navController)
        }
        composable("messages") {
            MessagesScreen(navController)
        }
        composable("account") {
            AccountScreen(navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(bottomBar = {BottomBar(navController)}
    ) {
        Box(modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()
        ) {
            TwyperPreview()
        }
    }
}

@Composable
fun MessagesScreen(navController: NavHostController) {
    Scaffold(bottomBar = {BottomBar(navController)}
    ) {
        Box(modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()) {
            Text("Messages Screen")
        }
    }
}

@Composable
fun AccountScreen(navController: NavHostController) {
    Scaffold(bottomBar = {BottomBar(navController)}
    ) {
        Box(modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()) {
            Text("Account Screen")
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val selectedIndex = remember { mutableStateOf(0) }
    BottomNavigation(modifier = Modifier.padding(10.dp), elevation = 10.dp, backgroundColor = whiteColor) {

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Favorite,"", tint = actionColor)
        },
            label = { Text(text = "Home") },
            selected = (selectedIndex.value == 0),
            onClick = {
                selectedIndex.value = 0
                navController.navigate("home")
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Email,"", tint = actionColor)
        },
            label = { Text(text = "Messages") },
            selected = (selectedIndex.value == 1),
            onClick = {
                selectedIndex.value = 1
                navController.navigate("messages")
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Person,"", tint = actionColor)
        },
            label = { Text(text = "Account") },
            selected = (selectedIndex.value == 2),
            onClick = {
                selectedIndex.value = 2
                navController.navigate("account")
            })
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DatingAppTheme {

    }
}