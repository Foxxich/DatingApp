package com.example.datingapp.screens

import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.datingapp.ui.theme.Typography
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.Interest
import com.example.datingapp.user.UserController
import com.github.theapache64.twyper.Twyper
import com.github.theapache64.twyper.rememberTwyperController

@Composable
fun ProfilePreview(userController: UserController) {
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
        val notSwipedUsersList = remember { userController.notSwipedUsersUri.keys.toMutableList() }
        if (notSwipedUsersList.size > 0) {
            val profileController = rememberTwyperController()
            Twyper(
                items = notSwipedUsersList,
                twyperController = profileController,
                onItemRemoved = { item, direction ->
                    println("Item removed: $item -> $direction")
                    if (direction.toString() == "RIGHT") {
                        userController.updateSwipes(item.userId, true)
                        userController.notSwipedUsersUri.remove(item)
                        notSwipedUsersList.remove(item)
                    } else {
                        userController.updateSwipes(item.userId, false)
                        userController.notSwipedUsersUri.remove(item)
                        notSwipedUsersList.remove(item)
                    }
                },
                onEmpty = {
                    UnavailableUsersText()
                }
            ) { item ->
                ItemBox(item.userName, userController.notSwipedUsersUri.getValue(item), item.interests)
            }
        } else {
            UnavailableUsersText()
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemBox(title: String, userPhotoUri: Uri?, interests: List<Interest>) {
    Box(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxSize(0.95f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = userPhotoUri,
                contentDescription = "Translated description of what the image contains",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = interests.toString(),
                style = MaterialTheme.typography.body2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun UnavailableUsersText() {
    Text(
        text = "There are no available users",
        style = Typography.h1
    )
}