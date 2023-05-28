package com.example.datingapp.activities

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.datingapp.chat.Message
import com.example.datingapp.connection.InternetCheckService
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.ui.theme.Shapes
import com.example.datingapp.ui.theme.Typography
import com.example.datingapp.ui.theme.additionalColor
import com.example.datingapp.ui.theme.backgroundColor
import com.example.datingapp.ui.theme.otherUserChatColor
import com.example.datingapp.user.UserController
import com.example.datingapp.user.UserData
import com.example.datingapp.user.UserDataObserver
import com.example.datingapp.utils.CommonSettings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : ComponentActivity(), UserDataObserver {

    @Inject
    lateinit var userControllerImpl: UserController

    @Inject
    lateinit var firebaseDataController: FirebaseDataController

    @Inject
    lateinit var internetCheckService: InternetCheckService

    private lateinit var chatId: String

    private var messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseDataController.addObserver(userControllerImpl)
        firebaseDataController.addObserver(this)
        chatId = intent.getStringExtra("USER_ID").toString()

        if (!internetCheckService.isInternetConnected(applicationContext)) {
            CommonSettings.showConnectionLost(applicationContext)
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun ChatScreen() {
        messages.clear()
        var messageText by remember { mutableStateOf(TextFieldValue()) }
        var sentMessage by remember { mutableStateOf<String?>(null) }
        userControllerImpl.userDataCollection.userData.chats.first { it.userId == chatId }
            .messagesList.forEach {
                messages.add(it)
            }
        messages.sortByDescending { it.timestamp.seconds }
        Scaffold {
            Column(
                modifier = Modifier
                    .background(backgroundColor)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(messages) { message ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = if (message.sender == "Me") Arrangement.End else Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (message.sender == "Me") additionalColor else otherUserChatColor,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                                    .clip(Shapes.large)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                        },
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Send",
                            style = Typography.button
                        )
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
