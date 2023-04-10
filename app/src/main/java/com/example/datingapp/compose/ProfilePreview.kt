package com.example.datingapp.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.UserData
import com.github.theapache64.twyper.Twyper
import com.github.theapache64.twyper.rememberTwyperController

@Preview
@Composable
fun TwyperPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(color = whiteColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val items = remember {
            mutableStateListOf(
                UserData(userId = 0, userName = "AAAA1"),
                UserData(userId = 5, userName = "AAAA2"),
                UserData(userId = 3, userName = "AAAA3"),
                UserData(userId = 2, userName = "AAAA4"),
            )
        }
        val twyperController = rememberTwyperController()
        Twyper(
            items = items,
            twyperController = twyperController,
            onItemRemoved = { item, direction ->
                println("Item removed: $item -> $direction")
                items.remove(item)
            },
            onEmpty = {
                println("End reached")
            }
        ) { item ->
            ItemBox(item.userName, item.userId.toString())
        }
    }
}


@Composable
fun ItemBox(title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxSize(0.95f)
    ) {
        AsyncImage(
            model = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/Person_icon_BLACK-01.svg/1924px-Person_icon_BLACK-01.svg.png",
            contentDescription = "Translated description of what the image contains"
        )
        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.body2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}