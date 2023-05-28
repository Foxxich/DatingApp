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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.datingapp.activities.SetUpActivity
import com.example.datingapp.activities.StartActivity
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.ui.theme.Shapes
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
    val userDataCollection = userControllerImpl.userDataCollection
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
                .clip(Shapes.large)
                .background(color = whiteColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .fillMaxSize(0.95f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .align(Alignment.TopCenter)// Specify the height of the upper column
                ) {
                    GlideImage(
                        model = userDataCollection.userPhoto,
                        contentDescription = "Translated description of what the image contains",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(Shapes.large)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                        .align(Alignment.BottomStart)// Specify the height of the bottom column
                ) {
                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        Row {
                            Text(
                                text = "Name: " + userDataCollection.userData.userName,
                                style = MaterialTheme.typography.h6,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row(modifier = Modifier.padding(bottom = 5.dp)) {
                            Text(
                                text = "Interests: " + userDataCollection.userData.interests.toString()
                                    .replace("[", "").replace("]", ""),
                                style = MaterialTheme.typography.body1,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp),
                            color = backgroundColor
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    val intent =
                                        Intent(context, SetUpActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.putExtra(
                                        "IS_NEW_USER",
                                        false
                                    )
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(end = 5.dp)
                            ) {
                                Text(
                                    text = "Change details",
                                    style = Typography.button
                                )
                            }
                            Button(
                                onClick = {
                                    userDataCollection.userData.swiped.clear()
                                    userControllerImpl.uploadToDatabase(userDataCollection)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(start = 5.dp)
                            ) {
                                Text(
                                    text = "Clear data",
                                    style = Typography.button,
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
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
                                    .fillMaxWidth()
                                    .padding(end = 5.dp)
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
                                    firebaseDataController.deleteData(userDataCollection.userData.userId)
                                    val intent = Intent(context, StartActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(start = 5.dp)
                            ) {
                                Text(
                                    text = "Delete account",
                                    style = Typography.button,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}