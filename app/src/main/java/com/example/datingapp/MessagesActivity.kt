package com.example.datingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.datingapp.compose.ActiveIcon
import com.example.datingapp.compose.OptionsBar
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.ui.theme.whiteColor
import com.example.datingapp.user.StartedChat

class MessagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val testData = mutableListOf<StartedChat>()
        testData.add(StartedChat(0, 1))
        testData.add(StartedChat(0, 2))
        testData.add(StartedChat(0, 3))
        testData.add(StartedChat(0, 4))
        testData.add(StartedChat(0, 5))
        setContent {
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(0.8f)
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .background(whiteColor)
                                    .fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .background(whiteColor)
                                        .fillMaxWidth()
                                ) {
                                    Text(text = "111", color = Color.Red)
                                    Divider(
                                        thickness = 1.dp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        ) {
                            OptionsBar(
                                applicationContext,
                                ActiveIcon.MESSAGES_ICON
                            ).PrepareOptionsBar()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    DatingAppTheme {

    }
}