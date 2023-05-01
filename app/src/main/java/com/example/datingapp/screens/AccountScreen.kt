package com.example.datingapp.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.datingapp.StartActivity
import com.example.datingapp.compose.BottomBar
import com.example.datingapp.firebase.FirebaseController
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.UserController

class AccountScreen : ScreenController {

    @Composable
    override fun Prepare(
        navController: NavHostController,
        firebaseController: FirebaseController,
        context: Context,
        userControllerImpl: UserController
    ) {
        Scaffold(bottomBar = { BottomBar().Prepare(navController) }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = backgroundColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    AsyncImage(
                        model = userControllerImpl.getUserPhoto().toString(),
                        contentDescription = "Translated description of what the image contains",
                        modifier = Modifier
                            .background(whiteColor)
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(whiteColor)
                            .fillMaxWidth()
                    ) {
                        Box {

                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    firebaseController.logout()
                                    val intent = Intent(context, StartActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Text("Logout")
                            }
                            Button(
                                onClick = {
                                    firebaseController.deleteUser()
                                    firebaseController.logout()
                                    val intent = Intent(context, StartActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Text("Delete account")
                            }
                        }
                    }
                }
            }
        }
    }

}