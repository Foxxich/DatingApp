package com.example.datingapp.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.datingapp.activities.StartActivity
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.ui.theme.Typography
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.UserController

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    context: Context,
    userControllerImpl: UserController,
    firebaseDataController: FirebaseDataController,
    firebaseAuthController: FirebaseAuthController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = whiteColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .fillMaxSize(0.95f)
            ) {
                // Upper column
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .align(Alignment.TopCenter)// Specify the height of the upper column
                ) {
                    GlideImage(
                        model = userControllerImpl.userPhoto,
                        contentDescription = "Translated description of what the image contains",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                    )
                }

                // Bottom column
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f)
                        .align(Alignment.BottomStart)// Specify the height of the bottom column
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                firebaseAuthController.logout()
                                val intent = Intent(context, StartActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Logout",
                                style = Typography.button
                            )
                        }
                        Button(
                            onClick = {
                                firebaseAuthController.deleteUser()
                                firebaseAuthController.logout()
                                firebaseDataController.deleteData(userControllerImpl.userData.userId)
                                val intent = Intent(context, StartActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Delete account",
                                style = Typography.button
                            )
                        }
                    }
                }
            }
        }
    }

}