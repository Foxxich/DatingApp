package com.example.datingapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.ui.theme.DatingAppTheme
import com.example.datingapp.user.Message
import com.example.datingapp.user.UserController
import com.example.datingapp.user.UserData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : ComponentActivity(), UserDataObserver {

    @Inject
    lateinit var userControllerImpl: UserController

    @Inject
    lateinit var firebaseDataController: FirebaseDataController

    lateinit var chatId: String

    private var exampleMessages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseDataController.addObserver(userControllerImpl)
        firebaseDataController.addObserver(this)
        chatId = intent.getStringExtra("USER_ID").toString()
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun ChatScreen() {
        exampleMessages.clear()
        val chat =
            userControllerImpl.userData.chats.first { it.userId == chatId }
        var messageText by remember { mutableStateOf(TextFieldValue()) }
        var sentMessage by remember { mutableStateOf<String?>(null) }
        chat.messagesList.forEach {
            exampleMessages.add(it)
        }
        exampleMessages.sortByDescending { it.timestamp.seconds }
        Scaffold {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(exampleMessages) { message ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = if (message.sender == "Me") Arrangement.End else Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(if (message.sender == "Me") Color.Blue else Color.LightGray)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = message.text,
                                    color = if (message.sender == "Me") Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        label = { Text("Message") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            sentMessage = messageText.text
                            messageText = TextFieldValue()
                            userControllerImpl.updateChats(textMessage = sentMessage!!, chatId)
                        }
                    ) {
                        Text("Send")
                    }
                }
            }
        }
    }

    override fun dataChanged(userData: UserData) {
        setContent {
            ChatScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DatingAppTheme {
    }
}