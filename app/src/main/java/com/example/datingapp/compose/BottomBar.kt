package com.example.datingapp.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.datingapp.ui.theme.actionColor
import com.example.datingapp.ui.theme.whiteColor

class BottomBar {

    @Composable
    fun Prepare(navController: NavHostController) {
        val selectedIndex = remember { mutableStateOf(0) }
        BottomNavigation(
            modifier = Modifier.padding(10.dp),
            elevation = 10.dp,
            backgroundColor = whiteColor
        ) {

            BottomNavigationItem(icon = {
                Icon(imageVector = Icons.Default.Favorite, "", tint = actionColor)
            },
                label = { Text(text = "Home") },
                selected = (selectedIndex.value == 0),
                onClick = {
                    selectedIndex.value = 0
                    navController.navigate("home")
                })

            BottomNavigationItem(icon = {
                Icon(imageVector = Icons.Default.Email, "", tint = actionColor)
            },
                label = { Text(text = "Messages") },
                selected = (selectedIndex.value == 1),
                onClick = {
                    selectedIndex.value = 1
                    navController.navigate("messages")
                })

            BottomNavigationItem(icon = {
                Icon(imageVector = Icons.Default.Person, "", tint = actionColor)
            },
                label = { Text(text = "Account") },
                selected = (selectedIndex.value == 2),
                onClick = {
                    selectedIndex.value = 2
                    navController.navigate("account")
                })
        }
    }

}