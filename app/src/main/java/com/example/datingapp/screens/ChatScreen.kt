package com.example.datingapp.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.datingapp.ChatActivity
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.UserController

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChatScreen(context: Context, userControllerImpl: UserController) {
    userControllerImpl.setChats()
    val matchedUsers = userControllerImpl.matchedWithUsersUri
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Box(modifier = Modifier.padding(bottom = 1.dp)) {
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()
                    .background(whiteColor)
            ) {
                items(matchedUsers.size) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent =
                                Intent(context, ChatActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra(
                                "USER_ID",
                                matchedUsers.keys.toMutableList()[it].userId
                            )
                            context.startActivity(intent)
                        }) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                        ) {
                            GlideImage(
                                model = matchedUsers.getValue(matchedUsers.keys.toMutableList()[it]),
                                contentDescription = "Translated description of what the image contains"
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = matchedUsers.keys.toMutableList()[it].userName,
                                    style = MaterialTheme.typography.h6,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = matchedUsers.keys.toMutableList()[it].userId,
                                    style = MaterialTheme.typography.body2,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    }

}