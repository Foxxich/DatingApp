package com.example.datingapp.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.datingapp.compose.BottomBar
import com.example.datingapp.firebase.FirebaseController
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.UserController

class MessagesScreen : ScreenController {

    @Composable
    override fun Prepare(
        navController: NavHostController,
        firebaseController: FirebaseController,
        context: Context,
        userControllerImpl: UserController
    ) {
        val testDataList = mutableListOf<String>()
        testDataList.add("S1")
        testDataList.add("S12")
        testDataList.add("S13")
        testDataList.add("S14")
        testDataList.add("S15")
        Scaffold(bottomBar = { BottomBar().Prepare(navController) }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = backgroundColor
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
                        items(testDataList.size) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(16.dp)
                                ) {
                                    AsyncImage(
                                        model = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/Person_icon_BLACK-01.svg/1924px-Person_icon_BLACK-01.svg.png",
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
                                            text = "XD",
                                            style = MaterialTheme.typography.h6,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "SS",
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
    }

}