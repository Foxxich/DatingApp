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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.datingapp.activities.ChatActivity
import com.example.datingapp.ui.theme.Shapes
import com.example.datingapp.ui.theme.Typography
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.UserController
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChatScreen(context: Context, userControllerImpl: UserController) {
    userControllerImpl.setChats()
    val matchedUsers = userControllerImpl.userDataCollection.matchedWithUsersUri
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Box(modifier = Modifier.padding(bottom = 1.dp)) {
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(Shapes.large)
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()
                    .background(whiteColor)
            ) {
                if (matchedUsers.isNotEmpty()) {
                    items(matchedUsers.size) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
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
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                GlideImage(
                                    model = matchedUsers.getValue(matchedUsers.keys.toMutableList()[it]),
                                    contentDescription = "Translated description of what the image contains",
                                    modifier = Modifier
                                        .size(100.dp, 100.dp)
                                        .align(Alignment.Center),
                                    contentScale = ContentScale.FillBounds,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1.5f)
                                    .fillMaxHeight()
                            ) {
                                Column {
                                    Text(
                                        text = matchedUsers.keys.toMutableList()[it].userName,
                                        style = Typography.h5,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    if (matchedUsers.keys.toMutableList()[it].chats.first { it.userId == userControllerImpl.userDataCollection.userData.userId }.messagesList.map { it.timestamp }
                                            .isNotEmpty()) {
                                        val dateTime = LocalDateTime.ofInstant(
                                            Instant.ofEpochSecond(matchedUsers.keys.toMutableList()[it].chats.first { it.userId == userControllerImpl.userDataCollection.userData.userId }.messagesList.map { it.timestamp }
                                                .first().seconds),
                                            ZoneId.systemDefault()
                                        )
                                        val day = dateTime.dayOfWeek.name.lowercase(Locale.ROOT)
                                        val hour = dateTime.hour.toString().padStart(2, '0')
                                        val minute = dateTime.minute.toString().padStart(2, '0')

                                        Text(
                                            text = "Last message on $day, at $hour:$minute",
                                            style = Typography.body1,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp),
                                color = backgroundColor
                            )
                        }
                    }
                }
            }
        }
    }

}