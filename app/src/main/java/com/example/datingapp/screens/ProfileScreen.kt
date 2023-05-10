package com.example.datingapp.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.datingapp.StartActivity
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseDataController
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
        GlideImage(
            model = userControllerImpl.userPhoto,
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
                        firebaseAuthController.logout()
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
                        firebaseAuthController.deleteUser()
                        firebaseAuthController.logout()
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