package com.example.datingapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val icon: ImageVector
) {

    object Home : BottomBarScreen(
        route = "home",
        icon = Icons.Default.Favorite
    )

    object Chat : BottomBarScreen(
        route = "chat",
        icon = Icons.Default.Email
    )

    object Profile : BottomBarScreen(
        route = "profile",
        icon = Icons.Default.Person
    )

}
